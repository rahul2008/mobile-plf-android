package com.philips.cl.di.dev.pa.dashboard;

public class DashboardAPL {
	
	private int pointerBackground;
	private int pointerRotation;
	private int title;
	private int summary;
	
	public DashboardAPL(int pointerBackground, int pointerRotation, int title, int summary) {
		this.pointerBackground = pointerBackground;
		this.pointerRotation = pointerRotation;
		this.title = title;
		this.summary = summary;
	}
	public int getPointerBackground() {
		return pointerBackground;
	}
	public int getPointerRotation() {
		return pointerRotation;
	}
	public int getTitle() {
		return title;
	}
	public int getSummary() {
		return summary;
	}
	
	
	
}
