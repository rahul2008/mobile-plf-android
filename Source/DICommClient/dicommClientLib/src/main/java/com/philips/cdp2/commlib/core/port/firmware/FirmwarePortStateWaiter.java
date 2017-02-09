/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class FirmwarePortStateWaiter {

    @NonNull
    private final FirmwarePort firmwarePort;
    private final ExecutorService executor;

    private class WaitTask implements Callable<Boolean> {

        private FirmwarePortState expectedState;
        private final long timeoutMillis;
        private CountDownLatch latch;

        private final DICommPortListener<FirmwarePort> firmwarePortListener = new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort port) {
                FirmwarePortState portState = port.getPortProperties().getState();
                if (portState == expectedState) {
                    latch.countDown();
                }
            }

            @Override
            public void onPortError(FirmwarePort port, Error error, String errorData) {
                // Ignored
            }
        };

        WaitTask(FirmwarePortState expectedState, long timeoutMillis) {
            this.expectedState = expectedState;
            this.timeoutMillis = timeoutMillis;
            this.latch = createCountDownLatch();
        }

        @Override
        public Boolean call() throws Exception {
            firmwarePort.addPortListener(firmwarePortListener);
            firmwarePort.subscribe();

            try {
                return latch.await(this.timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return false;
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
     * Await.
     *
     * @param expectedState the expected state
     * @param timeoutMillis the timeout in milliseconds
     * @return true, if the expected state was obtained or false
     * if the expected state wasn't obtained within the given timeout.
     */
    public boolean await(FirmwarePortState expectedState, long timeoutMillis) {
        if (expectedState == this.firmwarePort.getPortProperties().getState()) {
            return true;
        } else {
            WaitTask waitTask = new WaitTask(expectedState, timeoutMillis);
            Future<Boolean> future = executor.submit(waitTask);

            try {
                return future.get();
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
        }
    }

    @NonNull
    @VisibleForTesting
    CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }
}
