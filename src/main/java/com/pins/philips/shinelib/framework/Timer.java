package com.pins.philips.shinelib.framework;

import android.os.Handler;

/**
 * Created by 310188215 on 05/05/15.
 */
public class Timer {
    private final Handler handler;
    private final Runnable runnable;
    private final long timeoutTimeMS;

    public Timer(Handler handler, Runnable runnable, long timeoutTimeMS) {
        this.handler = handler;
        this.runnable = runnable;
        this.timeoutTimeMS = timeoutTimeMS;
    }

    public void restart() {
        stop();
        handler.postDelayed(runnable, timeoutTimeMS);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
