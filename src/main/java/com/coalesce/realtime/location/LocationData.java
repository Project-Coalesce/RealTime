package com.coalesce.realtime.location;

import com.google.gson.annotations.SerializedName;

public class LocationData {

	@SerializedName("ip")
	private String address;

	@SerializedName("country_name")
	private String countryName;

	@SerializedName("region_name")
	private String regionName;

	private String city;

	@SerializedName("time_zone")
	private String timeZone;

	private String latitude;
	private String longitude;

	public String getAddress() {
		return address;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getCity() {
		return city;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
}
