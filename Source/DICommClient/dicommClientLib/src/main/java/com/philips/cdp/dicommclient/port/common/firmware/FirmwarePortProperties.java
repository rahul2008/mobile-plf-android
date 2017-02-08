/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

public class FirmwarePortProperties {

	public static int INVALID_INT_VALUE = Integer.MIN_VALUE;

	public enum FirmwareState {
		IDLE, PREPARING, DOWNLOADING, CHECKING, READY, PROGRAMMING, CANCELING, ERROR
	}

	private String name = "";
	private String version = "";
	private String upgrade = "";
	private String state = "";
	private int progress = -1;
	private String statusmsg = "";
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
		} else if (state.equalsIgnoreCase("preparing")) {
			return FirmwareState.PREPARING;
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
		if (progress < 0)
			return 0;
		if (progress > 100)
			return 100;
		return progress;
	}

	public String getStatusmsg() {
		return statusmsg;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public boolean isUpdateAvailable() {
		if (upgrade == null || upgrade.equalsIgnoreCase(""))
			return false;
		return true;
	}

	public boolean isValid() {
		return !(name.isEmpty() || version.isEmpty() || progress == INVALID_INT_VALUE);
	}
}
