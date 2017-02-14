/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware.util;

import android.os.SystemClock;
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

    private static final String TAG = "FirmwarePortStateWaiter";

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
        FirmwarePortProperties firmwarePortProperties;
        firmwarePortProperties = this.firmwarePort.getPortProperties();

        while (firmwarePortProperties == null) {
            firmwarePortProperties = this.firmwarePort.getPortProperties();
            SystemClock.sleep(100);

            // FIXME do this in a timer task
        }

        final FirmwarePortState currentState = firmwarePortProperties.getState();
        DICommLog.d(DICommLog.FIRMWAREPORT, String.format(Locale.US, "waitForNextState - initial state [%s], current state [%s]", initialState.toString(), currentState.toString()));

        if (currentState != initialState) {
            return initialState;
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
