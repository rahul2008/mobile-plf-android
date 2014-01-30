package com.philips.cl.di.dev.pa.dto;

public class City {
	private String lon;
	private String city_name;
	private String weather;
	private String key;

	private String lat;
	private Gov gov;

	/**
	 * @return the city_name
	 */
	public String getCity_name() {
		return city_name;
	}

	/**
	 * @return the gov
	 */
	public Gov getGov() {
		return gov;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}

	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param city_name
	 *            the city_name to set
	 */
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	/**
	 * @param gov
	 *            the gov to set
	 */
	public void setGov(Gov gov) {
		this.gov = gov;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @param lon
	 *            the lon to set
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * @param weather
	 *            the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof City) {
			City mycity = (City) obj;
			if (mycity.getKey() == key) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return "lon " + lon + " city_name " + city_name + " weather " + weather + " key "
		        + key + " lat" + lat  + "gov"+gov;
	}
}
