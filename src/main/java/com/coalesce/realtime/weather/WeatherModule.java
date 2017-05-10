package com.coalesce.realtime.weather;

import com.coalesce.http.CoHTTP;
import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.realtime.location.LocationData;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class WeatherModule extends CoModule {


	private Map<UUID, WeatherData> weatherDataMap;

	private static final String WEATHER_API_URL = "api.openweathermap.org/data/2.5/weather?";

	public WeatherModule(CoPlugin plugin) {
		super(plugin, "Weather Module");

		weatherDataMap 	= new HashMap<>();
	}

	@Override
	protected void onEnable() throws Exception {

	}

	@Override
	protected void onDisable() throws Exception {

	}

	private void loadPlayerWeather(Player player, LocationData locationData){


		ListenableFuture<String> future = CoHTTP.sendGet(WEATHER_API_URL + "lat=" + locationData.getLatitude() + "&lon=" + locationData.getLongitude(), getPlugin().getDisplayName() + " Spigot Plugin");

		future.addListener(() -> {

			try {
				WeatherData data = new Gson().fromJson(future.get(), WeatherData.class);
				weatherDataMap.put(player.getUniqueId(), data);

				updatePlayerWeather(player, data);

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}, r -> Bukkit.getScheduler().runTask(getPlugin(), r));
	}

	private void updatePlayerWeather(Player player, WeatherData weatherData){

	}

}
