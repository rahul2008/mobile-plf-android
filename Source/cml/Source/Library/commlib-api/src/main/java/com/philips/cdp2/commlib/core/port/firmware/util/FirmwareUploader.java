/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;
import com.philips.cdp2.commlib.core.util.GsonProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.PROGRESS;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.fromString;
import static java.lang.Math.min;

/**
 * This type can be used to upload a binary firmware image to the {@link FirmwarePort} of an {@link Appliance}.
 * The uploading is performed in chunks, of which the size is determined by the 'maxchunksize' property that
 * is set, see {@link FirmwarePortProperties#getMaxChunkSize()}.
 */
public class FirmwareUploader {

    private final FirmwarePort firmwarePort;
    private final CommunicationStrategy communicationStrategy;
    private final byte[] firmwareData;
    @NonNull
    private final UploadListener listener;
    private final ExecutorService executor;

    private int maxChunkSize;

    private int progress;
    private Future<Void> uploadTask;

    public interface UploadListener {
        void onSuccess();

        void onError(String message, Throwable t);

        void onProgress(int progress);
    }

    public FirmwareUploader(@NonNull final FirmwarePort firmwarePort, @NonNull final CommunicationStrategy communicationStrategy, final byte[] firmwareData, @NonNull UploadListener listener) {
        this.firmwarePort = firmwarePort;
        this.communicationStrategy = communicationStrategy;
        this.firmwareData = firmwareData;
        this.listener = listener;
        this.executor = createExecutor();
    }

    @NonNull
    @VisibleForTesting
    ExecutorService createExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public void start() {
        uploadTask = executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                maxChunkSize = firmwarePort.getPortProperties().getMaxChunkSize();
                if (maxChunkSize <= 0) {
                    Throwable t = new IOException("Max chunk size is invalid.");
                    listener.onError(t.getMessage(), t);
                }

                progress = 0;
                listener.onProgress(progress);
                uploadNextChunk(progress);

                return null;
            }
        });
    }

    public void stop() {
        if (uploadTask != null) {
            uploadTask.cancel(true);
        }
        executor.shutdownNow();
    }

    private void uploadNextChunk(int offset) {
        Map<String, Object> properties = new HashMap<>();
        int nextChunkSize = min(maxChunkSize, this.firmwareData.length - offset);
        properties.put(FirmwarePortProperties.FirmwarePortKey.DATA.toString(), Arrays.copyOfRange(firmwareData, offset, offset + nextChunkSize));

        communicationStrategy.putProperties(properties, firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(final String data) {
                Map dataMap = new HashMap<>();

                try {
                    dataMap = GsonProvider.get().fromJson(data, dataMap.getClass());
                } catch (JsonSyntaxException e) {
                    listener.onError(e.getMessage(), e);
                    return;
                }

                FirmwarePortState firmwarePortState = fromString((String) dataMap.get(STATE.toString()));
                Double progress = (Double) dataMap.get(PROGRESS.toString());

                if (firmwarePortState == null || progress == null) {
                    Throwable t = new IOException("Invalid data received.");
                    listener.onError(t.getMessage(), t);
                    return;
                }

                switch (firmwarePortState) {
                    case DOWNLOADING:
                    case UNKNOWN:
                        int progressPercentage = (int) (progress / firmwareData.length * 100);
                        if (FirmwareUploader.this.progress != progressPercentage) {
                            FirmwareUploader.this.progress = progressPercentage;
                            listener.onProgress(progressPercentage);
                        }
                        uploadNextChunk(progress.intValue());
                        break;
                    case CHECKING:
                    case READY:
                        listener.onSuccess();
                        break;
                }
            }

            @Override
            public void onError(final Error error, final String errorData) {
                Throwable t = new IOException(error.getErrorMessage());
                listener.onError(t.getMessage(), t);
            }
        });
    }
}
