package com.philips.platform.csw;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.csw.injection.AppInfraModule;
import com.philips.platform.csw.injection.CswComponent;
import com.philips.platform.csw.injection.CswModule;
import com.philips.platform.csw.injection.DaggerCswComponent;
import com.philips.platform.csw.utils.CswLogger;
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
            launchAsActivity(((ActivityLauncher) uiLauncher), (CswLaunchInput) uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, (CswLaunchInput) uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher,
                                  CswLaunchInput uappLaunchInput) {

        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            CswFragment cswFragment = new CswFragment();
            cswFragment.setOnUpdateTitleListener(fragmentLauncher.
                    getActionbarListener());
            cswFragment.setArguments(uappLaunchInput.getApplicationName(), uappLaunchInput.getPropositionName(),uappLaunchInput.isAddtoBackStack());

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            if (uappLaunchInput.isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(CswConstants.CSWFRAGMENT);
            }

            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    cswFragment,
                    CswConstants.CSWFRAGMENT);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {

        }

    }

    private void launchAsActivity(ActivityLauncher uiLauncher, CswLaunchInput uappLaunchInput) {
        if (null != uiLauncher && uappLaunchInput != null) {
            Intent cswIntent = new Intent(((CswLaunchInput) uappLaunchInput).getContext(), CswActivity.class);
            cswIntent.putExtra(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, uappLaunchInput.getApplicationName());
            cswIntent.putExtra(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, uappLaunchInput.getPropositionName());
            cswIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            uappLaunchInput.getContext().startActivity(cswIntent);
        }
    }

    /**
     * Entry point for User registration. Please make sure no User registration components are being used before CswInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {

        initConsentToolKit(uappDependencies, uappSettings);
        cswComponent = initDaggerCoponents(uappDependencies, uappSettings);
        CswLogger.init();
        CswLogger.enableLogging();
    }

    private void initConsentToolKit(UappDependencies uappDependencies, UappSettings uappSettings) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(uappSettings.getContext());
        catkInputs.setAppInfra(uappDependencies.getAppInfra());
        catkInputs.setApplicationName(((CswDependencies) uappDependencies).getApplicationName());
        catkInputs.setPropositionName(((CswDependencies) uappDependencies).getPropositionName());
        ConsentAccessToolKit.getInstance().init(catkInputs);
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
