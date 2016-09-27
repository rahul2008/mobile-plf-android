package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.InAppBaseFragment;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by indrajitkumar on 22/09/16.
 */

public class MockIAPHandler extends IAPHandler {
    IAPSettings mIAPSettings;

    MockIAPHandler(IAPDependencies mIAPDependencies, IAPSettings pIapSettings) {
        super(mIAPDependencies, pIapSettings);
        mIAPSettings = pIapSettings;
    }

    @Override
    void initTaggingLogging() {
    }

    @Override
    protected int getNetworkEssentialReqeustCode(boolean useLocalData) {
        return super.getNetworkEssentialReqeustCode(useLocalData);
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
