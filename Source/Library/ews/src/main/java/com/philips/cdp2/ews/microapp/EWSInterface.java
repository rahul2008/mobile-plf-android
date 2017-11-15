/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.EWSActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

@SuppressWarnings("WeakerAccess")
public abstract class EWSInterface implements UappInterface {

    public static final String ERROR_MSG_UNSUPPORTED_LAUNCHER_TYPE = "EWS does not support FragmentLauncher at present. Please use ActivityLauncher approach";
    public static final String ERROR_MSG_INVALID_CALL = "Please call \"init\" method, before calling launching ews with valid params";
    public static final String SCREEN_ORIENTATION = "screen.orientation";
    public static final String PRODUCT_NAME = "productName";

    private Context context;
    private ContentConfiguration contentConfiguration;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        EWSDependencyProvider.getInstance().applyTheme(getTheme());
        EWSDependencies ewsDependencies = (EWSDependencies) uappDependencies;
        EWSDependencyProvider.getInstance().initDependencies(uappDependencies.getAppInfra(), ewsDependencies.getProductKeyMap());
        context = uappSettings.getContext();
        contentConfiguration = ewsDependencies.getContentConfiguration();
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof FragmentLauncher) {
            throw new UnsupportedOperationException(ERROR_MSG_UNSUPPORTED_LAUNCHER_TYPE);
        }

        if (!EWSDependencyProvider.getInstance().areDependenciesInitialized()) {
            throw new UnsupportedOperationException(ERROR_MSG_INVALID_CALL);
        }

        Intent intent = new Intent(context, EWSActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(EWSActivity.KEY_CONTENT_CONFIGURATION, contentConfiguration);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        context.startActivity(intent);
    }

    public abstract ThemeConfiguration getTheme() ;


}