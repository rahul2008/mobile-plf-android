package com.philips.cl.di.dev.pa.datamodel;

public class Stations {
	String co;
	String idx;

	String lat;

	String lon;

	String no2;

	String o3;

	String pm10;

	String pm25;

	String so2;

	String station_name;

	String t;

	/**
	 * @return the co
	 */
	public String getCo() {
		return co;
	}

	/**
	 * @return the idx
	 */
	public String getIdx() {
		return idx;
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
	 * @return the no2
	 */
	public String getNo2() {
		return no2;
	}

	/**
	 * @return the o3
	 */
	public String getO3() {
		return o3;
	}

	/**
	 * @return the pm10
	 */
	public String getPm10() {
		return pm10;
	}

	/**
	 * @return the pm25
	 */
	public String getPm25() {
		return pm25;
	}

	/**
	 * @return the so2
	 */
	public String getSo2() {
		return so2;
	}

	/**
	 * @return the station_name
	 */
	public String getStation_name() {
		return station_name;
	}

	/**
	 * @return the t
	 */
	public String getT() {
		return t;
	}

	/**
	 * @param co
	 *            the co to set
	 */
	public void setCo(String co) {
		this.co = co;
	}

	/**
	 * @param idx
	 *            the idx to set
	 */
	public void setIdx(String idx) {
		this.idx = idx;
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
	 * @param no2
	 *            the no2 to set
	 */
	public void setNo2(String no2) {
		this.no2 = no2;
	}

	/**
	 * @param o3
	 *            the o3 to set
	 */
	public void setO3(String o3) {
		this.o3 = o3;
	}

	/**
	 * @param pm10
	 *            the pm10 to set
	 */
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	/**
	 * @param pm25
	 *            the pm25 to set
	 */
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	/**
	 * @param so2
	 *            the so2 to set
	 */
	public void setSo2(String so2) {
		this.so2 = so2;
	}

	/**
	 * @param station_name
	 *            the station_name to set
	 */
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	/**
	 * @param t
	 *            the t to set
	 */
	public void setT(String t) {
		this.t = t;
	}

	@Override
	public String toString() {
		return "co :"+ co + " idx:" + idx + " lat:" +

		        lat + " lon:" + lon + " no2:" +

		        no2 + " o3:" + o3 + " pm10:" +

		        pm10 + " pm25:" + pm25 + " so2:" +

		        so2 + " station_name: " + station_name + " t:" +

		        t;
	}

}
