package com.philips.cdp.dicomm.util;

import android.os.Handler;

public class WrappedHandler {
    private Handler handler;

    public WrappedHandler(Handler handler) {
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
