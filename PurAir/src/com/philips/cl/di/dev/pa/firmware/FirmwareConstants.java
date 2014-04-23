package com.philips.cl.di.dev.pa.firmware;

public class FirmwareConstants {
	
	//Fragment IDs
	public enum FragmentID {
		FIRMWARE_UPDATE,
		FIRMWARE_INSTALL,
		FIRMWARE_INSTALLED,
		FIRMWARE_DOWNLOAD,
		FIRMWARE_DOWNLOAD_FAILED,
		FIRMWARE_CONTACT_SUPPORT,
		FIRMWARE_FAILED_SUPPORT,
		FIRMWARE_INSTALL_SUCCESS,
	}
	
	//Firmware JSON keys
	public static final String VERSION = "version";
	public static final String PROGRESS = "progress";
	public static final String STATE = "state";
	public static final String UPGRADE = "upgrade";
	public static final String IDLE = "idle";
	
	//Firmware properties.
	public static final String CANCEL = "cancel";
	public static final String GO = "go";
}
