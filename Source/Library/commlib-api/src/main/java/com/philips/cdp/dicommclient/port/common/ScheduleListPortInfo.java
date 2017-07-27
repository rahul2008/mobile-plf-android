/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.Map;

public class ScheduleListPortInfo implements Comparable<ScheduleListPortInfo>, PortProperties {

    private String time;
    private String days;
    private String port;
    private ScheduleListPortInfoMode mode;
    private Map<String, Object> command;
    private int scheduleNumber;
    private String name;
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
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

    /**
     * According to the specification mode property does not exist. Instead command should be used.
     */
    @Deprecated
    public String getMode() {
        return mode.getValue();
    }

    /**
     * According to the specification mode property does not exist. Instead command should be used.
     */
    @Deprecated
    public void setMode(String mode) {
        this.mode = new ScheduleListPortInfoMode(mode);
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(final Map<String, Object> command) {
        this.command = command;
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

    public static class ScheduleListPortInfoMode {

        @SerializedName("om")
        private String value;

        public ScheduleListPortInfoMode(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class ScheduleListPortInfoFromCpp {

        private int status;
        private ScheduleListPortInfo data;

        public int getStatus() {
            return status;
        }

        public ScheduleListPortInfo getData() {
            return data;
        }
    }
}
