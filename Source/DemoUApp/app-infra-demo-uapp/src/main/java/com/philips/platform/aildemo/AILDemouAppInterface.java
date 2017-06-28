package com.philips.platform.aildemo;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class AILDemouAppInterface implements UappInterface {

    private Context mContext;
    public static AppInfraInterface mAppInfra;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.mContext = uappSettings.getContext();
        AILDemouAppInterface.mAppInfra = uappDependencies.getAppInfra();
        AppTaggingInterface mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");


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
}
