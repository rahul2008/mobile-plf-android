package com.philips.cdp.registration.ui.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.janrain.android.Jump;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.injection.AppInfraModule;
import com.philips.cdp.registration.injection.DaggerRegistrationComponent;
import com.philips.cdp.registration.injection.NetworkModule;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.injection.UserModule;
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

import java.io.Serializable;

public class URInterface implements UappInterface {

    private static RegistrationComponent component;

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
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

            RegistrationLaunchMode registrationLaunchMode =  RegistrationLaunchMode.DEFAULT;
            if(((URLaunchInput) uappLaunchInput).isAccountSettings()){
                registrationLaunchMode= RegistrationLaunchMode.ACCOUNT_SETTINGS;
            }

            if(((URLaunchInput)uappLaunchInput).getEndPointScreen()!=null){
                registrationLaunchMode = ((URLaunchInput)uappLaunchInput).getEndPointScreen();
            }
            RegistrationContentConfiguration registrationContentConfiguration = ((URLaunchInput) uappLaunchInput).
                    getRegistrationContentConfiguration();
            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG, registrationContentConfiguration);

            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, registrationLaunchMode);
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput)
                    uappLaunchInput).isAccountSettings());
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(fragmentLauncher.
                    getActionbarListener());

            if (null != uappLaunchInput && null != ((URLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener()) {
                registrationFragment.setUserRegistrationUIEventListener
                        (((URLaunchInput) uappLaunchInput).
                                getUserRegistrationUIEventListener());
            }

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            if (((URLaunchInput)
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

            RegistrationContentConfiguration registrationContentConfiguration = ((URLaunchInput) uappLaunchInput).
                    getRegistrationContentConfiguration();

            RegistrationActivity.setUserRegistrationUIEventListener(((URLaunchInput) uappLaunchInput).
                    getUserRegistrationUIEventListener());
            Intent registrationIntent = new Intent(RegistrationHelper.getInstance().
                    getUrSettings().getContext(), RegistrationActivity.class);
            Bundle bundle = new Bundle();

            RegistrationLaunchMode registrationLaunchMode =  RegistrationLaunchMode.DEFAULT;

            if(((URLaunchInput) uappLaunchInput).isAccountSettings()){
                registrationLaunchMode= RegistrationLaunchMode.ACCOUNT_SETTINGS;
            }

            if(((URLaunchInput)uappLaunchInput).getEndPointScreen()!=null){
                registrationLaunchMode = ((URLaunchInput)uappLaunchInput).getEndPointScreen();
            }
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, registrationLaunchMode);
            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG, registrationContentConfiguration);
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, ((URLaunchInput)
                    uappLaunchInput).isAccountSettings());
            bundle.putInt(RegConstants.ORIENTAION, uiLauncher.getScreenOrientation().
                    getOrientationValue());

            registrationIntent.putExtras(bundle);
            registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            RegistrationHelper.getInstance().
                    getUrSettings().getContext().startActivity(registrationIntent);
        }
    }

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        Jump.init(uappSettings.getContext(), uappDependencies.getAppInfra().getSecureStorage());
        component = initDaggerComponents(uappDependencies, uappSettings);
        Jump.init(uappSettings.getContext(), uappDependencies.getAppInfra().getSecureStorage());
        RegistrationHelper.getInstance().setUrSettings(uappSettings);
        RegistrationHelper.getInstance().initializeUserRegistration(uappSettings.getContext());
    }

    public static RegistrationComponent getComponent() {
        return component;
    }

    @NonNull
    private RegistrationComponent initDaggerComponents(UappDependencies uappDependencies, UappSettings uappSettings) {
        return DaggerRegistrationComponent.builder()
                    .networkModule(new NetworkModule(uappSettings.getContext()))
                    .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra()))
                    .userModule(new UserModule(uappSettings.getContext()))
                    .build();
    }

    @Deprecated
    @VisibleForTesting
    public static void setComponent(RegistrationComponent componentMock) {
        component = componentMock;
    }
}
