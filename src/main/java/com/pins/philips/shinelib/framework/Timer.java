package com.pins.philips.shinelib.framework;

import android.os.Handler;

/**
 * Created by 310188215 on 05/05/15.
 */
public class Timer {
    private static Handler tempStaticHandler; // TODO: remove when constructor is made private and all clients use CreateTimer
    private final Handler handler;
    private final Runnable runnable;
    private final long timeoutTimeMS;

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
        stop();
        handler.postDelayed(runnable, timeoutTimeMS);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
