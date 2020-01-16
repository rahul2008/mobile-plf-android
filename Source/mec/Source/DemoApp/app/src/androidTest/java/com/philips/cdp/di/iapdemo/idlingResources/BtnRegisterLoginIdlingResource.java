package com.philips.cdp.di.iapdemo.idlingResources;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;
import android.widget.Button;

import com.philips.cdp.di.iapdemo.R;

public class BtnRegisterLoginIdlingResource implements IdlingResource {
    private ResourceCallback resourceCallback;
    private boolean isIdle;

    @Override
    public String getName() {
        return BtnRegisterLoginIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) return true;

        Activity activity = getCurrentActivity();
        if (activity == null) return false;

        Button btnStart = (Button) activity.findViewById(R.id.btn_register);
        isIdle = (btnStart != null && btnStart.getTranslationY() == 0);
        if (isIdle) {
            resourceCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    public Activity getCurrentActivity() {
        return null;
    }

    @Override
    public void registerIdleTransitionCallback(
            ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
