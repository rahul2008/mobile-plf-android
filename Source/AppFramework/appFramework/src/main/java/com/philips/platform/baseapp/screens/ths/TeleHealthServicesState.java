package com.philips.platform.baseapp.screens.ths;


import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class TeleHealthServicesState extends BaseState{
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput PTHMicroAppLaunchInput;
    private THSMicroAppInterfaceImpl PTHMicroAppInterface;

    public TeleHealthServicesState() {
        super(AppStates.THS);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher)uiLauncher;
        launchTHS();
    }

    private void launchTHS() {

        PTHMicroAppLaunchInput = new THSMicroAppLaunchInput("Launch Uapp Input");
        PTHMicroAppInterface = new THSMicroAppInterfaceImpl();
        PTHMicroAppInterface.init(new THSMicroAppDependencies(((AppFrameworkApplication)
                fragmentLauncher.getFragmentActivity().getApplicationContext()).getAppInfra()), new THSMicroAppSettings(fragmentLauncher.getFragmentActivity().getApplicationContext()));
        PTHMicroAppInterface.launch(fragmentLauncher, PTHMicroAppLaunchInput);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
