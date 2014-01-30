package com.philips.cl.di.dev.pa.dto;

import java.util.Map;

public class Gov {

	String co;
	String idx;
	String pm10;
	Map<String, Stations> stations;
	String so2;
	String no2;
	String o3;
	String pm25;
	String t;

	/**
	 * @return the so2
	 */
	public String getSo2() {
		return so2;
	}

	/**
	 * @param so2
	 *            the so2 to set
	 */
	public void setSo2(String so2) {
		this.so2 = so2;
	}

	/**
	 * @return the no2
	 */
	public String getNo2() {
		return no2;
	}

	/**
	 * @param no2
	 *            the no2 to set
	 */
	public void setNo2(String no2) {
		this.no2 = no2;
	}

	/**
	 * @return the o3
	 */
	public String getO3() {
		return o3;
	}

	/**
	 * @param o3
	 *            the o3 to set
	 */
	public void setO3(String o3) {
		this.o3 = o3;
	}

	/**
	 * @return the pm25
	 */
	public String getPm25() {
		return pm25;
	}

	/**
	 * @param pm25
	 *            the pm25 to set
	 */
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	/**
	 * @return the t
	 */
	public String getT() {
		return t;
	}

	/**
	 * @param t
	 *            the t to set
	 */
	public void setT(String t) {
		this.t = t;
	}

	/**
	 * @return the co
	 */
	public String getCo() {
		return co;
	}

	/**
	 * @param co
	 *            the co to set
	 */
	public void setCo(String co) {
		this.co = co;
	}

	/**
	 * @return the idx
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @param idx
	 *            the idx to set
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @return the pm10
	 */
	public String getPm10() {
		return pm10;
	}

	/**
	 * @param pm10
	 *            the pm10 to set
	 */
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	/**
	 * @return the stations
	 */
	public Map<String, Stations> getStations() {
		return stations;
	}

	/**
	 * @param stations
	 *            the stations to set
	 */
	public void setStations(Map<String, Stations> stations) {
		this.stations = stations;
	}

	@Override
	public String toString() {
		return idx;
	}
}
