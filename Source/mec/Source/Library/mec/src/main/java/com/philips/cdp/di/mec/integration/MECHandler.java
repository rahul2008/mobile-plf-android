/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.di.mec.activity.MECLauncherActivity;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

class MECHandler {
    private MECDependencies mMECDependencies;
    private MECSettings mMECSetting;
    private UiLauncher mUiLauncher;
    private MECLaunchInput mLaunchInput;

    MECHandler(MECDependencies pMECDependencies, MECSettings pMecSettings, UiLauncher pUiLauncher, MECLaunchInput pLaunchInput) {
        this.mMECDependencies = pMECDependencies;
        this.mMECSetting = pMecSettings;
        this.mUiLauncher = pUiLauncher;
        this.mLaunchInput = pLaunchInput;

    }


    void launchMEC() {
        if (mUiLauncher instanceof ActivityLauncher) {
            launchMECasActivity();
        } else {
            launchMECasFragment();
        }
    }


    protected void launchMECasActivity() {
        Intent intent = new Intent(mMECSetting.getContext(), MECLauncherActivity.class);
        intent.putExtra(MECConstant.MEC_LANDING_SCREEN, mLaunchInput.mLandingView);
        ActivityLauncher activityLauncher = (ActivityLauncher) mUiLauncher;
        Bundle mBundle = new Bundle();

        if (mLaunchInput.mMECFlowInput != null) {
            if (mLaunchInput.mMECFlowInput.getProductCTN() != null) {
                intent.putExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                        mLaunchInput.mMECFlowInput.getProductCTN());
            }
            if (mLaunchInput.mMECFlowInput.getProductCTNs() != null) {
                intent.putStringArrayListExtra(MECConstant.CATEGORISED_PRODUCT_CTNS,
                        mLaunchInput.mMECFlowInput.getProductCTNs());
            }
            intent.putExtra(MECConstant.MEC_IGNORE_RETAILER_LIST, mLaunchInput.getIgnoreRetailers());
        }
        mBundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        intent.putExtras(mBundle);
        mMECSetting.getContext().startActivity(intent);

    }

    protected void launchMECasFragment() {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("LaunchInput", (UappLaunchInput) mLaunchInput);
        FragmentLauncher fragmentLauncher = (FragmentLauncher) mUiLauncher;
        mBundle.putInt("fragment_container", fragmentLauncher.getParentContainerResourceID()); // frame_layout for fragment

    }


}
