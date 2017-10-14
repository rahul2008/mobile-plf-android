package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.demouapp.MyaDemouAppInterface;
import com.philips.platform.mya.demouapp.MyaDemouAppDependencies;
import com.philips.platform.mya.demouapp.MyaDemouAppLaunchInput;
import com.philips.platform.mya.demouapp.MyaDemouAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DemoMyaState extends DemoBaseState {

    private MyaDemouAppInterface myaDemoAppuAppInterface;
    private Context mContext;

    public DemoMyaState() {
        super(AppStates.TESTMYACCOUNTSERVICE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        MyaDemouAppLaunchInput launchInput = new MyaDemouAppLaunchInput();
        launchInput.setContext(mContext);
        myaDemoAppuAppInterface.launch(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,
                        getDLSThemeConfiguration(mContext.getApplicationContext()), 0, null), launchInput);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        MyaDemouAppSettings myAccountSettings = new MyaDemouAppSettings(context.getApplicationContext());
        myaDemoAppuAppInterface = getMyAccountDemoUAppInterface();
        myaDemoAppuAppInterface.init(getUappDependencies(context), myAccountSettings);
    }

    @NonNull
    protected MyaDemouAppInterface getMyAccountDemoUAppInterface() {
        return new MyaDemouAppInterface();
    }

    @Override
    public void updateDataModel() {

    }

    @NonNull
    protected MyaDemouAppDependencies getUappDependencies(Context context) {
        return new MyaDemouAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra());
    }
}
