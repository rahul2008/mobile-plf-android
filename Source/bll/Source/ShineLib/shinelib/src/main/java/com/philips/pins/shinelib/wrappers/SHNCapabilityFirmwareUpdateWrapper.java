/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

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
        uploadFirmware(firmwareData, false);
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData, final boolean shouldResume) {
        internalHandler.post(() -> wrappedCapability.uploadFirmware(firmwareData, shouldResume));
    }

    @Override
    public void abortFirmwareUpload() {
        internalHandler.post(wrappedCapability::abortFirmwareUpload);
    }

    @Override
    public void deployFirmware() {
        internalHandler.post(wrappedCapability::deployFirmware);
    }

    @Override
    public void getUploadedFirmwareInfo(final SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) {
        Runnable command = () -> wrappedCapability.getUploadedFirmwareInfo((value, result) -> {
            Runnable resultRunnable = () -> shnFirmwareInfoResultListener.onActionCompleted(value, result);
            userHandler.post(resultRunnable);
        });
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
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onStateChanged(SHNCapabilityFirmwareUpdateWrapper.this);
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final float progress) {
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onProgressUpdate(SHNCapabilityFirmwareUpdateWrapper.this, progress);
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onUploadFailed(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onUploadFinished(SHNCapabilityFirmwareUpdateWrapper.this);
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onDeployFailed(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, final SHNResult shnResult) {
        Runnable callback = () -> {
            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onDeployFinished(SHNCapabilityFirmwareUpdateWrapper.this, shnResult);
            }
        };
        userHandler.post(callback);
    }
}
