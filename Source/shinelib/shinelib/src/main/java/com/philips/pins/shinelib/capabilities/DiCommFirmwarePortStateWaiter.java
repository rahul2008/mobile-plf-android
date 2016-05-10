package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Map;

import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.Key;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.State;

public class DiCommFirmwarePortStateWaiter implements DiCommPort.UpdateListener {

    private static final String TAG = "DiCommFirmwarePort";
    private State expectedState;

    public interface Listener {

        void onRequestReceived(State state, SHNResult shnResult);
    }

    @NonNull
    private final DiCommFirmwarePort diCommFirmwarePort;

    @Nullable
    private Listener listener;

    public DiCommFirmwarePortStateWaiter(@NonNull DiCommFirmwarePort diCommFirmwarePort) {
        this.diCommFirmwarePort = diCommFirmwarePort;
    }

    public void waitUntilStateIsReached(@NonNull final State state, @NonNull final Listener listener) {
        if (diCommFirmwarePort.getState() == state) {
            listener.onRequestReceived(state, SHNResult.SHNOk);
        } else {
            diCommFirmwarePort.subscribe(this, new SHNResultListener() {
                @Override
                public void onActionCompleted(SHNResult result) {
                    if (result != SHNResult.SHNOk) {
                        listener.onRequestReceived(null, result);
                    } else {
                        DiCommFirmwarePortStateWaiter.this.listener = listener;
                        DiCommFirmwarePortStateWaiter.this.expectedState = state;
                    }
                }
            });
        }
    }

    public void cancel() {
        listener = null;
        diCommFirmwarePort.unsubscribe(this, null);
    }

    //implements DiCommPort.UpdateListener
    @Override
    public void onPropertiesChanged(@NonNull Map<String, Object> properties) {
        if (properties.containsKey(Key.STATE)) {
            Object state = properties.get(Key.STATE);

            if (state instanceof String) {
                State newState = State.fromString((String) state);

                if (newState == expectedState) {
                    notifyListenerAndCancel(newState, SHNResult.SHNOk);
                } else {
                    if (!isTransientState(newState)) {
                        SHNLogger.e(TAG, "was expecting state: " + expectedState + "but encountered state: " + newState);
                        notifyListenerAndCancel(newState, SHNResult.SHNErrorInvalidState);
                    }
                }
            }
        }
    }

    @Override
    public void onSubscriptionFailed(SHNResult shnResult) {

    }

    private void notifyListenerAndCancel(State newState, SHNResult shnErrorInvalidState) {
        if (listener != null) {
            listener.onRequestReceived(newState, shnErrorInvalidState);
        }
        cancel();
    }

    private boolean isTransientState(State newState) {
        switch (newState) {
            case Preparing:
            case Checking:
            case Programming:
            case Canceling:
                return true;
            default:
                return false;
        }
    }
}


