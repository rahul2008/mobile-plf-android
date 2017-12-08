/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.PortProperties;

import static android.text.TextUtils.isEmpty;
import static java.lang.Math.floor;
import static java.lang.Math.max;

/**
 * The FirmwarePortProperties class holds information about the state of the {@link FirmwarePort}.
 * @publicApi
 */
public class FirmwarePortProperties implements PortProperties {

    public static final int INVALID_INT_VALUE = Integer.MIN_VALUE;
    public static final double BASE64_FACTOR = .75;

    /**
     * Property names from the FirmwarePort
     */
    public enum FirmwarePortKey {
        NAME("name"),
        VERSION("version"),
        UPGRADE("upgrade"),
        STATE("state"),
        PROGRESS("progress"),
        STATUS_MESSAGE("statusmsg"),
        MANDATORY("mandatory"),
        CAN_UPGRADE("canupgrade"),
        MAX_CHUNK_SIZE("maxchunksize"),
        SIZE("size"),
        DATA("data");

        private final String keyName;

        FirmwarePortKey(String keyName) {
            this.keyName = keyName;
        }

        @Override
        public String toString() {
            return keyName;
        }
    }

    /**
     * States that the {@link FirmwarePort} can be in
     */
    public enum FirmwarePortState {
        IDLE("idle"),
        PREPARING("preparing"),
        DOWNLOADING("downloading"),
        CHECKING("checking"),
        READY("ready"),
        PROGRAMMING("go", "programming"),
        CANCELING("cancel", "canceling", "cancelling"),
        ERROR("error"),
        UNKNOWN("unknown");

        private final String[] stateNames;

        FirmwarePortState(String... stateNames) {
            this.stateNames = stateNames;
        }

        @Override
        public String toString() {
            return stateNames[0];
        }

        public static FirmwarePortState fromString(@NonNull String stateString) {
            for (FirmwarePortState state : FirmwarePortState.values()) {
                for (String stateName : state.stateNames) {
                    if (stateName.equalsIgnoreCase(stateString)) {
                        return state;
                    }
                }
            }
            return UNKNOWN;
        }
    }

    // By default, always support upgrades if the device doesn't provide a value.
    private boolean canupgrade = true;
    private boolean mandatory;
    private int maxchunksize;
    private int progress;
    private int size = 0;
    private String name;
    private String state = "";
    private String statusmsg;
    private String upgrade = "";
    private String version = "";

    /**
     * Return the name of the firmware
     * @return String The name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the version code of the firmware
     * @return String The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns a String when the appliance is capable of downloading a different firmware version.
     * When empty, there is no version available to use.
     * See #isUpdateAvailable
     * @return String
     */
    public String getUpgrade() {
        return upgrade;
    }

    /**
     * Returns the current state of the {@link FirmwarePort}
     * @return FirmwarePortState The current state
     */
    public FirmwarePortState getState() {
        return FirmwarePortState.fromString(state);
    }

    /**
     * Returns an indication of the progress when upgrading firmware.
     * The progress is the number of bytes that have been processed.
     * @return int The progress of the upgrading action
     */
    public int getProgress() {
        return max(0, progress);
    }

    /**
     * Returns a status message
     * @return String The status message
     */
    public String getStatusMessage() {
        return statusmsg;
    }

    /**
     * Returns a boolean to indicate if the appliance supports firmware upgrade.
     * @return boolean True if appliance supports firmware upgrade, false otherwise
     */
    public boolean canUpgrade() {
        return canupgrade;
    }

    /**
     * Returns a boolean to indicate whether the firmware version was mandatory to install.
     * @return boolean True if it was a mandatory version, false otherwise.
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Returns the maximum chunk size that the appliance can accept when uploading new firmware.
     * @return int The maximum chunk size
     */
    public int getMaxChunkSize() {
        return (int) floor(maxchunksize * BASE64_FACTOR);
    }

    /**
     * Returns the size of the firmware
     * @return int The size of the upgrade that is being executed.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a boolean to indicate if there is a firmware update available.
     * @return boolean True if the appliance has an update available to install, false otherwise.
     */
    public boolean isUpdateAvailable() {
        return !isEmpty(upgrade);
    }

    /**
     * Returns a boolean to indicate if the firmware version is valid.
     * @return boolean True if the firmware is valid, false otherwise.
     */
    public boolean isValid() {
        return !isEmpty(name) && !isEmpty(version) && progress != INVALID_INT_VALUE;
    }
}
