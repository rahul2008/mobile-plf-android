package com.philips.pins.shinelib.capabilities;

import android.os.Handler;
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
    @NonNull
    private Handler internalHandler;

    @Nullable
    private Listener listener;

    public DiCommFirmwarePortStateWaiter(@NonNull DiCommFirmwarePort diCommFirmwarePort, @NonNull Handler internalHandler) {
        this.diCommFirmwarePort = diCommFirmwarePort;
        this.internalHandler = internalHandler;
    }

    public void waitUntilStateIsReached(@NonNull final State state, @NonNull final Listener listener) {
        if (diCommFirmwarePort.getState() == state) {
            listener.onRequestReceived(state, SHNResult.SHNOk);
        } else {
            SHNLogger.d(TAG, "stateWaiter subscribe for State " + state);
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
        SHNLogger.d(TAG, "stateWaiter cancel ");

        listener = null;
        diCommFirmwarePort.unsubscribe(this, null);
    }

    //implements DiCommPort.UpdateListener
    @Override
    public void onPropertiesChanged(@NonNull Map<String, Object> properties) {
        SHNLogger.d(TAG, "stateWaiter onPropertiesChanged " + properties);

        if (properties.containsKey(Key.STATE)) {
            Object state = properties.get(Key.STATE);

            if (state instanceof String) {
                State newState = State.fromString((String) state);

                if (newState == expectedState) {
                    notifyListenerAndCancel(newState, SHNResult.SHNOk);
                } else {
                    if (!isTransientState(newState)) {
                        SHNLogger.e(TAG, "was expecting state: " + expectedState + " but encountered state: " + newState);
                        notifyListenerAndCancel(newState, SHNResult.SHNErrorInvalidState);
                    }
                }
            }
        }
    }

    private void notifyListenerAndCancel(final State newState, final SHNResult shnErrorInvalidState) {
        if (listener != null) {
            internalHandler.postDelayed(new Runnable() {
                private Listener runnableListener = DiCommFirmwarePortStateWaiter.this.listener;

                @Override
                public void run() {
                    this.runnableListener.onRequestReceived(newState, shnErrorInvalidState);
                }
            }, 1);
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


