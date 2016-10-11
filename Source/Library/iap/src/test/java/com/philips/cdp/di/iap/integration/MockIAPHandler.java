/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.iapHandler.IAPExposedAPI;
import com.philips.cdp.di.iap.screens.InAppBaseFragment;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class MockIAPHandler extends IAPHandler {

    MockIAPHandler(IAPDependencies mIAPDependencies, IAPSettings pIapSettings) {
        super(mIAPDependencies, pIapSettings);
    }

    @Override
    void initPreRequisite() {
    }

    @Override
    protected void setLangAndCountry() {
        super.setLangAndCountry();
    }

    @Override
    protected IAPExposedAPI getExposedAPIImplementor() {
        return super.getExposedAPIImplementor();
    }

    @Override
    void launchIAP(UiLauncher uiLauncher, IAPLaunchInput pLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(new Application(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }

    @Override
    protected void addFragment(InAppBaseFragment newFragment, FragmentLauncher fragmentLauncher, IAPListener iapListener) {
        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), iapListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
    }
}
