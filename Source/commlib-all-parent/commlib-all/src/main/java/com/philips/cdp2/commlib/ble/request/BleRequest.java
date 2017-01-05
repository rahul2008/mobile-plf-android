/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.request;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.ble.error.BleErrorMap;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import com.philips.pins.shinelib.dicommsupport.DiCommByteStreamReader;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;
import com.philips.pins.shinelib.dicommsupport.StatusCode;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidPayloadFormatException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidStatusCodeException;

import java.util.concurrent.CountDownLatch;

import static com.philips.cdp.dicommclient.request.Error.NOT_UNDERSTOOD;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNResult.SHNOk;
import static com.philips.pins.shinelib.dicommsupport.StatusCode.NoError;

/**
 * The type BleRequest.
 * <p>
 * This type is used by {@link BleCommunicationStrategy} to perform DiComm
 * requests to a BLE device. The request uses a {@link CountDownLatch} to block the thread on
 * which it is running until a call to either {@link ResponseHandler#onSuccess(String)} or
 * {@link ResponseHandler#onError(Error, String)} is made to ensure that all requests that are
 * dispatched in a queue are processed sequentially.
 */
public abstract class BleRequest implements Runnable {
    static final int MAX_PAYLOAD_LENGTH = (int) Math.pow(2, 16) - 1;

    @NonNull
    private final BleDeviceCache deviceCache;
    @NonNull
    private final String cppId;
    @NonNull
    final ResponseHandler responseHandler;
    @NonNull
    final String productId;
    @NonNull
    final String portName;

    private SHNDevice bleDevice;
    private CapabilityDiComm capability;
    private CountDownLatch inProgressLatch;

    private DiCommByteStreamReader.DiCommMessageListener dicommMessageListener = new DiCommByteStreamReader.DiCommMessageListener() {
        @Override
        public void onMessage(DiCommMessage diCommMessage) {
            try {
                final DiCommResponse res = new DiCommResponse(diCommMessage);
                processDicommResponse(res);
            } catch (IllegalArgumentException | InvalidMessageTerminationException | InvalidPayloadFormatException e) {
                responseHandler.onError(Error.PROTOCOL_VIOLATION, e.getMessage());
            } catch (InvalidStatusCodeException e) {
                responseHandler.onError(Error.UNKNOWN, e.getMessage());
            }
            inProgressLatch.countDown();
        }

        @Override
        public void onError(String message) {
            responseHandler.onError(Error.NO_REQUEST_DATA, message);
            inProgressLatch.countDown();
        }
    };

    private final DiCommByteStreamReader diCommByteStreamReader = new DiCommByteStreamReader(dicommMessageListener);

    @VisibleForTesting
    synchronized void processDicommResponse(final DiCommResponse res) {
        final StatusCode statusCode = res.getStatus();

        if (statusCode == NoError) {
            responseHandler.onSuccess(res.getPropertiesAsString());
        } else {
            final Error error = BleErrorMap.getErrorByStatusCode(statusCode);
            responseHandler.onError(error, res.getPropertiesAsString());
        }
    }

    private final ResultListener<SHNDataRaw> resultListener = new ResultListener<SHNDataRaw>() {
        @Override
        public void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
            BleRequest.this.onActionCompleted(shnDataRaw, shnResult);
        }
    };

    private synchronized void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
        if (isExecuting()) {
            if (shnResult == SHNOk) {
                diCommByteStreamReader.onBytes(shnDataRaw.getRawData());
            } else {
                responseHandler.onError(NOT_UNDERSTOOD, shnResult.name());
                inProgressLatch.countDown();
            }
        }
    }

    private boolean isExecuting() {
        return inProgressLatch != null;
    }

    /**
     * Instantiates a new BleRequest.
     *
     * @param deviceCache     the device cache
     * @param cppId           the cppId of the BleDevice
     * @param portName        the port name
     * @param productId       the product id
     * @param responseHandler the response handler
     */
    BleRequest(@NonNull BleDeviceCache deviceCache,
               @NonNull String cppId,
               @NonNull String portName,
               int productId,
               @NonNull ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        this.deviceCache = deviceCache;
        this.cppId = cppId;
        this.portName = portName;
        this.productId = Integer.toString(productId);
    }

    @VisibleForTesting
    synchronized void execute() {
        if (portName.isEmpty()) {
            responseHandler.onError(Error.NO_SUCH_PORT, "Port name is empty.");
            return;
        }

        bleDevice = deviceCache.getDeviceMap().get(cppId);

        if (bleDevice == null) {
            responseHandler.onError(Error.NOT_AVAILABLE, "Communication is not available");
            return;
        }

        inProgressLatch = createLatch();

        if (bleDevice.getState() == Connected) {
            onConnected();
        } else {
            connectToDevice();
        }
    }

    private SHNDevice.SHNDeviceListener bleDeviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(final SHNDevice shnDevice) {
            if (Connected.equals(shnDevice.getState())) {
                onConnected();
            }
        }

        @Override
        public void onFailedToConnect(final SHNDevice shnDevice, final SHNResult shnResult) {
            responseHandler.onError(Error.NOT_AVAILABLE, "Communication is not available");
            inProgressLatch.countDown();
        }

        @Override
        public void onReadRSSI(final int i) {
            // Don't care
        }
    };

    private synchronized void connectToDevice() {
        bleDevice.registerSHNDeviceListener(bleDeviceListener);
        bleDevice.connect();
    }

    private synchronized void onConnected() {
        capability = (CapabilityDiComm) bleDevice.getCapabilityForType(SHNCapabilityType.DI_COMM);
        if (capability == null) {
            responseHandler.onError(Error.NOT_AVAILABLE, "Communication is not available");
            return;
        }

        capability.addDataListener(resultListener);

        execute(capability);
    }

    protected abstract void execute(CapabilityDiComm capability);

    /**
     * Cancel.
     * <p>
     * This cancels the current request and notifies the {@link ResponseHandler} for this request.
     *
     * @param reason the reason
     */
    public synchronized void cancel(String reason) {
        if (isExecuting()) {
            responseHandler.onError(Error.TIMED_OUT, reason);
            inProgressLatch.countDown();
        }
    }

    @Override
    public void run() {
        execute();

        if (isExecuting()) {
            try {
                inProgressLatch.await();
            } catch (InterruptedException ignored) {
            }
            cleanup();
        }
    }

    @VisibleForTesting
    synchronized void cleanup() {
        if (bleDevice != null) {
            bleDevice = null;
            if (capability != null) {
                capability.removeDataListener(resultListener);
                capability = null;
            }
        }
        inProgressLatch = null;
    }

    @VisibleForTesting
    CountDownLatch createLatch() {
        return new CountDownLatch(1);
    }
}
