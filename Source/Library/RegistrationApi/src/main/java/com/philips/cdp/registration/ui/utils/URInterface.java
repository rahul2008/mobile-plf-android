package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.Locale;

public class URInterface implements UappInterface {


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
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput, uappListener);
        } else {
            launchAsFragment((FragmentLauncher)uiLauncher, uappLaunchInput, uappListener);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {

        try {
            FragmentManager mFragmentManager =  fragmentLauncher.getFragmentActivity().getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput) uappLaunchInput).isAccountSettings());
            bundle.putSerializable(RegConstants.USER_REGISTRATION_LISTENER,
                    (UserRegistrationListener)uappListener);
            registrationFragment.setArguments(bundle);
          registrationFragment.setOnUpdateTitleListener(fragmentLauncher.getActionbarListener());
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }

    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {


        if (null != uappLaunchInput) {
            RegistrationFunction registrationFunction = ((URLaunchInput) uappLaunchInput).getRegistrationFunction();
            if (null != registrationFunction) {
                RegistrationConfiguration.getInstance().setPrioritisedFunction(registrationFunction);
            }
            Intent registrationIntent = new Intent(mContext, RegistrationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput) uappLaunchInput).isAccountSettings());
            bundle.putInt(RegConstants.ORIENTAION, uiLauncher.getScreenOrientation().getOrientationValue());
            bundle.putSerializable(RegConstants.USER_REGISTRATION_LISTENER,(UserRegistrationListener)uappListener);
            registrationIntent.putExtras(bundle);
            registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  RegistrationHelper.getInstance().registerUserRegistrationListener((UserRegistrationListener) uappListener);
            mContext.startActivity(registrationIntent);
        }


    }


}
