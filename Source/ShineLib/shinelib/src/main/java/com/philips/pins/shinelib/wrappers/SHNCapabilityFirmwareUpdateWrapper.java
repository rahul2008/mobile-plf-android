/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;

public class SHNCapabilityFirmwareUpdateWrapper implements SHNCapabilityFirmwareUpdate, SHNCapabilityFirmwareUpdate.SHNCapabilityFirmwareUpdateListener {
    private final SHNCapabilityFirmwareUpdate wrappedCapability;
    private final Handler internalHandler;
    private final Handler userHandler;
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;

    public SHNCapabilityFirmwareUpdateWrapper(SHNCapabilityFirmwareUpdate capability, Handler internalHandler, Handler userHandler) {
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        wrappedCapability = capability;
        wrappedCapability.setSHNCapabilityFirmwareUpdateListener(this);
    }

    @Override
    public boolean supportsUploadWithoutDeploy() {
        return wrappedCapability.supportsUploadWithoutDeploy();
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedCapability.uploadFirmware(firmwareData);
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
        this.shnCapabilityFirmwareUpdateListener = shnCapabilityFirmwareUpdateListener;
    }

    @Override
    public SHNFirmwareUpdateState getState() {
        return wrappedCapability.getState();
    }

    @Override
    public void onStateChanged(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onStateChanged(SHNCapabilityFirmwareUpdateWrapper.this);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final float progress) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onProgressUpdate(SHNCapabilityFirmwareUpdateWrapper.this, progress);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onUploadFailed(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onUploadFinished(SHNCapabilityFirmwareUpdateWrapper.this);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onDeployFailed(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityFirmwareUpdateListener != null) {
                    shnCapabilityFirmwareUpdateListener.onDeployFinished(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
                }
            }
        };
        userHandler.post(callback);
    }
}
