/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    private FragmentTransaction fragmentTransaction;
    int containerId;
    public abstract int getContainerId();

    public void handleFragmentBackStack(Fragment fragment, String fragmentTag, int fragmentAddState) {
        containerId = getContainerId();
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (fragmentAddState) {
                case Constants.ADD_HOME_FRAGMENT:

                    if (null == getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG)) {
                        addToBackStack(containerId, fragment, fragmentTag);
                    } else {
                        getSupportFragmentManager().popBackStackImmediate(HomeFragment.TAG, 0);
                    }

                    break;
                case Constants.ADD_FROM_HAMBURGER:

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId,new HomeFragment(),HomeFragment.TAG);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addToBackStack(containerId, fragment, fragmentTag);

                    break;
                case Constants.CLEAR_TILL_HOME:

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId,new HomeFragment(),HomeFragment.TAG);

                    break;
            }
        }catch (Exception e){

        }
    }

    public void addFragment(Fragment fragment, String fragmentTag){
        containerId = getContainerId();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId,fragment,fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }
    private void addToBackStack(int containerID, Fragment fragment,String fragmentTag){
        fragmentTransaction.replace(containerID,fragment,fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public abstract void updateActionBarIcon(boolean b);
}
