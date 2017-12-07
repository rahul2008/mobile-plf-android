/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.EWSActivity;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.DependencyHelper;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSDependencyProviderModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import javax.inject.Inject;

/*
 * EWSInterface is an entry point for EWS launching,
 * All the initialisation for EWS should be done using it.
 */
@SuppressWarnings("WeakerAccess")
public class EWSUapp implements UappInterface {

    public static final String ERROR_MSG_INVALID_CALL = "Please call \"init\" method, before calling launching ews with valid params";
    public static final String ERROR_MSG_INVALID_IMPLEMENTATION = "Please implement EWSActionBarListener in Activity";
    public static final String SCREEN_ORIENTATION = "screen.orientation";
    public static final String PRODUCT_NAME = "productName";
    private static final String TAG = "EWSInterface";
    @Inject
    Navigator navigator;

    @NonNull
    private Context context;

    private DependencyHelper dependencyHelper;

    /**
     * Entry point for EWS. Please make sure no EWS components are being used before EWSInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application context.
     */
    @Override
    public void init(@NonNull final UappDependencies uappDependencies, @NonNull final UappSettings uappSettings) {
        EWSDependencies ewsDependencies = (EWSDependencies) uappDependencies;
        dependencyHelper = new DependencyHelper(uappDependencies.getAppInfra(), ewsDependencies.getCommCentral(), ewsDependencies.getProductKeyMap(), ewsDependencies.getContentConfiguration());
        context = uappSettings.getContext();
    }

    /**
     * Launches the EWS user interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - URLaunchInput
     */
    @Override
    public void launch(@NonNull final UiLauncher uiLauncher, @NonNull final UappLaunchInput uappLaunchInput) {
        if (!DependencyHelper.areDependenciesInitialized()) {
            throw new UnsupportedOperationException(ERROR_MSG_INVALID_CALL);
        }

        if (uiLauncher instanceof FragmentLauncher) {
            if (!(((FragmentLauncher) uiLauncher).getFragmentActivity() instanceof EWSActionBarListener)) {
                throw new UnsupportedOperationException(ERROR_MSG_INVALID_IMPLEMENTATION);
            }
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
        } else if (uiLauncher instanceof ActivityLauncher) {
            dependencyHelper.setThemeConfiguration(((ActivityLauncher) uiLauncher).getDlsThemeConfiguration());
            launchAsActivity(uappLaunchInput);
        }
    }

    @VisibleForTesting
    void launchAsFragment(@NonNull final FragmentLauncher fragmentLauncher, @NonNull final UappLaunchInput uappLaunchInput) {
        EWSComponent ewsComponent = null;
        try {
            ewsComponent = createEWSComponent(fragmentLauncher);
            ewsComponent.inject(this);
            ((EWSLauncherInput) uappLaunchInput).setContainerFrameId(fragmentLauncher.getParentContainerResourceID());
            ((EWSLauncherInput) uappLaunchInput).setFragmentManager(fragmentLauncher.getFragmentActivity().getSupportFragmentManager());
            navigator.navigateToGettingStartedScreen();
            ewsComponent.getEWSTagger().collectLifecycleInfo(fragmentLauncher.getFragmentActivity());
        } catch (Exception e) {
            ewsComponent.getEWSLogger().e(TAG,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    @VisibleForTesting
    @NonNull
    EWSComponent createEWSComponent(@NonNull FragmentLauncher fragmentLauncher) {
        return DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(fragmentLauncher.getFragmentActivity(),
                        fragmentLauncher.getFragmentActivity().getSupportFragmentManager(),
                        fragmentLauncher.getParentContainerResourceID(), DependencyHelper.getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(fragmentLauncher.getFragmentActivity(), DependencyHelper.getContentConfiguration()))
                .eWSDependencyProviderModule(new EWSDependencyProviderModule(DependencyHelper.getAppInfraInterface(), DependencyHelper.getProductKeyMap()))
                .build();
    }

    private void launchAsActivity(@NonNull final UappLaunchInput uappLaunchInput) {
        ((EWSLauncherInput) uappLaunchInput).setContainerFrameId(R.id.contentFrame);
        Intent intent = new Intent(context, EWSActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(EWSActivity.KEY_CONTENT_CONFIGURATION, DependencyHelper.getContentConfiguration());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

}