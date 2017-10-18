/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.view.EWSActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

@SuppressWarnings("WeakerAccess")
public class EWSInterface implements UappInterface, EWSContentConfigurationInterface {

    public static final String ERROR_MSG_UNSUPPORTED_LAUNCHER_TYPE = "EWS does not support FragmentLauncher at present. Please use ActivityLauncher approach";
    public static final String ERROR_MSG_INVALID_CALL = "Please call \"init\" method, before calling launching ews with valid params";
    public static final String SCREEN_ORIENTATION = "screen.orientation";
    public static final String PRODUCT_NAME = "productName";
    private Context context;
    private Parcelable baseContentConfiguration;
    private HappyFlowContentConfiguration happyFlowContentConfiguration;



    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        EWSDependencies ewsDependencies = (EWSDependencies) uappDependencies;
        EWSDependencyProvider.getInstance().initDependencies(uappDependencies.getAppInfra(), ewsDependencies.getProductKeyMap());
        context = uappSettings.getContext();
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof FragmentLauncher) {
            throw new UnsupportedOperationException(ERROR_MSG_UNSUPPORTED_LAUNCHER_TYPE);
        }

        if (!EWSDependencyProvider.getInstance().areDependenciesInitialized()) {
            throw new UnsupportedOperationException(ERROR_MSG_INVALID_CALL);
        }

        EWSCallbackNotifier.getInstance().setCallback(((EWSLauncherInput) uappLaunchInput).getCallback());
        Intent intent = new Intent(context, EWSActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(EWSActivity.KEY_BASECONTENT_CONFIGURATION, baseContentConfiguration);
        intent.putExtra(EWSActivity.KEY_HAPPYFLOW_CONFIGURATION, happyFlowContentConfiguration);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        context.startActivity(intent);
    }

    @Override
    public void setContentConfiguration(@Nullable  BaseContentConfiguration baseContentConfiguration,
                                        @Nullable HappyFlowContentConfiguration happyFlowContentConfiguration) {
        this.baseContentConfiguration = baseContentConfiguration;
        this.happyFlowContentConfiguration = happyFlowContentConfiguration;
    }

}