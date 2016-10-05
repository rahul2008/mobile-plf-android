/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appframework.homescreen.HomeFragment;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * AppFrameworkBaseActivity is the App level settings class for controlling the behavior of apps.
 */
public abstract class AppFrameworkBaseActivity extends UiKitActivity implements ActionBarListener {
    public UIBasePresenter presenter;
    private int cartItemCount = 0;

    public void handleFragmentBackStack(Fragment fragment, String fragmentTag, int fragmentAddState) {
        int containerId = R.id.frame_container;
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (fragmentAddState) {
                case Constants.ADD_HOME_FRAGMENT:
                    if (null == getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG)) {
                        fragmentTransaction.replace(containerId, fragment, fragmentTag);
                        fragmentTransaction.addToBackStack(fragmentTag);
                        fragmentTransaction.commitAllowingStateLoss();
                    } else {
                        getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG, 0);
                    }
                    break;
                case Constants.ADD_FROM_HAMBURGER:
                    getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG, 0);
                    fragmentTransaction.replace(containerId, fragment, fragmentTag);
                    fragmentTransaction.addToBackStack(fragmentTag);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
                case Constants.CLEAR_TILL_HOME:
                    getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG, 0);
                    break;
            }
        }catch (Exception e){

        }
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public abstract void updateActionBarIcon(boolean b);
}
