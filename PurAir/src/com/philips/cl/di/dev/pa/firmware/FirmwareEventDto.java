package com.philips.cl.di.dev.pa.firmware;

public class FirmwareEventDto {
	
	public enum FirmwareState {
		IDLE, DOWNLOADING, CHECKING, READY, PROGRAMMING, CANCELING, ERROR
	}
	
	private String name;
	private String version = "";
	private String upgrade = "";
	private String state = "";
	private int progress;
	private String statusmsg;
	private boolean mandatory;
	
	public String getName() {
		return name;
	}
	public String getVersion() {
		return version;
	}
	public String getUpgrade() {
		return upgrade;
	}
	public FirmwareState getState() {
		if (state == null) {
			return null;
		} else if (state.equalsIgnoreCase("idle")) {
			return FirmwareState.IDLE;
		} else if (state.equals("downloading")) {
			return FirmwareState.DOWNLOADING;
		} else if (state.equals("checking")) {
			return FirmwareState.CHECKING;
		} else if (state.equals("ready")) {
			return FirmwareState.READY;
		} else if (state.equals("programming")) {
			return FirmwareState.PROGRAMMING;
		} else if (state.equals("cancelling")) {
			return FirmwareState.CANCELING;
		} else if (state.equals("error")) {
			return FirmwareState.ERROR;
		}
		return null;
	}
	
	public int getProgress() {
		if (progress < 0) return 0;
		if (progress > 100) return 100;
		return progress;
	}
	public String getStatusmsg() {
		return statusmsg;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	
	public boolean isUpdateAvailable() {
		if (upgrade == null || upgrade.equalsIgnoreCase("")) return false;
		return true;
	}
}
