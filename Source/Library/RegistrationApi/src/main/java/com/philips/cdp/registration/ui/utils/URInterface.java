package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.Locale;

public class URInterface implements UappInterface{


    private static URInterface ourInstance = new URInterface();

    public static URInterface getInstance() {
        return ourInstance;
    }

    private URInterface() {
    }


    private Context mContext;

    @Override
    public void init(Context context, UappDependencies uappDependencies) {
        mContext = context;

        RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
        RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        RegistrationHelper.getInstance().initializeUserRegistration(mContext);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {

        UserRegistrationHelper.getInstance().registerEventNotification((UserRegistrationListener) uappListener);

        if(uiLauncher instanceof ActivityLauncher){
            launchAsActivity(((ActivityLauncher)uiLauncher),uappLaunchInput,uappListener);
        }else{
            launchAsFragment(uiLauncher,uappLaunchInput,uappListener);
        }

    }

    private void launchAsFragment(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {

    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {
        Intent registrationIntent = new Intent(mContext, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput)uappLaunchInput).isAccountSettings());
        bundle.putInt(RegConstants.ORIENTAION, uiLauncher.getScreenOrientation().getOrientationValue());
        registrationIntent.putExtras(bundle);
        registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(registrationIntent);
    }


}
