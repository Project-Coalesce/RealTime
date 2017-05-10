package com.coalesce.realtime.time;

import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.realtime.location.LocationData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TimeModule extends CoModule {

	HashMap<UUID, Calendar> timezomeCalendars;

	private BukkitTask timeUpdater;

	//May change this to a config option. As of now, it is set to 10 secs
	private static final int UPDATE_SPEED = 10 * 20;

	private static final long MINECRAFT_MIDNIGHT = 18000;
	private static final long MINECRAFT_DAY_LENGTH = 24000;
	private static final long MINS_PER_DAY = 60 * 24;

	public TimeModule(CoPlugin plugin) {
		super(plugin, "Time Module");

		timezomeCalendars = new HashMap<>();
	}

	@Override
	protected void onEnable() throws Exception {

		//Freeze the time for all worlds
		Bukkit.getWorlds().forEach(world -> {
			world.setGameRuleValue("doDaylightCycle", "false");
			world.setTime(0);
		});

		timeUpdater = Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

			timezomeCalendars.forEach(this::updatePlayerTime);

		}, UPDATE_SPEED, UPDATE_SPEED);

		getPlugin().registerListener(this);
	}

	@Override
	protected void onDisable() throws Exception {

	}

	private void updatePlayerTime(UUID uuid, Calendar timezoneCalendar){

		Player player = Bukkit.getPlayer(uuid);
		timezoneCalendar.setTimeInMillis(System.currentTimeMillis());

		getPlugin().getCoLogger().debug("Updated Player time");

		final int minsIntoDay = (timezoneCalendar.get(Calendar.HOUR_OF_DAY) * 60) + timezoneCalendar.get(Calendar.MINUTE);
		final long newTime = MINECRAFT_MIDNIGHT + ((minsIntoDay * MINECRAFT_DAY_LENGTH) / MINS_PER_DAY);

		player.setPlayerTime(newTime, false);
	}

	public void loadTimeData(UUID uuid, LocationData locationData){

		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(locationData.getTimeZone()));

		if (timezomeCalendars.containsKey(uuid)){
			timezomeCalendars.replace(uuid, calendar);
			return;
		}

		timezomeCalendars.put(uuid, calendar);

		updatePlayerTime(uuid, calendar);
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event){

		timezomeCalendars.remove(event.getPlayer().getUniqueId());

	}
}
