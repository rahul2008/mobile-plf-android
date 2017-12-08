package com.philips.platform.aildemo;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class AILDemouAppInterface implements UappInterface {

    private static AILDemouAppInterface aILDemouAppInterface;
    private Context mContext;
    private AppInfraInterface mAppInfra;

    private AILDemouAppInterface() {
    }

    public static AILDemouAppInterface getInstance() {
        if (null == aILDemouAppInterface) {
            aILDemouAppInterface = new AILDemouAppInterface();
        }
        return aILDemouAppInterface;
    }

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.mContext = uappSettings.getContext();
        this.mAppInfra = uappDependencies.getAppInfra();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(mContext, AppInfraMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public void setAppInfra(AppInfraInterface mAppInfra) {
        this.mAppInfra = mAppInfra;
    }
}
