/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.PROGRESS;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.UNKNOWN;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.fromString;
import static java.lang.Math.min;

public class FirmwareUploader {

    private final FirmwarePort firmwarePort;
    private final CommunicationStrategy communicationStrategy;
    private final FirmwareUpdatePushLocal operation;
    private final byte[] firmwareData;

    private IOException error;
    private CountDownLatch latch;
    private int maxChunkSize;

    private int progress;

    public FirmwareUploader(@NonNull final FirmwarePort firmwarePort, @NonNull final CommunicationStrategy communicationStrategy, final FirmwareUpdatePushLocal firmwareUpdatePushLocal, final byte[] firmwareData) {
        this.firmwarePort = firmwarePort;
        this.communicationStrategy = communicationStrategy;
        this.operation = firmwareUpdatePushLocal;
        this.firmwareData = firmwareData;
    }

    public void upload() throws IOException {
        maxChunkSize = this.firmwarePort.getPortProperties().getMaxChunkSize();
        if (maxChunkSize <= 0) {
            throw new IOException("Max chunk size is invalid.");
        }

        if (latch != null) {
            throw new IOException("Upload already in progress.");
        }

        latch = createCountDownLatch();
        uploadNextChunk(0);
        try {
            latch.await();
            if (error != null) {
                throw error;
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        } finally {
            latch = null;
            error = null;
        }
    }

    @VisibleForTesting
    @NonNull
    CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }

    private void uploadNextChunk(int offset) {
        Map<String, Object> properties = new HashMap<>();
        int nextChunkSize = min(maxChunkSize, this.firmwareData.length - offset);
        properties.put(FirmwarePortProperties.FirmwarePortKey.DATA.toString(), Arrays.copyOfRange(firmwareData, offset, offset + nextChunkSize));

        communicationStrategy.putProperties(properties, firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                HashMap<String, Object> dataMap = new HashMap<>();

                try {
                    dataMap = GsonProvider.get().fromJson(data, dataMap.getClass());
                } catch (JsonSyntaxException e) {
                    error = new IOException(e);
                    latch.countDown();
                    return;
                }

                FirmwarePortProperties.FirmwarePortState firmwarePortState = fromString((String) dataMap.get(STATE.toString()));
                Double progress = (Double) dataMap.get(PROGRESS.toString());

                if (firmwarePortState == null || progress == null) {
                    error = new IOException("Invalid data received.");
                    latch.countDown();
                    return;
                }

                int progressPercentage = (int) (progress / firmwareData.length * 100);
                if (FirmwareUploader.this.progress != progressPercentage) {
                    FirmwareUploader.this.progress = progressPercentage;
                    operation.onDownloadProgress(progressPercentage);
                }

                if (firmwarePortState == DOWNLOADING || firmwarePortState == UNKNOWN) {
                    uploadNextChunk(progress.intValue());
                    return;
                }
                latch.countDown();
            }

            @Override
            public void onError(final Error error, final String errorData) {
                FirmwareUploader.this.error = new IOException(error.getErrorMessage());
                latch.countDown();
            }
        });
    }
}
