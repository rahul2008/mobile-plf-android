package com.philips.cl.di.dev.pa.datamodel ;

import java.io.Serializable;

public class OutdoorAQIEventDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int [] idx ;
	private int [] pm10 ;
	private int [] pm25 ;
	private int [] so2 ;
	
	private int [] no2 ;
	public int[] getPm10() {
		return pm10;
	}
	public void setPm10(int[] pm10) {
		this.pm10 = pm10;
	}
	private String t ;
	public int[] getIdx() {
		return idx;
	}
	public void setIdx(int[] idx) {
		this.idx = idx;
	}
	
	public int[] getPm25() {
		return pm25;
	}
	public void setPm25(int[] pm25) {
		this.pm25 = pm25;
	}
	public int[] getSo2() {
		return so2;
	}
	public void setSo2(int[] so2) {
		this.so2 = so2;
	}
	public int[] getNo2() {
		return no2;
	}
	public void setNo2(int[] no2) {
		this.no2 = no2;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
}
