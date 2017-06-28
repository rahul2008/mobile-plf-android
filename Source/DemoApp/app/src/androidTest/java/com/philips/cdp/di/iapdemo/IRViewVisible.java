package com.philips.cdp.di.iapdemo;

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
        if(view.getVisibility() == View.VISIBLE && callback != null) {
            callback.onTransitionToIdle();
            return true;
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
