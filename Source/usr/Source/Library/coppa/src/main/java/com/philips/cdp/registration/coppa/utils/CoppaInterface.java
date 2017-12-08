package com.philips.cdp.registration.coppa.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.coppa.ui.activity.RegistrationCoppaActivity;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uid.thememanager.*;

public class CoppaInterface extends URInterface {

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

            RegistrationLaunchMode registrationLaunchMode = RegistrationLaunchMode.DEFAULT;
            if (((CoppaLaunchInput) uappLaunchInput).getEndPointScreen() != null) {
                registrationLaunchMode = ((CoppaLaunchInput)
                        uappLaunchInput).getEndPointScreen();
            }
            UIFlow uiFlow =((CoppaLaunchInput) uappLaunchInput).getUIflow();
            RegUtility.setUiFlow(uiFlow);
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, registrationLaunchMode);
            bundle.putSerializable(RegConstants.REGISTRATION_UI_FLOW, uiFlow);
            RegistrationContentConfiguration registrationContentConfiguration = ((CoppaLaunchInput) uappLaunchInput).
                    getRegistrationContentConfiguration();

            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG, registrationContentConfiguration);

            bundle.putBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, ((CoppaLaunchInput)
                    uappLaunchInput).isParentalFragment());
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(fragmentLauncher.getActionbarListener());
            if (null != uappLaunchInput && null != ((CoppaLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener()) {
                RegistrationConfiguration.getInstance().setUserRegistrationUIEventListener
                        (((CoppaLaunchInput) uappLaunchInput).
                                getUserRegistrationUIEventListener());
                ((CoppaLaunchInput) uappLaunchInput).setUserRegistrationUIEventListener(null);
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

            ThemeConfiguration themeConfiguration = uiLauncher.getDlsThemeConfiguration();
            if(themeConfiguration != null) {
                RegistrationHelper.getInstance().setThemeConfiguration(themeConfiguration);
            }
            int themeResId = uiLauncher.getUiKitTheme();
            RegistrationHelper.getInstance().setTheme(themeResId);

            RegistrationConfiguration.getInstance().setUserRegistrationUIEventListener(((CoppaLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener());
            ((CoppaLaunchInput) uappLaunchInput).setUserRegistrationUIEventListener(null);
            Intent registrationIntent = new Intent(RegistrationHelper.getInstance().
                    getUrSettings().getContext(), RegistrationCoppaActivity.class);
            Bundle bundle = new Bundle();

            RegistrationLaunchMode registrationLaunchMode = RegistrationLaunchMode.DEFAULT;

            UIFlow uiFlow =((CoppaLaunchInput) uappLaunchInput).getUIflow();
            RegUtility.setUiFlow(uiFlow);

            if(((CoppaLaunchInput)uappLaunchInput).getEndPointScreen() !=null){
                registrationLaunchMode = ((CoppaLaunchInput)
                        uappLaunchInput).getEndPointScreen();
            }
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, registrationLaunchMode);

            RegistrationContentConfiguration registrationContentConfiguration = ((CoppaLaunchInput) uappLaunchInput).
                    getRegistrationContentConfiguration();
            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG, registrationContentConfiguration);
            bundle.putSerializable(RegConstants.REGISTRATION_UI_FLOW, uiFlow);

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
}
