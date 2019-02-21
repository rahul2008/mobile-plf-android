package com.philips.platform.udi.integration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.udi.UDIActivity;
import com.philips.platform.udi.injection.AppAuthComponent;
import com.philips.platform.udi.injection.AppInfraModule;
import com.philips.platform.udi.configration.UdiConfiguration;
import com.philips.platform.udi.injection.DaggerAppAuthComponent;
import com.philips.platform.udi.utilities.AuthLog;
import com.philips.platform.udi.utilities.AuthTagging;
import com.philips.platform.udi.utilities.UdiConstants;

public class UDIInterface implements UappInterface {

    private String TAG = UDIInterface.class.getSimpleName();
    private AppAuthComponent component;
    private Context context;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        component = initDaggerComponents(uappDependencies, uappSettings);
        context = uappSettings.getContext();
        UdiConfiguration.getInstance().setComponent(component);
        AuthLog.init();
        AuthTagging.init();
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
        addFragment( uiLauncher, uappLaunchInput);
    }

    private void addFragment(FragmentLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        Intent intent = new Intent(uiLauncher.getActivityContext(), UDIActivity.class);
        intent.putExtra(UdiConstants.UDI_KEY_ACTIVITY_THEME, uiLauncher.getUiKitTheme());
        uiLauncher.getActivityContext().startActivity(intent);
    }

    @NonNull
    private AppAuthComponent initDaggerComponents(UappDependencies uappDependencies, UappSettings uappSettings) {
        return DaggerAppAuthComponent.builder()
                .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra()))
                .build();
    }
}
