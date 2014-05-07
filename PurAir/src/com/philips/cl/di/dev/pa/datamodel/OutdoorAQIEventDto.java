package com.philips.cl.di.dev.pa.datamodel ;

public class OutdoorAQIEventDto {
	
	private int [] idx ;
	private int [] pm10 ;
	private int [] pm25 ;
	private int [] so2 ;
	private int [] no2 ;
	private String t;
	
	public int[] getPm10() {
		if (pm10 == null) return null;
		return pm10.clone();
	}
	public void setPm10(final int[] pm10) {
		this.pm10 = pm10;
	}
	
	public int[] getIdx() {
		if (idx == null) return null;
		return idx.clone();
	}
	public void setIdx(final int[] idx) {
		this.idx = idx;
	}
	
	public int[] getPm25() {
		if (pm25 == null) return null;
		return pm25.clone();
	}
	public void setPm25(final int[] pm25) {
		this.pm25 = pm25;
	}
	public int[] getSo2() {
		if (so2 == null) return null;
		return so2.clone();
	}
	public void setSo2(final int[] so2) {
		this.so2 = so2;
	}
	public int[] getNo2() {
		if (no2 == null) return null;
		return no2.clone();
	}
	public void setNo2(final int[] no2) {
		this.no2 = no2;
	}
	public String getT() {
		return t;
	}
	public void setT(final String t) {
		this.t = t;
	}
}
