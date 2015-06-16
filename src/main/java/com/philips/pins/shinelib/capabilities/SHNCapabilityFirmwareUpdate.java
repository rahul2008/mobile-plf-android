package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;

/**
 * Created by 310188215 on 16/06/15.
 */
public interface SHNCapabilityFirmwareUpdate {
    void uploadFirmware(byte[] firmwareData);

    void abortFirmwareUpload();

    void deployFirmware();

    void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener);

    public enum SHNFirmwareUpdateState {
        SHNFirmwareUpdateStateIdle,
        SHNFirmwareUpdateStatePreparing,
        SHNFirmwareUpdateStateUploading,
        SHNFirmwareUpdateStateVerifying,
        SHNFirmwareUpdateStateDeploying,
    }

    public interface SHNCapabilityFirmwareUpdateListener {
        void onStateChanged(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
        void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, float progress);
        void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);
        void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
        void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);
        void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
    }
}
