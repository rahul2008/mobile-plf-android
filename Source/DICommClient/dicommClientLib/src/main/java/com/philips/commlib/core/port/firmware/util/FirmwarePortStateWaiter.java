/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirmwarePortStateWaiter {

    @NonNull
    private final FirmwarePort firmwarePort;
    private final ExecutorService executor;

    private class StateWaitTask implements Callable<FirmwarePortState> {

        private FirmwarePortState initialState;
        private FirmwarePortState portState;

        private final long timeoutMillis;
        private CountDownLatch latch;

        private final DICommPortListener<FirmwarePort> firmwarePortListener = new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort port) {
                final FirmwarePortProperties firmwarePortProperties = port.getPortProperties();
                if (firmwarePortProperties == null) {
                    return;
                }
                portState = firmwarePortProperties.getState();
                DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "onPortUpdate - initialState [%s], portState [%s]", initialState.toString(), portState));

                if (portState != initialState) {
                    latch.countDown();
                }
            }

            @Override
            public void onPortError(FirmwarePort port, Error error, String errorData) {
                DICommLog.e(DICommLog.FIRMWAREPORT, String.format(Locale.US, "onPortError - error [%s], message [%s], state [%s]", error.toString(), errorData, portState.toString()));
                portState = null;
                latch.countDown();
            }
        };

        StateWaitTask(FirmwarePortState initialState, long timeoutMillis) {
            this.initialState = initialState;
            this.timeoutMillis = timeoutMillis;
            this.latch = createCountDownLatch();
        }

        @Override
        public FirmwarePortState call() throws Exception {
            DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "StateWaitTask - start, state [%s]", this.initialState));

            firmwarePort.addPortListener(firmwarePortListener);
            firmwarePort.subscribe();

            try {
                if (latch.await(this.timeoutMillis, TimeUnit.MILLISECONDS)) {
                    if (this.portState == null) {
                        throw new IllegalStateException("Error obtaining port state.");
                    }
                    return this.portState;
                } else {
                    throw new TimeoutException();
                }
            } catch (InterruptedException e) {
                throw e;
            } finally {
                DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "StateWaitTask - end, state [%s]", this.portState));

                firmwarePort.unsubscribe();
                firmwarePort.removePortListener(firmwarePortListener);
            }
        }
    }

    public FirmwarePortStateWaiter(ExecutorService executor, FirmwarePort firmwarePort) {
        this.executor = executor;
        this.firmwarePort = firmwarePort;
    }

    /**
     * Wait for new state.
     *
     * @param initialState  the initial state
     * @param timeoutMillis the timeout in milliseconds
     * @return the new state
     * @throws StateWaitException if an error occurred while waiting for the new state
     */
    @NonNull
    public FirmwarePortState waitForNextState(FirmwarePortState initialState, long timeoutMillis) throws StateWaitException {
        FirmwarePortProperties firmwarePortProperties;
        firmwarePortProperties = this.firmwarePort.getPortProperties();

        if (firmwarePortProperties != null) {
            final FirmwarePortState currentState = firmwarePortProperties.getState();
            DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "requested to waitForNextState - from [%s] to [%s]", initialState.toString(), currentState.toString()));

            if (currentState != initialState) {
                return currentState;
            }
        }

        final StateWaitTask stateWaitTask = new StateWaitTask(initialState, timeoutMillis);

        try {
            final FirmwarePortState currentState = executor.submit(stateWaitTask).get();
            DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "done waitForNextState - from [%s] to [%s]", initialState.toString(), currentState.toString()));
            return currentState;
        } catch (ExecutionException | InterruptedException e) {
            DICommLog.e(DICommLog.FIRMWAREPORT, "Exception caught while waiting for next state: " + e.getMessage());
            throw new StateWaitException(e);
        }
    }

    @NonNull
    @VisibleForTesting
    CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }
}
