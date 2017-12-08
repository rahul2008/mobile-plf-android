/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.os.Handler;

/**
 * @publicPluginApi
 */
public class Timer {
    private static Handler tempStaticHandler; // TODO: remove when constructor is made private and all clients use CreateTimer
    private final Handler handler;
    private final Runnable runnable;
    private long timeoutTimeMS;

    public static Timer createTimer(Runnable runnable, long timeoutTimeMS) {
        return new Timer(Timer.tempStaticHandler, runnable, timeoutTimeMS);
    }

    public static void setHandler(Handler handler) {
        Timer.tempStaticHandler = handler;
    }

    public Timer(Handler handler, Runnable runnable, long timeoutTimeMS) {
        this.handler = handler;
        this.runnable = runnable;
        this.timeoutTimeMS = timeoutTimeMS;
    }

    public void restart() {
        restart(timeoutTimeMS);
    }

    public void restart(long timeoutTimeMS) {
        stop();
        handler.postDelayed(runnable, timeoutTimeMS);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void setTimeoutForSubsequentRestartsInMS(long timeoutInMS) {
        this.timeoutTimeMS = timeoutInMS;
    }
}
