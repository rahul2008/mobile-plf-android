/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;

class FirmwarePortStateWaiter implements DICommPortListener<FirmwarePort> {
    private static final String TAG = "FirmwarePortStateWaiter";

    private FirmwarePortProperties.FirmwareState expectedState;

    interface Listener {
        void onStateUpdated(FirmwarePortProperties.FirmwareState state, Result shnResult);
    }

    enum Result {
        Ok,
        InvalidState
    }

    @NonNull
    private final FirmwarePort firmwarePort;
    @NonNull
    private final Handler internalHandler;

    @Nullable
    private Listener listener;

    public FirmwarePortStateWaiter(@NonNull FirmwarePort firmwarePort, @NonNull Handler internalHandler) {
        this.firmwarePort = firmwarePort;
        this.internalHandler = internalHandler;
    }

    public void waitUntilStateIsReached(@NonNull final FirmwarePortProperties.FirmwareState state, @NonNull final Listener listener) {
        if (firmwarePort.getPortProperties().getState() == state) {
            listener.onStateUpdated(state, Result.Ok);
        } else {
            DICommLog.d(TAG, "stateWaiter subscribe for State " + state);
            firmwarePort.subscribe();
            firmwarePort.addPortListener(this);

            this.listener = listener;
            this.expectedState = state;
        }
    }

    public void cancel() {
        DICommLog.d(TAG, "stateWaiter cancel ");

        listener = null;
        firmwarePort.unsubscribe();
        firmwarePort.removePortListener(this);
    }

    @Override
    public void onPortUpdate(FirmwarePort port) {
        FirmwarePortProperties.FirmwareState newState = port.getPortProperties().getState();

        if (port.getPortProperties().getState() != null) {
            if (newState == expectedState) {
                notifyListenerAndCancel(newState, Result.Ok);
            } else {
                if (!isTransientState(newState)) {
                    DICommLog.e(TAG, "was expecting state: " + expectedState + " but encountered state: " + newState);
                    notifyListenerAndCancel(newState, Result.InvalidState);
                }
            }
        }
    }

    @Override
    public void onPortError(FirmwarePort port, Error error, String errorData) {

    }

    private void notifyListenerAndCancel(final FirmwarePortProperties.FirmwareState newState, final Result shnErrorInvalidState) {
        if (listener != null) {
            internalHandler.postDelayed(new Runnable() {
                private Listener runnableListener = FirmwarePortStateWaiter.this.listener;

                @Override
                public void run() {
                    this.runnableListener.onStateUpdated(newState, shnErrorInvalidState);
                }
            }, 1);
        }

        cancel();
    }

    private boolean isTransientState(FirmwarePortProperties.FirmwareState newState) {
        switch (newState) {
            case PREPARING:
            case CHECKING:
            case PROGRAMMING:
            case CANCELING:
                return true;
            default:
                return false;
        }
    }
}
