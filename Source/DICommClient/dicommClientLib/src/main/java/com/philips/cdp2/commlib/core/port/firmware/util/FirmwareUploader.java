/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.cdp2.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.PROGRESS;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.UNKNOWN;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.fromString;
import static java.lang.Math.min;

public class FirmwareUploader {

    private final FirmwarePort firmwarePort;
    private final CommunicationStrategy communicationStrategy;
    private final byte[] firmwareData;
    @NonNull
    private final UploadListener listener;

    private int maxChunkSize;

    private int progress;

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
    }

    public void upload() {
        maxChunkSize = this.firmwarePort.getPortProperties().getMaxChunkSize();
        if (maxChunkSize <= 0) {
            Throwable t = new IOException("Max chunk size is invalid.");
            listener.onError(t.getMessage(), t);
        }

        progress = 0;
        listener.onProgress(progress);
        uploadNextChunk(progress);
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
                    listener.onError(e.getMessage(), e);
                    return;
                }

                FirmwarePortProperties.FirmwarePortState firmwarePortState = fromString((String) dataMap.get(STATE.toString()));
                Double progress = (Double) dataMap.get(PROGRESS.toString());

                if (firmwarePortState == null || progress == null) {
                    Throwable t = new IOException("Invalid data received.");
                    listener.onError(t.getMessage(), t);
                    return;
                }

                int progressPercentage = (int) (progress / firmwareData.length * 100);
                if (FirmwareUploader.this.progress != progressPercentage) {
                    FirmwareUploader.this.progress = progressPercentage;
                    listener.onProgress(progressPercentage);
                }

                if (firmwarePortState == DOWNLOADING || firmwarePortState == UNKNOWN) {
                    uploadNextChunk(progress.intValue());
                    return;
                }

                listener.onSuccess();
            }

            @Override
            public void onError(final Error error, final String errorData) {
                Throwable t = new IOException(error.getErrorMessage());
                listener.onError(t.getMessage(), t);
            }
        });
    }
}
