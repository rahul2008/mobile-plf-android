package com.philips.cl.di.dev.pa.datamodel;

public class OutdoorAQIEventDto {

	private int[] idx;
	private int[] pm10;
	private int[] pm25;
	private int[] so2;
	private int[] no2;
	private String t;

	public OutdoorAQIEventDto(int[] idx, int[] pm10, int[] pm25, int[] so2,
			int[] no2, String t) {
		this.idx = idx;
		this.pm10 = pm10;
		this.pm25 = pm25;
		this.so2 = so2;
		this.no2 = no2;
		this.t = t;
	}

	public int[] getPm10() {
		if (pm10 == null)
			return null;
		return pm10.clone();
	}

	public int[] getIdx() {
		if (idx == null)
			return null;
		return idx.clone();
	}

	public int[] getPm25() {
		if (pm25 == null)
			return null;
		return pm25.clone();
	}

	public int[] getSo2() {
		if (so2 == null)
			return null;
		return so2.clone();
	}

	public int[] getNo2() {
		if (no2 == null)
			return null;
		return no2.clone();
	}

	public String getT() {
		return t;
	}

}
