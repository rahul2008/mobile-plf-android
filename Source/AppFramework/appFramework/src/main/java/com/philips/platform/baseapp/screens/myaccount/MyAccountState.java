package com.philips.platform.baseapp.screens.myaccount;


import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.MyaDependencies;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class MyAccountState extends BaseState {

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        FragmentLauncher fragmentLauncher = (FragmentLauncher)uiLauncher;
        Context actContext = fragmentLauncher.getFragmentActivity();

        ((AbstractAppFrameworkBaseActivity)actContext).handleFragmentBackStack(null,null,getUiStateData().getFragmentLaunchState());

        MyaLaunchInput launchInput = new MyaLaunchInput();
        launchInput.setContext(actContext);
        MyaInterface myaInterface = new MyaInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    @Override
    public void init(Context context) {
    }


    @Override
    public void updateDataModel() {

    }

    @NonNull
    protected MyaDependencies getUappDependencies(Context actContext) {
        return new MyaDependencies(((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra());
    }
}
