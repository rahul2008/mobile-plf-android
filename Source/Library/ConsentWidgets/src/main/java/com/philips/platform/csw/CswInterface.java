package com.philips.platform.csw;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.csw.injection.AppInfraModule;
import com.philips.platform.csw.injection.CswComponent;
import com.philips.platform.csw.injection.CswModule;
import com.philips.platform.csw.injection.DaggerCswComponent;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CswInterface implements UappInterface {


    /**
     * Launches the CswInterface interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - CswLaunchInput
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

        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            CswFragment cswFragment = new CswFragment();
            cswFragment.setOnUpdateTitleListener(fragmentLauncher.
                    getActionbarListener());

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    cswFragment,
                    CswConstants.CSWFRAGMENT).addToBackStack(CswConstants.CSWFRAGMENT);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {

        }


    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

    }

    /**
     * Entry point for User registration. Please make sure no User registration components are being used before CswInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        cswComponent = initDaggerCoponents(uappDependencies, uappSettings);
    }

    private CswComponent initDaggerCoponents(UappDependencies uappDependencies, UappSettings uappSettings) {
        return DaggerCswComponent.builder()
                .cswModule(new CswModule(uappSettings.getContext()))
                .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra())).build();
    }

    public static CswComponent getCswComponent() {
        return cswComponent;
    }

    private static CswComponent cswComponent;


}
