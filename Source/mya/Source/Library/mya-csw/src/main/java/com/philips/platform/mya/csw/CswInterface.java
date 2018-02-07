/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw;

import java.util.List;

import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.mya.csw.injection.AppInfraModule;
import com.philips.platform.mya.csw.injection.CswComponent;
import com.philips.platform.mya.csw.injection.CswModule;
import com.philips.platform.mya.csw.injection.DaggerCswComponent;
import com.philips.platform.mya.csw.permission.PermissionView;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CswInterface implements UappInterface {
    private static final CswInterface reference = new CswInterface();

    public static CswInterface get() {
        return reference;
    }

    private UiLauncher uiLauncher;
    private CswLaunchInput cswLaunchInput;
    private CswDependencies uappDependencies;

    /**
     * Entry point for User registration. Please make sure no User registration components are being used before CswInterface$init.
     *
     * @param uappDependencies
     *            - With an AppInfraInterface instance.
     * @param uappSettings
     *            - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        if (!(uappDependencies instanceof CswDependencies)) {
            throw new IllegalStateException("Illegal state! Must be instance of CswDependencies");
        }

        CswDependencies ourDeps = (CswDependencies) uappDependencies;

        cswComponent = initDaggerComponents(uappDependencies, uappSettings);
        this.uappDependencies = (CswDependencies) uappDependencies;

        reference.uappDependencies = ourDeps;

        CswLogger.init();
        CswLogger.enableLogging();
    }

    /**
     * Launches the CswInterface interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher
     *            - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput
     *            - CswLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        this.uiLauncher = uiLauncher;
        this.cswLaunchInput = (CswLaunchInput) uappLaunchInput;
        launch();
    }

    private void launch() {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), cswLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, cswLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, CswLaunchInput uappLaunchInput) {
        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().getSupportFragmentManager();
            // CswFragment cswFragment = new CswFragment();
            // cswFragment.setOnUpdateTitleListener(fragmentLauncher.getActionbarListener());
            // if (cswFragment.getArguments() == null) {
            // cswFragment.setArguments(new Bundle());
            // }
            // cswFragment.getArguments().putBoolean(CswFragment.BUNDLE_KEY_ADDTOBACKSTACK, uappLaunchInput.isAddtoBackStack());
            //
            // FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // if (uappLaunchInput.isAddtoBackStack()) {
            // fragmentTransaction.addToBackStack(CswConstants.CSWFRAGMENT);
            // }
            //
            // fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
            // cswFragment,
            // CswConstants.CSWFRAGMENT);
            // fragmentTransaction.commitAllowingStateLoss();
            // reference.cswLaunchInput = uappLaunchInput;

            PermissionView permissionFragment = new PermissionView();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(PermissionView.TAG);
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), permissionFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {

        }
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, CswLaunchInput uappLaunchInput) {
        if (null != uiLauncher && uappLaunchInput != null) {
            Intent cswIntent = new Intent(uappLaunchInput.getContext(), CswActivity.class);
            cswIntent.putExtra(CswConstants.DLS_THEME, uiLauncher.getUiKitTheme());
            cswIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            uappLaunchInput.getContext().startActivity(cswIntent);
            reference.cswLaunchInput = uappLaunchInput;
        }
    }

    private CswComponent initDaggerComponents(UappDependencies uappDependencies, UappSettings uappSettings) {

        List<ConsentConfiguration> consentConfigurationList = ((CswDependencies) uappDependencies).getConsentConfigurationList();

        return DaggerCswComponent.builder()
                .cswModule(new CswModule(uappSettings.getContext(), consentConfigurationList))
                .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra())).build();
    }

    public static CswComponent getCswComponent() {
        return cswComponent;
    }

    private static CswComponent cswComponent;

    public CswDependencies getDependencies() {
        return uappDependencies;
    }
}
