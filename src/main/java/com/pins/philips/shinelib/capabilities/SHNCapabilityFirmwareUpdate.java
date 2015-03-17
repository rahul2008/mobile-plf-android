package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNFirmwareInfoResultListener;
import com.pins.philips.shinelib.SHNResult;

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
        public void onStateChanged(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
        public void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, float progress);
        public void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);
        public void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
        public void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);
        public void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);
    }

    private SHNFirmwareUpdateState shnFirmwareUpdateState = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;

    public void uploadFirmware(byte[] firmwareData) { throw new UnsupportedOperationException(); }
    public void abortFirmwareUpload()  { throw new UnsupportedOperationException(); }
    public void deployFirmware() { throw new UnsupportedOperationException(); }
    public void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) { throw new UnsupportedOperationException(); }
}
