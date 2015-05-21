package com.pins.philips.shinelib.wrappers;

import android.os.Handler;

import com.pins.philips.shinelib.SHNIntegerResultListener;
import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.capabilities.SHNCapabilityLogSynchronization;

/**
 * Created by 310188215 on 19/05/15.
 */
public class SHNCapabilityLogSynchronizationWrapper implements SHNCapabilityLogSynchronization {
    private final SHNCapabilityLogSynchronization wrappedShnCapabilityLogSynchronization;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityLogSynchronizationWrapper(SHNCapabilityLogSynchronization wrappedShnCapabilityLogSynchronization, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapabilityLogSynchronization = wrappedShnCapabilityLogSynchronization;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void setListener(final Listener listener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityLogSynchronization.setListener(listener);
            }
        };
        internalHandler.post(command);
    }

    @Override
    public State getState() {
        return wrappedShnCapabilityLogSynchronization.getState();
    }

    @Override
    public Object getLastSynchronizationToken() {
        return wrappedShnCapabilityLogSynchronization.getLastSynchronizationToken();
    }

    @Override
    public void startSynchronizationFromToken(final Object synchronizationToken) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityLogSynchronization.startSynchronizationFromToken(synchronizationToken);
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void abortSynchronization() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityLogSynchronization.abortSynchronization();
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void getValueForOption(final Option option, final SHNIntegerResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityLogSynchronization.getValueForOption(option, new SHNIntegerResultListener() {
                    @Override
                    public void onActionCompleted(final int value, final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(value, result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setValueForOption(final int value, final Option option, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityLogSynchronization.setValueForOption(value, option, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }
}
