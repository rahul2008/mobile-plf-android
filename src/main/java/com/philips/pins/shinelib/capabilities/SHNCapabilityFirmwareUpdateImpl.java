package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNCapabilityFirmwareUpdateImpl implements SHNCapabilityFirmwareUpdate {

    private SHNFirmwareUpdateState shnFirmwareUpdateState = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;

    @Override
    public void uploadFirmware(byte[] firmwareData) { throw new UnsupportedOperationException(); }
    @Override
    public void abortFirmwareUpload()  { throw new UnsupportedOperationException(); }
    @Override
    public void deployFirmware() { throw new UnsupportedOperationException(); }
    @Override
    public void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) { throw new UnsupportedOperationException(); }
}
