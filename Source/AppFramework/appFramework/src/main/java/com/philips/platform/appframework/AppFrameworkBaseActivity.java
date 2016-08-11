/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.appframework.homescreen.HomeFragment;
import com.philips.platform.appframework.inapppurchase.InAppPurchasesFragment;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * AppFrameworkBaseActivity is the App level settings class for controlling the behavior of apps.
 */
public abstract class AppFrameworkBaseActivity extends UiKitActivity{
    private FragmentManager fragmentManager = null;
    public UIBasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ProductSelectionLogger.i(Constants.ACTIVITY, "onCreate");
        fragmentManager = getSupportFragmentManager();
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        int containerId = R.id.frame_container;

            try {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerId, fragment, fragmentTag);
                fragmentTransaction.addToBackStack(fragmentTag);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
    }

    public void popBackTillHomeFragment() {
        getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG,0);
    }

    boolean isLaunchedFromHamburgerMenu(String tag){
        if(tag.equalsIgnoreCase(SettingsFragment.TAG) || tag.equalsIgnoreCase(InAppPurchasesFragment.TAG) || tag.equalsIgnoreCase(DebugTestFragment.TAG)){
            return true;
        }
        return false;
    }

    public void popBack(){
        FragmentManager.BackStackEntry backEntry=getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1);
        String str=backEntry.getName();
        if(str!=null && isLaunchedFromHamburgerMenu(str)){
            popBackTillHomeFragment();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected boolean backstackFragment() {
        fragmentManager.popBackStack();
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    public void removeFragmentByTag(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentByTag(tag);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    public void finishActivity() {
        this.finishAffinity();
    }

    protected boolean findFragmentByTag(String tag) {
        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag(tag);

        return (currentFrag != null);
    }

}
