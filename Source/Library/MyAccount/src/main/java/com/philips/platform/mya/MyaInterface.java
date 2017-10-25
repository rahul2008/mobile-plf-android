package com.philips.platform.mya;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.csw.CswConstants;
import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MyaInterface implements UappInterface {
    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher,
                                  UappLaunchInput uappLaunchInput) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        CswInterface cswInterface = new CswInterface();
        cswInterface.launch(fragmentLauncher, cswLaunchInput);
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setContext(((MyaLaunchInput) uappLaunchInput).getContext());
        CswInterface cswInterface = new CswInterface();
        cswInterface.launch(uiLauncher, cswLaunchInput);
/*
        if (null != uiLauncher && uappLaunchInput != null) {
            Intent registrationIntent = new Intent(((MyaLaunchInput) uappLaunchInput).getContext(), MyAccountActivity.class);
            registrationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            ((MyaLaunchInput) uappLaunchInput).getContext().startActivity(registrationIntent);
        }*/
    }

    /**
     * Entry point for User registration. Please make sure no User registration components are being used before MyaInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {

    }
}
