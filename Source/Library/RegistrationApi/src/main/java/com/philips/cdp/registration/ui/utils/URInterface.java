package com.philips.cdp.registration.ui.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class URInterface implements UappInterface {

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (null != uappLaunchInput && null != ((URLaunchInput) uappLaunchInput).
                getUserRegistrationUIEventListener()) {
            RegistrationHelper.getInstance().setUserRegistrationUIEventListener
                    (((URLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener());
        }
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
        } else {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher,
                                  UappLaunchInput uappLaunchInput) {

        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput)
                    uappLaunchInput).isAccountSettings());
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(fragmentLauncher.
                    getActionbarListener());
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            if(((URLaunchInput)
                    uappLaunchInput).isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(RegConstants.REGISTRATION_FRAGMENT_TAG);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }

    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {


        if (null != uappLaunchInput) {
            RegistrationFunction registrationFunction = ((URLaunchInput) uappLaunchInput).
                    getRegistrationFunction();
            if (null != registrationFunction) {
                RegistrationConfiguration.getInstance().setPrioritisedFunction
                        (registrationFunction);
            }

            Intent registrationIntent = new Intent(RegistrationHelper.getInstance().
                    getUrSettings().getContext(), RegistrationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput)
                    uappLaunchInput).isAccountSettings());
            bundle.putInt(RegConstants.ORIENTAION, uiLauncher.getScreenOrientation().
                    getOrientationValue());
            registrationIntent.putExtras(bundle);
            registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            RegistrationHelper.getInstance().
                    getUrSettings().getContext().startActivity(registrationIntent);
        }


    }


    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        RegistrationHelper.getInstance().setAppInfraInstance(((URDependancies)uappDependencies).getAppInfra());
        RegistrationHelper.getInstance().setUrSettings(((URSettings)uappSettings));
        RegistrationHelper.getInstance().initializeUserRegistration(((URSettings)uappSettings).getContext());
    }

}
