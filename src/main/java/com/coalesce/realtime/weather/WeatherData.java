package com.coalesce.realtime.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherData {

	@SerializedName("weather")
	private PrecipitationData precipitationData;

	class PrecipitationData {

		private String weatherId;
		private String description;

		public String getWeatherId() {
			return weatherId;
		}

		public String getDescription() {
			return description;
		}
	}

	public PrecipitationData getPrecipitationData() {
		return precipitationData;
	}
}
