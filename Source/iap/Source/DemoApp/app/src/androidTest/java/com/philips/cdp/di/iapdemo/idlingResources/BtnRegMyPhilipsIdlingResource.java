//package com.philips.cdp.di.iapdemo.idlingResources;
//
//import android.app.Activity;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.IdlingResource;
//
//import com.philips.cdp.di.iapdemo.DemoApplication;
//import com.philips.cdp.di.iapdemo.R;
//import com.philips.cdp.registration.ui.customviews.XProviderButton;
//
//public class BtnRegMyPhilipsIdlingResource implements IdlingResource {
//    private ResourceCallback resourceCallback;
//    private boolean isIdle;
//
//    @Override
//    public String getName() {
//        return BtnRegMyPhilipsIdlingResource.class.getName();
//    }
//
//    @Override
//    public boolean isIdleNow() {
//        if (isIdle) return true;
//
//        Activity activity = getCurrentActivity();
//        if (activity == null) return false;
//
//        XProviderButton btnStart = (XProviderButton) activity.findViewById(R.id.btn_reg_my_philips);
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
