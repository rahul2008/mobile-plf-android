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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.philips.cdp.dicommclient.request.Error.NOT_UNDERSTOOD;
import static com.philips.cdp.dicommclient.request.Error.PROTOCOL_VIOLATION;
import static com.philips.cdp.dicommclient.request.Error.UNKNOWN;
import static com.philips.cdp2.commlib.ble.error.BleErrorMap.getErrorByStatusCode;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.DISCONNECTING;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.EXECUTING;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.FINISHED;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.NOT_STARTED;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
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

    enum State {
        NOT_STARTED,
        EXECUTING,
        DISCONNECTING,
        FINISHED
    }

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

    @NonNull
    @VisibleForTesting
    CountDownLatch inProgressLatch = new CountDownLatch(1);

    @NonNull
    private State state = NOT_STARTED;

    @NonNull
    private final Object stateLock = new Object();

    private boolean stateIs(State state) {
        synchronized (stateLock) {
            return state == this.state;
        }
    }

    private boolean setIfStateIs(State newState, State... currentStates) {
        synchronized (stateLock) {
            List<State> currentStateList = Arrays.asList(currentStates);
            if (currentStateList.contains(state)) {
                state = newState;
                return true;
            } else {
                return false;
            }
        }
    }

    private final DiCommByteStreamReader.DiCommMessageListener dicommMessageListener = new DiCommByteStreamReader.DiCommMessageListener() {
        @Override
        public void onMessage(DiCommMessage diCommMessage) {
            try {
                final DiCommResponse res = new DiCommResponse(diCommMessage);
                processDicommResponse(res);
            } catch (IllegalArgumentException | InvalidMessageTerminationException | InvalidPayloadFormatException e) {
                BleRequest.this.onError(PROTOCOL_VIOLATION, e.getMessage());
            } catch (InvalidStatusCodeException e) {
                BleRequest.this.onError(UNKNOWN, e.getMessage());
            }
        }

        @Override
        public void onError(String message) {
            BleRequest.this.onError(Error.NO_REQUEST_DATA, message);
        }
    };

    @VisibleForTesting
    void processDicommResponse(final DiCommResponse res) {
        final StatusCode statusCode = res.getStatus();

        if (statusCode == NoError) {
            onSuccess(res.getPropertiesAsString());
        } else {
            onError(getErrorByStatusCode(statusCode), res.getPropertiesAsString());
        }
    }

    private final DiCommByteStreamReader diCommByteStreamReader = new DiCommByteStreamReader(dicommMessageListener);

    private final ResultListener<SHNDataRaw> resultListener = new ResultListener<SHNDataRaw>() {
        @Override
        public void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
            if (stateIs(EXECUTING)) {
                if (shnResult == SHNOk) {
                    diCommByteStreamReader.onBytes(shnDataRaw.getRawData());
                } else {
                    onError(NOT_UNDERSTOOD, shnResult.name());
                }
            }
        }
    };

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

    @Override
    public void run() {
        if (setIfStateIs(EXECUTING, NOT_STARTED)) {
            execute();

            try {
                inProgressLatch.await();
            } catch (InterruptedException ignored) {
                onError(UNKNOWN, "Thread interrupted");
            }

            cleanup();
        }
    }

    private void execute() {
        if (portName.isEmpty()) {
            onError(Error.NO_SUCH_PORT, "Port name is empty.");
            return;
        }

        bleDevice = deviceCache.getDeviceMap().get(cppId);

        if (bleDevice == null) {
            onError(Error.NOT_AVAILABLE, "Communication is not available");
            return;
        }

        bleDevice.registerSHNDeviceListener(bleDeviceListener);
        if (bleDevice.getState() == Connected) {
            onConnected();
        } else {
            connectToDevice();
        }
    }

    private SHNDevice.SHNDeviceListener bleDeviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(final SHNDevice shnDevice) {
            if (shnDevice.getState() == Connected) {
                onConnected();
            } else if (shnDevice.getState() == Disconnected) {
                onDisconnected();
            }
        }

        @Override
        public void onFailedToConnect(final SHNDevice shnDevice, final SHNResult shnResult) {
            onError(Error.NOT_AVAILABLE, "Communication is not available");
        }

        @Override
        public void onReadRSSI(final int i) {
            // Don't care
        }
    };

    private void connectToDevice() {
        bleDevice.connect();
    }

    private void onConnected() {
        if (stateIs(EXECUTING)) {
            capability = (CapabilityDiComm) bleDevice.getCapabilityForType(SHNCapabilityType.DI_COMM);
            if (capability == null) {
                onError(Error.NOT_AVAILABLE, "Communication is not available");
                return;
            }

            capability.addDataListener(resultListener);
            execute(capability);
        }
    }

    protected abstract void execute(CapabilityDiComm capability);

    /**
     * Cancel.
     * <p>
     * This cancels the current request and notifies the {@link ResponseHandler} for this request.
     *
     * @param reason the reason
     */
    public void cancel(String reason) {
        onError(Error.TIMED_OUT, reason);
    }

    private void onError(Error error, String errorMessage) {
        if (setIfStateIs(DISCONNECTING, EXECUTING, NOT_STARTED)) {
            responseHandler.onError(error, errorMessage);
            finishRequest();
        }
    }

    private void onSuccess(String data) {
        if (setIfStateIs(DISCONNECTING, EXECUTING)) {
            responseHandler.onSuccess(data);
            finishRequest();
        }
    }

    private void finishRequest() {
        if (bleDevice != null) {
            if (bleDevice.getState() == Disconnected) {
                onDisconnected();
            } else {
                bleDevice.disconnect();
            }
        } else {
            onDisconnected();
        }
    }

    private void onDisconnected() {
        if (setIfStateIs(FINISHED, DISCONNECTING)) {
            inProgressLatch.countDown();
        }
    }

    private void cleanup() {
        if (bleDevice != null) {
            bleDevice.registerSHNDeviceListener(null);
            bleDevice = null;
            if (capability != null) {
                capability.removeDataListener(resultListener);
                capability = null;
            }
        }
    }
}
