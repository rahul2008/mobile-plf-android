package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.datatypes.SHNLog;

/**
 * Created by 310188215 on 19/05/15.
 */
public class SHNCapabilityLogSynchronizationWrapper implements SHNCapabilityLogSynchronization, SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener {
    private final SHNCapabilityLogSynchronization wrappedShnCapabilityLogSynchronization;
    private final Handler internalHandler;
    private final Handler userHandler;
    private SHNCapabilityLogSynchronizationListener SHNCapabilityLogSynchronizationListener;

    public SHNCapabilityLogSynchronizationWrapper(SHNCapabilityLogSynchronization wrappedShnCapabilityLogSynchronization, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapabilityLogSynchronization = wrappedShnCapabilityLogSynchronization;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        wrappedShnCapabilityLogSynchronization.setSHNCapabilityLogSynchronizationListener(this);
    }

    @Override
    public void setSHNCapabilityLogSynchronizationListener(final SHNCapabilityLogSynchronizationListener SHNCapabilityLogSynchronizationListener) {
        this.SHNCapabilityLogSynchronizationListener = SHNCapabilityLogSynchronizationListener;
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

    // implements SHNCapabilityLogSynchronization.Listener
    @Override
    public void onStateUpdated(final SHNCapabilityLogSynchronization shnCapabilityLogSynchronization) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (SHNCapabilityLogSynchronizationListener != null) {
                    SHNCapabilityLogSynchronizationListener.onStateUpdated(shnCapabilityLogSynchronization);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onProgressUpdate(final SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, final float progress) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (SHNCapabilityLogSynchronizationListener != null) {
                    SHNCapabilityLogSynchronizationListener.onProgressUpdate(shnCapabilityLogSynchronization, progress);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onLogSynchronized(final SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, final SHNLog shnLog, final SHNResult shnResult) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (SHNCapabilityLogSynchronizationListener != null) {
                    SHNCapabilityLogSynchronizationListener.onLogSynchronized(shnCapabilityLogSynchronization, shnLog, shnResult);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void onLogSynchronizationFailed(final SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, final SHNResult shnResult) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (SHNCapabilityLogSynchronizationListener != null) {
                    SHNCapabilityLogSynchronizationListener.onLogSynchronizationFailed(shnCapabilityLogSynchronization, shnResult);
                }
            }
        };
        userHandler.post(callback);
    }
}
