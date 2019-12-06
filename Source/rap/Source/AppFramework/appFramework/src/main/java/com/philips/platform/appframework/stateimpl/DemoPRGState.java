package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import androidx.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.prdemoapp.PRDemoAppuAppDependencies;
import com.philips.platform.prdemoapp.PRDemoAppuAppInterface;
import com.philips.platform.prdemoapp.PRDemoAppuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoPRGState extends DemoBaseState {

    private Context context;

    public DemoPRGState() {
        super(AppStates.TESTPR);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        PRDemoAppuAppInterface uAppInterface = getPrDemoAppuAppInterface();
        uAppInterface.init(new PRDemoAppuAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra()), new PRDemoAppuAppSettings(context.getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(context, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);// pass launch input if required

    }

    @NonNull
    protected PRDemoAppuAppInterface getPrDemoAppuAppInterface() {
        return new PRDemoAppuAppInterface();
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }
}
