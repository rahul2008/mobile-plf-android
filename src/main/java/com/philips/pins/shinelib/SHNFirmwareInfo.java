package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNFirmwareInfo {

    public enum SHNFirmwareState {
        Idle,
        Uploading,
        ReadyToDeploy,
        InvalidImage
    }

    private String version;
    private String deviceCloudComponentName;
    private String deviceCloudComponentVersion;
    private SHNFirmwareState state;

    public String getVersion() {
        return version;
    }

    public String getDeviceCloudComponentName() {
        return deviceCloudComponentName;
    }

    public String getDeviceCloudComponentVersion() {
        return deviceCloudComponentVersion;
    }

    public SHNFirmwareState getState() {
        return state;
    }
}
