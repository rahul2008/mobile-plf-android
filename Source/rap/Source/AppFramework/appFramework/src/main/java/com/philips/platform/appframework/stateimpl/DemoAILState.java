package com.philips.platform.appframework.stateimpl;

import android.app.Activity;
import android.content.Context;

import com.philips.platform.aildemo.AILDemouAppDependencies;
import com.philips.platform.aildemo.AILDemouAppInterface;
import com.philips.platform.aildemo.AILDemouAppLaunchInput;
import com.philips.platform.aildemo.AILDemouAppSettings;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoAILState extends BaseState {

    private Context context;

    public DemoAILState() {
        super(AppStates.TESTAPPINFRA);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        AILDemouAppDependencies mAIDemoAppDependencies = new AILDemouAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra());
        AILDemouAppSettings aiDemoAppSettings = new AILDemouAppSettings(context.getApplicationContext());
        AILDemouAppLaunchInput mAIDemoAppLaunchInput = new AILDemouAppLaunchInput();
        AILDemouAppInterface mAIDemoAppInterface = AILDemouAppInterface.getInstance();
        mAIDemoAppInterface.init(mAIDemoAppDependencies, aiDemoAppSettings);
        ActivityLauncher activityLauncher = new ActivityLauncher((Activity) context,ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR,null, 0,null);
        mAIDemoAppInterface.launch(activityLauncher, mAIDemoAppLaunchInput);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }
}
