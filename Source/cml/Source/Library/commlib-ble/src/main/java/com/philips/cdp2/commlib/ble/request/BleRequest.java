/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp.dicommclient.request.Error.NOT_UNDERSTOOD;
import static com.philips.cdp.dicommclient.request.Error.PROTOCOL_VIOLATION;
import static com.philips.cdp.dicommclient.request.Error.UNKNOWN;
import static com.philips.cdp.dicommclient.util.DICommLog.BLEREQUEST;
import static com.philips.cdp2.commlib.ble.error.BleErrorMap.getErrorByStatusCode;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.COMPLETED;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.CREATED;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.EXECUTING;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.FINALIZED;
import static com.philips.cdp2.commlib.ble.request.BleRequest.State.STARTED;
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
    static final int MAX_PAYLOAD_LENGTH = (1 << 16) - 1;

    private static final long REQUEST_TIMEOUT_MS = 30000L;

    private static final long CONNECTION_TIMEOUT = 30000L;

    enum State {
        CREATED,
        STARTED,
        EXECUTING,
        COMPLETED,
        FINALIZED
    }

    @NonNull
    final ResponseHandler responseHandler;

    @NonNull
    final String productId;

    @NonNull
    final String portName;

    @NonNull
    private final Handler handlerToPostResponseOnto;

    @NonNull
    private final AtomicBoolean disconnectAfterRequest;

    @NonNull
    private final SHNDevice bleDevice;

    private CapabilityDiComm capability;

    @NonNull
    @VisibleForTesting
    CountDownLatch inProgressLatch = new CountDownLatch(1);

    @NonNull
    private State state = CREATED;

    @NonNull
    private final Object stateLock = new Object();

    @VisibleForTesting
    DiCommByteStreamReader.DiCommMessageListener dicommMessageListener = new DiCommByteStreamReader.DiCommMessageListener() {
        @Override
        public void onMessage(DiCommMessage diCommMessage) {
            try {
                final DiCommResponse res = new DiCommResponse(diCommMessage);
                processDiCommResponse(res);
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
    DiCommByteStreamReader diCommByteStreamReader = new DiCommByteStreamReader(dicommMessageListener);

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

    @Nullable
    private Timer timer;

    @VisibleForTesting
    void processDiCommResponse(final DiCommResponse res) {
        final StatusCode statusCode = res.getStatus();

        if (statusCode == NoError) {
            onSuccess(res.getPropertiesAsString());
        } else {
            onError(getErrorByStatusCode(statusCode), res.getPropertiesAsString());
        }
    }

    /**
     * Instantiates a new BleRequest.
     *
     * @param bleDevice              the device to send requests to
     * @param portName               the port name
     * @param productId              the product id
     * @param responseHandler        the response handler
     * @param disconnectAfterRequest indicates if the request should disconnect from the device after communicating
     */
    BleRequest(@NonNull SHNDevice bleDevice,
               @NonNull String portName,
               int productId,
               @NonNull ResponseHandler responseHandler,
               @NonNull Handler handlerToPostResponseOnto,
               @NonNull AtomicBoolean disconnectAfterRequest) {
        this.bleDevice = bleDevice;
        this.responseHandler = responseHandler;
        this.portName = portName;
        this.productId = Integer.toString(productId);
        this.handlerToPostResponseOnto = handlerToPostResponseOnto;
        this.disconnectAfterRequest = disconnectAfterRequest;
    }

    @Override
    public void run() {
        if (setStateIfStateIs(STARTED, CREATED)) {
            addTimeoutToRequest();
            execute();

            try {
                inProgressLatch.await();
            } catch (InterruptedException ignored) {
                onError(UNKNOWN, "Thread interrupted");
            }
            cleanup();
        }
    }

    private void addTimeoutToRequest() {
        DICommLog.d(BLEREQUEST, "adding timeout (" + REQUEST_TIMEOUT_MS + "ms) to request (" + this.hashCode() + ")");
        timer = createTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DICommLog.d(BLEREQUEST, "request (" + BleRequest.this.hashCode() + ") timed out");
                BleRequest.this.cancel("Timeout occurred.");
            }
        }, REQUEST_TIMEOUT_MS);
    }

    @NonNull
    protected Timer createTimer() {
        return new Timer();
    }

    private void execute() {
        if (portName.isEmpty()) {
            onError(Error.NO_SUCH_PORT, "Port name is empty.");
            return;
        }

        bleDevice.registerSHNDeviceListener(bleDeviceListener);
        if (bleDevice.getState() == Connected) {
            onConnected();
        } else {
            connectToDevice();
        }
    }

    @VisibleForTesting
    SHNDevice.SHNDeviceListener bleDeviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(final SHNDevice shnDevice) {
            if (shnDevice.getState() == Connected) {
                onConnected();
            } else if (shnDevice.getState() == Disconnected) {
                if (stateIs(COMPLETED)) {
                    completeRequest();
                } else {
                    onError(Error.REQUEST_FAILED, "device disconnected");
                }
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
        bleDevice.connect(CONNECTION_TIMEOUT);
    }

    private void onConnected() {
        if (setStateIfStateIs(EXECUTING, STARTED)) {
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

    private boolean stateIs(State state) {
        synchronized (stateLock) {
            return state == this.state;
        }
    }

    private boolean setStateIfStateIs(State newState, State... currentStates) {
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

    private void onError(final Error error, final String errorMessage) {
        if (setStateIfStateIs(COMPLETED, EXECUTING, STARTED, CREATED)) {
            handlerToPostResponseOnto.post(new Runnable() {
                @Override
                public void run() {
                    responseHandler.onError(error, errorMessage);
                }
            });
            finishRequest();
        }
    }

    private void onSuccess(final String data) {
        if (setStateIfStateIs(COMPLETED, EXECUTING)) {
            handlerToPostResponseOnto.post(new Runnable() {
                @Override
                public void run() {
                    responseHandler.onSuccess(data);
                }
            });
            finishRequest();
        }
    }

    private void finishRequest() {
        if (bleDevice.getState() != Disconnected && disconnectAfterRequest.get()) {
            bleDevice.disconnect();
        } else {
            completeRequest();
        }
    }

    private void completeRequest() {
        if (setStateIfStateIs(FINALIZED, COMPLETED)) {
            inProgressLatch.countDown();
        }
    }

    private void cleanup() {
        bleDevice.unregisterSHNDeviceListener(bleDeviceListener);

        if (capability != null) {
            capability.removeDataListener(resultListener);
            capability = null;
        }

        if (timer != null) {
            timer.cancel();
        }
    }

    @NonNull
    public String getPortName() {
        return portName;
    }

    @NonNull
    public String getProductId() {
        return productId;
    }

    @NonNull
    public SHNDevice getBleDevice() {
        return bleDevice;
    }
}
