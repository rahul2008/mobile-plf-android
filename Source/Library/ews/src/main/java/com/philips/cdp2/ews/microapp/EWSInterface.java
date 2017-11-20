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
import com.philips.cdp2.ews.communication.EventingChannel;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class EWSInterface implements UappInterface {

    public static final String ERROR_MSG_INVALID_CALL = "Please call \"init\" method, before calling launching ews with valid params";
    public static final String ERROR_MSG_INVALID_IMPLEMENTATION = "Please implement EWSActionBarListener in Activity";
    public static final String SCREEN_ORIENTATION = "screen.orientation";
    public static final String PRODUCT_NAME = "productName";
    private static final String TAG = "EWSInterface";
    @Inject
    Navigator navigator;
    @Inject
    EventingChannel<EventingChannel.ChannelCallback> ewsEventingChannel;
    private Context context;
    private ContentConfiguration contentConfiguration;

    public void init(@NonNull final UappDependencies uappDependencies, @NonNull final UappSettings uappSettings) {
        EWSDependencies ewsDependencies = (EWSDependencies) uappDependencies;
        EWSDependencyProvider.getInstance().initDependencies(uappDependencies.getAppInfra(), ewsDependencies.getProductKeyMap());
        context = uappSettings.getContext();
        EWSDependencyProvider.getInstance().setContext(context);
        contentConfiguration = ewsDependencies.getContentConfiguration();
    }

    @Override
    public void launch(@NonNull final UiLauncher uiLauncher, @NonNull final UappLaunchInput uappLaunchInput) {
        if (!EWSDependencyProvider.getInstance().areDependenciesInitialized()) {
            throw new UnsupportedOperationException(ERROR_MSG_INVALID_CALL);
        }

        if (uiLauncher instanceof FragmentLauncher) {
            if (!(((FragmentLauncher) uiLauncher).getFragmentActivity() instanceof EWSActionBarListener)) {
                throw new UnsupportedOperationException(ERROR_MSG_INVALID_IMPLEMENTATION);
            }
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
        } else if (uiLauncher instanceof ActivityLauncher) {
            EWSDependencyProvider.getInstance().setThemeConfiguration(((ActivityLauncher)uiLauncher).getDlsThemeConfiguration());
            launchAsActivity();
        }
    }

    @VisibleForTesting
    void launchAsFragment(@NonNull final FragmentLauncher fragmentLauncher, @NonNull final UappLaunchInput uappLaunchInput) {
        try {
            EWSDependencyProvider.getInstance().createEWSComponent(fragmentLauncher, contentConfiguration);
            EWSDependencyProvider.getInstance().getEwsComponent().inject(this);
            ((EWSLauncherInput) uappLaunchInput).setContainerFrameId(fragmentLauncher.getParentContainerResourceID());
            ((EWSLauncherInput) uappLaunchInput).setFragmentManager(fragmentLauncher.getFragmentActivity().getSupportFragmentManager());
            navigator.navigateToGettingStartedScreen();
            ewsEventingChannel.start();
            EWSTagger.collectLifecycleInfo(fragmentLauncher.getFragmentActivity());
        } catch (Exception e) {
            EWSLogger.e(TAG,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    private void launchAsActivity() {
        Intent intent = new Intent(context, EWSActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(EWSActivity.KEY_CONTENT_CONFIGURATION, contentConfiguration);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);

    }

}