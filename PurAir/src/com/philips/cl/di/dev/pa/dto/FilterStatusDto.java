package com.philips.cl.di.dev.pa.dto;

public class FilterStatusDto {
	private int preFilterStatus;
	private int multiCareFilterStatus;
	private int activeCarbonFilterStatus;
	private int hepaFilterStatus;
	
	public int getPreFilterStatus() {
		return preFilterStatus;
	}
	public void setPreFilterStatus(int preFilterStatus) {
		this.preFilterStatus = preFilterStatus;
	}
	public int getMultiCareFilterStatus() {
		return multiCareFilterStatus;
	}
	public void setMultiCareFilterStatus(int multiCareFilterStatus) {
		this.multiCareFilterStatus = multiCareFilterStatus;
	}
	public int getActiveCarbonFilterStatus() {
		return activeCarbonFilterStatus;
	}
	public void setActiveCarbonFilterStatus(int activeCarbonFilterStatus) {
		this.activeCarbonFilterStatus = activeCarbonFilterStatus;
	}
	public int getHepaFilterStatus() {
		return hepaFilterStatus;
	}
	public void setHepaFilterStatus(int hepaFilterStatus) {
		this.hepaFilterStatus = hepaFilterStatus;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
