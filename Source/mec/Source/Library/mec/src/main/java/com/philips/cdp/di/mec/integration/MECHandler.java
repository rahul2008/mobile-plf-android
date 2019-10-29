/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.mec.activity.MECFragmentLauncher;
import com.philips.cdp.di.mec.activity.MECLauncherActivity;
import com.philips.cdp.di.mec.screens.MecBaseFragment;
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment;
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

        Bundle bundle =  getBundle();
        bundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        intent.putExtras(bundle);
        mMECSetting.getContext().startActivity(intent);

    }

    protected void launchMECasFragment() {


        FragmentLauncher fragmentLauncher = (FragmentLauncher) mUiLauncher;
        Bundle bundle =  getBundle();
      //  bundle.putSerializable("LaunchInput", (UappLaunchInput) mLaunchInput);
        bundle.putInt("fragment_container", fragmentLauncher.getParentContainerResourceID()); // frame_layout for fragment
        loadDecisionFragment(bundle);


    }

     void loadDecisionFragment(Bundle bundle ){
        MECFragmentLauncher mecFragmentLauncher = new MECFragmentLauncher();
         mecFragmentLauncher.setArguments(bundle);

         FragmentLauncher fragmentLauncher = (FragmentLauncher)mUiLauncher;
         mecFragmentLauncher.setActionBarListener(fragmentLauncher.getActionbarListener(), mLaunchInput.getMecListener());
         String tag = mecFragmentLauncher.getClass().getName();
         FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
         transaction.replace(fragmentLauncher.getParentContainerResourceID(), mecFragmentLauncher, tag);
       //  transaction.addToBackStack(tag);
         transaction.commitAllowingStateLoss();

    }

    Bundle getBundle (){
        Bundle mBundle = new Bundle();
        if (mLaunchInput.mMECFlowInput != null) {
            if (mLaunchInput.mMECFlowInput.getProductCTN() != null) {
                mBundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                        mLaunchInput.mMECFlowInput.getProductCTN());
            }
            if (mLaunchInput.mMECFlowInput.getProductCTNs() != null) {
                mBundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS,
                        mLaunchInput.mMECFlowInput.getProductCTNs());
            }
            mBundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, mLaunchInput.getIgnoreRetailers());
        }
        return mBundle;
    }


}
