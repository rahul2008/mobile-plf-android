/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.baseapp.screens.homefragment.HomeFragment;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.List;

/**
 * AppFrameworkBaseActivity is the App level settings class for controlling the behavior of apps.
 */
public abstract class AppFrameworkBaseActivity extends UiKitActivity implements ActionBarListener {
    private static final String TAG = AppFrameworkBaseActivity.class.getName();

    public UIBasePresenter presenter;
    //  private int cartItemCount = 0;
    int containerId;
    private FragmentTransaction fragmentTransaction;

    public abstract int getContainerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.init(new ThemeConfiguration(this,ContentColor.ULTRA_LIGHT, NavigationColor.ULTRA_LIGHT));
        getTheme().applyStyle(R.style.Theme_Philips_DarkBlue_NoActionBar, true);
        super.onCreate(savedInstanceState);
    }

    public void handleFragmentBackStack(Fragment fragment, String fragmentTag, int fragmentAddState) {
        RALog.d(TAG," handleFragmentBackStack called");
        containerId = getContainerId();
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (fragmentAddState) {
                case Constants.ADD_HOME_FRAGMENT:
                    RALog.d(TAG," Added as ADD_HOME_FRAGMENT");
                    if (null == getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG)) {
                        addToBackStack(containerId, fragment, fragmentTag);
                    } else {
                        getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG, 0);
                    }

                    break;
                case Constants.ADD_FROM_HAMBURGER:
                    RALog.d(TAG," Added as ADD_FROM_HAMBURGER");

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId, new HomeFragment(), HomeFragment.TAG);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addToBackStack(containerId, fragment, fragmentTag);

                    break;
                case Constants.CLEAR_TILL_HOME:
                    RALog.d(TAG," Added as CLEAR_TILL_HOME");

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId, new HomeFragment(), HomeFragment.TAG);

                    break;
            }
        } catch (Exception e) {
            RALog.e(TAG,e.getMessage());
        }
    }

    public void addFragment(Fragment fragment, String fragmentTag) {
        RALog.d(TAG," addFragment called");

        containerId = getContainerId();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    private void addToBackStack(int containerID, Fragment fragment, String fragmentTag) {
        RALog.d(TAG," addToBackStack called");

        fragmentTransaction.replace(containerID, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    /*public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }*/

    public abstract void updateActionBarIcon(boolean b);

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        RALog.d(TAG," onResume called");

        if (((AppFrameworkApplication) getApplicationContext()).getAppInfra() != null) {
            startCollectingLifecycleData();
            startPushNotificationFlow();
        }
    }

    public void startCollectingLifecycleData() {
        AppFrameworkTagging.getInstance().collectLifecycleData(this);
    }

    public void startPushNotificationFlow() {
        if (!BaseAppUtil.isDSPollingEnabled(this)) {
            BaseFlowManager baseFlowManager = ((AppFrameworkApplication) getApplicationContext()).getTargetFlowManager();
            if (baseFlowManager != null) {
                BaseState currentState = baseFlowManager.getCurrentState();
                if (currentState instanceof DataServicesState) {
                    PushNotificationManager.getInstance().startPushNotificationRegistration(this);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RALog.d(TAG," onPause called");
        AppFrameworkTagging.getInstance().pauseCollectingLifecycleData();
    }

}
