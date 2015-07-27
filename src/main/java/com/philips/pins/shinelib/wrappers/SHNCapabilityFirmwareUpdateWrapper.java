package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataStreaming;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Created by code1_310170470 on 27/07/15.
 */
public class SHNCapabilityFirmwareUpdateWrapper implements SHNCapabilityFirmwareUpdate {
    private final SHNCapabilityFirmwareUpdate wrappedCapability;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityFirmwareUpdateWrapper(SHNCapabilityFirmwareUpdate capability, Handler internalHandler, Handler userHandler) {
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        wrappedCapability = capability;
    }

    @Override
    public boolean supportsUploadWithoutDeploy() {
        return wrappedCapability.supportsUploadWithoutDeploy();
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() { wrappedCapability.uploadFirmware(firmwareData);
            }
        });
    }

    @Override
    public void abortFirmwareUpload() {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedCapability.abortFirmwareUpload();
            }
        });
    }

    @Override
    public void deployFirmware() {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedCapability.deployFirmware();
            }
        });
    }

    @Override
    public void getUploadedFirmwareInfo(final SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedCapability.getUploadedFirmwareInfo(new SHNFirmwareInfoResultListener() {
                    @Override
                    public void onActionCompleted(final SHNFirmwareInfo value, final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnFirmwareInfoResultListener.onActionCompleted(value, result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setSHNCapabilityFirmwareUpdateListener(final SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedCapability.setSHNCapabilityFirmwareUpdateListener(shnCapabilityFirmwareUpdateListener);
            }
        });
    }
}
