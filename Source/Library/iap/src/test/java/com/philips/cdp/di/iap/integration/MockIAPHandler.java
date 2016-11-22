/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.screens.InAppBaseFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class MockIAPHandler extends IAPHandler {

    MockIAPHandler(IAPDependencies mIAPDependencies, IAPSettings pIapSettings) {
        super(mIAPDependencies, pIapSettings);
    }

    @Override
    void initPreRequisite() {
    }

    /*@Override
    void launchIAP(UiLauncher uiLauncher, IAPLaunchInput pLaunchInput) {
        verifyInput(pLaunchInput.mLandingView, pLaunchInput.mIAPFlowInput);

        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(new Application(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }*/

    @Override
    protected void addFragment(InAppBaseFragment newFragment, FragmentLauncher fragmentLauncher, IAPListener iapListener) {
        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), iapListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
    }
}
