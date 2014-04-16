package com.philips.cl.di.dev.pa.firmware;

public class FirmwareConstants {
	
	//Fragment tags
	public static final String FIRMWARE_DOWNLOAD_FRAGMENT = "FirmwareDownloadFragment";
	public static final String FIRMWARE_INSTALL_FRAGMENT = "FirmwareInstallFragment";
	public static final String FIRMWARE_DOWNLOAD_FAILED_FRAGMENT = "FirmwareDownloadFailedFragment";
	public static final String FIRMWARE_FAILED_SUPPORT_FRAGMENT = "FirmwareFailedSupportFragment";
	public static final String FIRMWARE_CONTACT_SUPPORT_FRAGMENT = "FirmwareContactSupportFragment";
	public static final String FIRMWARE_INSTALL_SUCCESS_FRAGMENT = "FirmwareInstallSuccessFragment";

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
}
