package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.os.Handler;

public class ThreadUtils {
    public static void postInMainThread(Context context, Runnable runnable) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(runnable);
    }
}
