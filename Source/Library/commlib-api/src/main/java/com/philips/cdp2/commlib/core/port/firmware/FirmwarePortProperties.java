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
 * @publicApi
 */
public class FirmwarePortProperties implements PortProperties {

    public static final int INVALID_INT_VALUE = Integer.MIN_VALUE;
    public static final double BASE64_FACTOR = .75;

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

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public FirmwarePortState getState() {
        return FirmwarePortState.fromString(state);
    }

    public int getProgress() {
        return max(0, progress);
    }

    public String getStatusMessage() {
        return statusmsg;
    }

    public boolean canUpgrade() {
        return canupgrade;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public int getMaxChunkSize() {
        return (int) floor(maxchunksize * BASE64_FACTOR);
    }

    public int getSize() {
        return size;
    }

    public boolean isUpdateAvailable() {
        return !isEmpty(upgrade);
    }

    public boolean isValid() {
        return !isEmpty(name) && !isEmpty(version) && progress != INVALID_INT_VALUE;
    }
}
