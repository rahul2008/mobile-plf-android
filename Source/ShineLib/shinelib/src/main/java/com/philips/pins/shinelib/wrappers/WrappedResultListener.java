package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;

public class WrappedResultListener<T> implements ResultListener<T> {

    @NonNull
    private final Handler handler;

    @NonNull
    private final ResultListener<T> listener;

    public WrappedResultListener(final @NonNull Handler handler, final @NonNull ResultListener<T> listener) {
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    public void onActionCompleted(final T value, @NonNull final SHNResult result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onActionCompleted(value, result);
            }
        });
    }
}