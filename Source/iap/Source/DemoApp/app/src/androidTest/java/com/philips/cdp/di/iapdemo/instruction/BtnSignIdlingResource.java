//package com.philips.cdp.di.iapdemo.instruction;
//
//import android.app.Activity;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.IdlingResource;
//import android.widget.Button;
//
//import com.philips.cdp.di.iapdemo.DemoApplication;
//import com.philips.cdp.di.iapdemo.R;
//
//
///**
// * Created by F1sherKK on 14/04/16.
// */
//public class BtnSignIdlingResource implements IdlingResource {
//    private ResourceCallback resourceCallback;
//    private boolean isIdle;
//
//    @Override
//    public String getName() {
//        return BtnSignIdlingResource.class.getName();
//    }
//
//    @Override
//    public boolean isIdleNow() {
//        if (isIdle) return true;
//
//        Activity activity = getCurrentActivity();
//        if (activity == null) return false;
//
//        Button btnStart = (Button) activity.findViewById(R.id.btn_reg_sign_in);
//        isIdle = (btnStart != null && btnStart.getTranslationY() == 0);
//        if (isIdle) {
//            resourceCallback.onTransitionToIdle();
//        }
//        return isIdle;
//    }
//
//    public Activity getCurrentActivity() {
//        return ((DemoApplication) InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();
//    }
//
//    @Override
//    public void registerIdleTransitionCallback(
//            ResourceCallback resourceCallback) {
//        this.resourceCallback = resourceCallback;
//    }
//}
