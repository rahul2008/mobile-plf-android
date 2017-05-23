package com.example.cdpp.bluelibexampleapp.uapp;

import android.content.Context;
import android.content.Intent;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class BleDemoAppMicroAppInterface implements UappInterface {
    private Context context;
    public static String WELCOME_MESSAGE = "welcome_message";
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        BleDemoAppMicroAppLaunchInput bleDemoAppMicroAppLaunchInput = (BleDemoAppMicroAppLaunchInput) uappLaunchInput;
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, BlueLibExampleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            //TODO:Need to add logic to make implmentation using fragment.
        }
    }
}
