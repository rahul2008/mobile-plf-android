package com.philips.cl.di.dev.pa.firmware;

public class FirmwareEventDto {
	
	private String name;
	private int version;
	private int upgrade;
	private String state;
	private String progress;
	private String statusmsg;
	private boolean mandatory;
	
	public String getName() {
		return name;
	}
	public int getVersion() {
		return version;
	}
	public int getUpgrade() {
		return upgrade;
	}
	public String getState() {
		return state;
	}
	public String getProgress() {
		return progress;
	}
	public String getStatusmsg() {
		return statusmsg;
	}
	public boolean isMandatory() {
		return mandatory;
	}
}
