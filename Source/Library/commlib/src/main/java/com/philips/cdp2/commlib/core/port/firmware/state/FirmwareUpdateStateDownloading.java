/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUploader;

public class FirmwareUpdateStateDownloading extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateDownloading(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @VisibleForTesting
    FirmwareUploader.UploadListener firmwareUploadListener = new FirmwareUploader.UploadListener() {
        @Override
        public void onSuccess() {
            firmwareUpdateOperation.onDownloadProgress(100);
            firmwareUpdateOperation.waitForNextState();
        }

        @Override
        public void onProgress(final int progress) {
            firmwareUpdateOperation.onDownloadProgress(progress);
        }

        @Override
        public void onError(final String message, final Throwable t) {
            FirmwareUpdateStateDownloading.this.onError(message);
        }
    };

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        firmwareUpdateOperation.uploadFirmware(firmwareUploadListener);
    }

    @Override
    public void onError(final String message) {
        super.onError("Could not upload firmware: " + message);
    }

    @Override
    public void cancel() {
        firmwareUpdateOperation.stopUploading();
        super.cancel();
    }
}
