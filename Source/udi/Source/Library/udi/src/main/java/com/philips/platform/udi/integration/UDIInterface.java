package com.philips.platform.udi.integration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.udi.LoginActivity;

public class UDIInterface implements UappInterface {

    private static final String ORIENTAION = "Orientaion";
    private String TAG = UDIInterface.class.getSimpleName();

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {

    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
            Log.i(TAG, "Launch : Launched as activity");
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
            Log.i(TAG, "Launch : Launched as fragment");
        }
    }

    private void launchAsFragment(FragmentLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        //TODO
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        Intent registrationIntent = new Intent(uiLauncher.getActivityContext(), LoginActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt(ORIENTAION, uiLauncher.getScreenOrientation().
                getOrientationValue());

        registrationIntent.putExtras(bundle);
        registrationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        uiLauncher.getActivityContext().startActivity(registrationIntent);
    }
}
