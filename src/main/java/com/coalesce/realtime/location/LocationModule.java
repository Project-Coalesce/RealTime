package com.coalesce.realtime.location;

import com.coalesce.http.CoHTTP;
import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.realtime.RealTimePlugin;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class LocationModule extends CoModule {

	private final Map<UUID, LocationData> locationDataMap;

	private BukkitTask locationUpdater;

	private static final String LOCATION_API_URL = "http://freegeoip.net/json/";
	//May change this to a config option. As of now, it is set to 1 min
	private static final int UPDATE_SPEED = 60 * 20;

	public LocationModule(CoPlugin plugin) {
		super(plugin, "Location Module");

		locationDataMap = new HashMap<>();
	}

	@Override
	protected void onEnable() {

		Bukkit.getOnlinePlayers().forEach(this::loadLocationData);

		//Start the updater task
		locationUpdater = Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

			//Update the locations of all players who are not in the locationMap
			Bukkit.getOnlinePlayers().stream().filter(player -> !locationDataMap.containsKey(player.getUniqueId())).forEach(this::loadLocationData);

		}, UPDATE_SPEED, UPDATE_SPEED);

		//Register the eventhandlers
		getPlugin().registerListener(this);
	}

	@Override
	protected void onDisable() {

		locationUpdater.cancel();
		locationDataMap.clear();

	}

	public Map<UUID, LocationData> getLocationDataMap() {
		//Returning a copy so that we dont have Concurrency issues
		return new HashMap<>(locationDataMap);
	}

	private void loadLocationData(Player player){

		getPlugin().getCoLogger().debug("Downloaded player location");
		ListenableFuture<String> future = CoHTTP.sendGet(LOCATION_API_URL + player.getAddress().getAddress().getHostAddress(), getPlugin().getDisplayName() + " Spigot Plugin");

		future.addListener(() -> {

			try {
				LocationData data = new Gson().fromJson(future.get(), LocationData.class);
				locationDataMap.put(player.getUniqueId(), data);
				//Load in the players time data
				((RealTimePlugin)getPlugin()).getTimeModule().loadTimeData(player.getUniqueId(), data);

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}, r -> Bukkit.getScheduler().runTask(getPlugin(), r));
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){

		loadLocationData(event.getPlayer());

	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event){

		locationDataMap.remove(event.getPlayer().getUniqueId());

	}
}
