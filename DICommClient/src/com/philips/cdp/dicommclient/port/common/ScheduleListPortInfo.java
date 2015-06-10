package com.philips.cdp.dicommclient.port.common;


public class ScheduleListPortInfo implements Comparable<ScheduleListPortInfo> {
	private String time;
	private String days ;
	private String mode ;
	private int scheduleNumber ;
	private String name ;
	private boolean enabled;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getScheduleTime() {
		return time;
	}
	public int getScheduleNumber() {
		return scheduleNumber;
	}
	public void setScheduleNumber(int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}
	public void setScheduleTime(String scheduleTime) {
		this.time = scheduleTime;
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
	
	@Override
	public int compareTo(ScheduleListPortInfo port) {
		return name.compareTo(port.getName());
	}	
}
