
package com.philips.platform.myapplication;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.thsdemolaunch.MainActivity;
import com.philips.platform.thsdemolaunch.THSAppInfraInstance;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

@SuppressWarnings("serial")
public class DemoMicroAppApplicationuAppInterface implements UappInterface {

    private Context context;
    private AppInfraInterface appInfraInterface;

 /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
       this.context = uappSettings.getContext();
        this.appInfraInterface = uappDependencies.getAppInfra();
        THSAppInfraInstance.getInstance().setAppInfraInterface(appInfraInterface);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {

       if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } 
    }
}
