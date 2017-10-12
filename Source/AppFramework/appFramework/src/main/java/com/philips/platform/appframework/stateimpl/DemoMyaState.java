package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.demouapp.MyAccountDemoUAppInterface;
import com.philips.platform.mya.demouapp.activity.MyAccountDependencies;
import com.philips.platform.mya.demouapp.activity.MyAccountSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DemoMyaState extends DemoBaseState {

    private MyAccountDemoUAppInterface myaDemoAppuAppInterface;
    private Context mContext;

    public DemoMyaState() {
        super(AppStates.TESTMYACCOUNTSERVICE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        myaDemoAppuAppInterface.launch(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,
                        getDLSThemeConfiguration(mContext.getApplicationContext()), 0, null), null);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        MyAccountSettings myAccountSettings = new MyAccountSettings(context.getApplicationContext());
        myaDemoAppuAppInterface = new MyAccountDemoUAppInterface();
        myaDemoAppuAppInterface.init(getUappDependencies(context), myAccountSettings);
    }

    @Override
    public void updateDataModel() {

    }

    @NonNull
    protected MyAccountDependencies getUappDependencies(Context context) {
        return new MyAccountDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra());
    }
}
