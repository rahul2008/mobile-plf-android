package com.philips.pins.shinelib.dicommsupport.ports;

import android.os.Handler;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.dicommsupport.DiCommPort;

import java.util.Map;

public class DiCommFirmwarePort extends DiCommPort {

    public static final String FIRMWARE = "firmware";

    public static class Key {
        public static final String STATE = "state";
        public static final String MAC_CHUNK_SIZE = "maxchunksize";
        public static final String UPGRADE = "upgrade";
        public static final String CAN_UPGRADE = "canupgrade";

        public static final String STATUS_MESSAGE = "statusmsg";
        public static final String DATA = "data";
        public static final String PROGRESS = "progress";
        public static final String SIZE = "size";
    }

    public enum State {
        Idle("idle"),
        Preparing("preparing"),
        Downloading("downloading"),
        Checking("checking"),
        Ready("ready"),
        Programming("programming"),
        Canceling("canceling"),
        Error("error"),
        Unknown("unknown");

        private String stateName;

        State(String stateName) {
            this.stateName = stateName;
        }

        public static State fromString(String stateString) {
            for (State state : State.values()) {
                if (state.stateName.equalsIgnoreCase(stateString)) {
                    return state;
                }
            }

            return Unknown;
        }

        public String getName() {
            return stateName;
        }
    }

    public enum Command {
        Downloading("downloading"),
        DeployGo("go"),
        Cancel("cancel");

        private String commandName;

        Command(String commandName) {
            this.commandName = commandName;
        }

        public final String getName() {
            return commandName;
        }
    }

    public DiCommFirmwarePort(Handler internalHandler) {
        super(FIRMWARE, internalHandler);
    }

    public int getMaxChunkSize() {
        Map<String, Object> properties = getProperties();

        Object size = properties.get(Key.MAC_CHUNK_SIZE);
        if (size instanceof Integer) {
            Integer maxChunkSizeInBase64 = (Integer) size;
            return (int) Math.floor(maxChunkSizeInBase64 * 0.75);
        }

        return Integer.MAX_VALUE;
    }

    public State getState() {
        String stringState = getString(Key.STATE);

        return State.fromString(stringState);
    }

    public String getStatusMessage() {
        return getString(Key.STATUS_MESSAGE);
    }

    public String getUploadedUpgradeVersion() {
        return getString(Key.UPGRADE);
    }

    public boolean getCanUpgrade() {
        String canUpgrade = getString(Key.CAN_UPGRADE);
        return Boolean.valueOf(canUpgrade);
    }

    @Nullable
    private String getString(String key) {
        Map<String, Object> properties = getProperties();

        Object state = properties.get(key);
        if (state instanceof String) {
            return (String) state;
        }
        return null;
    }
}
