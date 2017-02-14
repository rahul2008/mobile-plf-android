/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

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
                portState = port.getPortProperties().getState();
                if (portState != initialState) {
                    latch.countDown();
                }
            }

            @Override
            public void onPortError(FirmwarePort port, Error error, String errorData) {
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
            firmwarePort.addPortListener(firmwarePortListener);
            firmwarePort.subscribe();

            try {
                if (latch.await(this.timeoutMillis, TimeUnit.MILLISECONDS)) {
                    return this.portState;
                } else {
                    throw new TimeoutException();
                }
            } catch (InterruptedException e) {
                throw e;
            } finally {
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
     * @return the new state, or null if new state could not be obtained or a timeout occurred.
     * @throws StateWaitException if an error occurred while waiting for the new state
     */
    @NonNull
    public FirmwarePortState waitForNextState(FirmwarePortState initialState, long timeoutMillis) throws StateWaitException {
        final FirmwarePortState currentState = this.firmwarePort.getPortProperties().getState();

        if (initialState != currentState) {
            return currentState;
        }

        final StateWaitTask stateWaitTask = new StateWaitTask(initialState, timeoutMillis);

        try {
            return executor.submit(stateWaitTask).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new StateWaitException(e);
        }
    }

    @NonNull
    @VisibleForTesting
    CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }
}
