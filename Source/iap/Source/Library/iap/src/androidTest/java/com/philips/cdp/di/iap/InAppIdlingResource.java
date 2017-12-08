package com.philips.cdp.di.iap;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by philips on 5/2/17.
 */

public class InAppIdlingResource implements IdlingResource {

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);
    private ResourceCallback callback;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(final ResourceCallback callback) {

        this.callback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }

}
