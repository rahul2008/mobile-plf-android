package com.philips.pins.shinelib;

import android.support.annotation.NonNull;

public interface ResultListener<T> {
    void onActionCompleted(T value, @NonNull final SHNResult result);
}
