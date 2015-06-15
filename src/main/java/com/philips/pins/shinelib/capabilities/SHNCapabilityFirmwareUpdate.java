package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNCapabilityFirmwareUpdate {
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

    private SHNFirmwareUpdateState shnFirmwareUpdateState = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;

    public void uploadFirmware(byte[] firmwareData) { throw new UnsupportedOperationException(); }
    public void abortFirmwareUpload()  { throw new UnsupportedOperationException(); }
    public void deployFirmware() { throw new UnsupportedOperationException(); }
    public void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) { throw new UnsupportedOperationException(); }
}
