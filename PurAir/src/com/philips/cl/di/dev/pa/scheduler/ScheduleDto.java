package com.philips.cl.di.dev.pa.scheduler;

public class ScheduleDto {
	private String scheduleTime;
	private String days ;
	private String mode ;
	private int scheduleNumber ;
	private String name ;
	
	public String getScheduleTime() {
		return scheduleTime;
	}
	public int getScheduleNumber() {
		return scheduleNumber;
	}
	public void setScheduleNumber(int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
