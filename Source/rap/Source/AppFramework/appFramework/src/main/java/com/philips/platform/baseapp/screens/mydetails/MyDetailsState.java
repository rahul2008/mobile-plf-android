package com.philips.platform.baseapp.screens.mydetails;

import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class MyDetailsState extends BaseState {

    private FragmentLauncher fragmentLauncher;
    public MyDetailsState() {
        super(AppStates.MY_DETAILS_STATE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.USER_DETAILS);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher,urLaunchInput);

    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
