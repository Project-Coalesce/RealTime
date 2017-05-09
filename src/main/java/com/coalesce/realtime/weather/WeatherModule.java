package com.coalesce.realtime.weather;

import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

}
