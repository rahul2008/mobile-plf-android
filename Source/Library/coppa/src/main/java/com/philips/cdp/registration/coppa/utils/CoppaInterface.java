package com.philips.cdp.registration.coppa.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.ui.activity.RegistrationCoppaActivity;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CoppaInterface implements UappInterface {

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
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
            final RegistrationCoppaFragment registrationFragment = new RegistrationCoppaFragment();
            final Bundle bundle = new Bundle();
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, ((CoppaLaunchInput)
                    uappLaunchInput).getRegistrationLaunchMode());
            bundle.putBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, ((CoppaLaunchInput)
                    uappLaunchInput).isParentalFragment());
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(fragmentLauncher.getActionbarListener());
            if (null != uappLaunchInput && null != ((CoppaLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener()) {
                registrationFragment.setUserRegistrationUIEventListener
                        (((CoppaLaunchInput) uappLaunchInput).
                                getUserRegistrationUIEventListener());
            }
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    registrationFragment,
                    RegConstants.REGISTRATION_COPPA_FRAGMENT_TAG);
            if (((CoppaLaunchInput)
                    uappLaunchInput).isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(RegConstants.REGISTRATION_COPPA_FRAGMENT_TAG);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured in " +
                            "addFragment  :"
                            + e.getMessage());
        }
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

        if (null != uappLaunchInput) {
            RegistrationFunction registrationFunction = ((CoppaLaunchInput) uappLaunchInput).
                    getRegistrationFunction();
            if (null != registrationFunction) {
                RegistrationConfiguration.getInstance().setPrioritisedFunction
                        (registrationFunction);
            }

            RegistrationCoppaActivity.setUserRegistrationUIEventListener(((CoppaLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener());
            Intent registrationIntent = new Intent(RegistrationHelper.getInstance().
                    getUrSettings().getContext(), RegistrationCoppaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, ((CoppaLaunchInput)
                    uappLaunchInput).getRegistrationLaunchMode());
            bundle.putInt(RegConstants.ORIENTAION, uiLauncher.getScreenOrientation().
                    getOrientationValue());
            bundle.putBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, ((CoppaLaunchInput)
                    uappLaunchInput).isParentalFragment());
            registrationIntent.putExtras(bundle);
            registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            RegistrationHelper.getInstance().
                    getUrSettings().getContext().startActivity(registrationIntent);
        }

    }

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        RegistrationHelper.getInstance().setAppInfraInstance(((CoppaDependancies) uappDependencies).getAppInfra());
        RegistrationHelper.getInstance().setUrSettings(((CoppaSettings) uappSettings));
        RegistrationHelper.getInstance().initializeUserRegistration(((CoppaSettings) uappSettings).getContext());
    }
}
