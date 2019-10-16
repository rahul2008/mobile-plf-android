/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.mec.activity.MECLauncherActivity;
import com.philips.cdp.di.mec.screens.InAppBaseFragment;
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

class MECHandler {
    private MECDependencies mMECDependencies;
    private MECSettings mMECSetting;
    private UiLauncher mUiLauncher;
    private MECLaunchInput mLaunchInput;

    MECHandler(MECDependencies pMECDependencies,MECSettings pMecSettings,UiLauncher pUiLauncher,  MECLaunchInput pLaunchInput) {
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
        ActivityLauncher activityLauncher =  (ActivityLauncher) mUiLauncher;
        Bundle mBundle = new Bundle();
        //mBundle.putSerializable("LaunchInput",(UappLaunchInput)mLaunchInput);
       // mBundle.putSerializable("UILauncher",mUiLauncher);
        mBundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        intent.putExtras(mBundle);
        mMECSetting.getContext().startActivity(intent);


    }

    protected void launchMECasFragment() {

    }





}
