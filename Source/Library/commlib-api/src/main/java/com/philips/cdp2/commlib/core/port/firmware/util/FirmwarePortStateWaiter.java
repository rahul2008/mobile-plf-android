/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.philips.cdp.dicommclient.port.DICommPort.SUBSCRIPTION_TTL;

public class FirmwarePortStateWaiter {

    @NonNull
    private final FirmwarePort firmwarePort;

    @NonNull
    private final WaiterListener listener;

    @NonNull
    private final CommunicationStrategy communicationStrategy;

    @NonNull
    private FirmwarePortState initialState;

    private TimerTask timeoutTask;

    private final ResponseHandler emptyResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(String data) {
            // Ignored
        }

        @Override
        public void onError(Error error, String errorData) {
            // Ignored
        }
    };

    public interface WaiterListener {
        void onNewState(FirmwarePortState state);

        void onError(String message);
    }

    private final SubscriptionEventListener firmwareSubscriptionEventListener = new SubscriptionEventListener() {
        @Override
        public void onSubscriptionEventReceived(String portName, String data) {
            firmwarePort.processResponse(data);

            final FirmwarePortProperties firmwarePortProperties = firmwarePort.getPortProperties();
            if (firmwarePortProperties == null) {
                return;
            }

            final FirmwarePortState newState = firmwarePortProperties.getState();
            if (initialState == newState) {
                return;
            }

            DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "onPortUpdate - initialState [%s], newState [%s]", initialState.toString(), newState.toString()));
            communicationStrategy.removeSubscriptionEventListener(this);
            communicationStrategy.unsubscribe(firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), emptyResponseHandler);

            if (timeoutTask != null) {
                timeoutTask.cancel();
            }
            listener.onNewState(newState);
        }

        @Override
        public void onSubscriptionEventDecryptionFailed(String portName) {
            communicationStrategy.removeSubscriptionEventListener(this);
            communicationStrategy.unsubscribe(firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), emptyResponseHandler);

            final String message = String.format(Locale.US, "Subscription event decryption failed on port [%s]", portName);

            DICommLog.e(DICommLog.FIRMWAREPORT, message);
            listener.onError(message);
        }
    };

    public FirmwarePortStateWaiter(@NonNull FirmwarePort firmwarePort, @NonNull CommunicationStrategy communicationStrategy, @NonNull FirmwarePortState initialState, @NonNull WaiterListener listener) {
        this.firmwarePort = firmwarePort;
        this.communicationStrategy = communicationStrategy;
        this.initialState = initialState;
        this.listener = listener;
    }

    public void waitForNextState(long stateTransitionTimeoutMillis) {
        if (stateTransitionTimeoutMillis <= 0) {
            throw new IllegalArgumentException("Timeout value is invalid, must be a non-zero positive integer.");
        }
        FirmwarePortProperties firmwarePortProperties;
        firmwarePortProperties = this.firmwarePort.getPortProperties();

        if (firmwarePortProperties != null) {
            final FirmwarePortState currentState = firmwarePortProperties.getState();
            DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "requested to waitForNextState - initiated in [%s], currently in [%s]", initialState.toString(), currentState.toString()));

            if (currentState != initialState) {
                listener.onNewState(currentState);
                return;
            }
        }

        communicationStrategy.addSubscriptionEventListener(firmwareSubscriptionEventListener);
        communicationStrategy.subscribe(firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), SUBSCRIPTION_TTL, emptyResponseHandler);

        timeoutTask = new TimerTask() {
            @Override
            public void run() {
                communicationStrategy.removeSubscriptionEventListener(firmwareSubscriptionEventListener);
                communicationStrategy.unsubscribe(firmwarePort.getDICommPortName(), firmwarePort.getDICommProductId(), emptyResponseHandler);
                listener.onError("Timeout while waiting for next state.");
            }
        };
        scheduleTask(timeoutTask, stateTransitionTimeoutMillis);
    }

    @VisibleForTesting
    void scheduleTask(final TimerTask timeoutTask, long timeoutMillis) {
        new Timer().schedule(timeoutTask, timeoutMillis);
    }
}
