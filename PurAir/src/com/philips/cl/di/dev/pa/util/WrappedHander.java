package com.philips.cl.di.dev.pa.util;

import android.os.Handler;

public class WrappedHander {
    private Handler handler;

    public WrappedHander(Handler handler) {
        this.handler = handler;
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    public void postDelayed(Runnable runnable, int delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    public void removeCallbacks(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }
}
