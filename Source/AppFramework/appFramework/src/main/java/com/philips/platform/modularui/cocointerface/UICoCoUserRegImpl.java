package com.philips.platform.modularui.cocointerface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.introscreen.IntroductionScreenActivity;

/**
 * Created by 310240027 on 6/22/2016.
 */

public class UICoCoUserRegImpl implements UICoCoInterface,UserRegistrationListener,RegistrationTitleBarListener {
    Context context;
    User userObject;


    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    @Override
    public void loadPlugIn(Context context) {
        this.context = context;
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);

    }

    @Override
    public void runCoCo(Context context) {
        this.context.startActivity(new Intent(context, RegistrationActivity.class));

    }

    @Override
    public void unloadCoCo() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);

    }

    @Override
    public void setActionbar(ActionbarUpdateListener actionBarClickListener) {

    }

    @Override
    public void setFragActivity(FragmentActivity fa) {

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            if(context instanceof IntroductionScreenActivity) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
            } else if(context instanceof HomeActivity){
                activity.finish();
            }
        }

    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {

    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    @Override
    public void updateRegistrationTitle(int i) {

    }

    @Override
    public void updateRegistrationTitleWithBack(int i) {

    }

    @Override
    public void updateRegistrationTitleWithOutBack(int i) {

    }
}
