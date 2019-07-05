package com.philips.cdp.di.pesdemo;

import android.support.test.espresso.IdlingResource;
import android.view.View;

/**
 * Created by philips on 6/28/17.
 */

public class IRViewVisible implements IdlingResource {
    private View view;
    private ResourceCallback callback;

    public IRViewVisible(View view) {
        this.view = view;
    }

    @Override
    public String getName() {
        return IRViewVisible.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if (view.getVisibility() == View.VISIBLE && view.isEnabled() && view.isActivated() && callback != null) {
            callback.onTransitionToIdle();
            return false;
        }
        return true;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
