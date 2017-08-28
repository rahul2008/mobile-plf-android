/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.uappdemo.screens.homefragment.HomeFragmentU;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * UappBaseActivity is the App level settings class for controlling the behavior of apps.
 */
public abstract class UappBaseActivity extends UiKitActivity implements ActionBarListener {
    public UappBasePresenter presenter;
  //  private int cartItemCount = 0;
    int containerId;
    private FragmentTransaction fragmentTransaction;

    public abstract int getContainerId();

    public void handleFragmentBackStack(Fragment fragment, String fragmentTag, int fragmentAddState) {
        containerId = getContainerId();
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (fragmentAddState) {
                case UappConstants.ADD_HOME_FRAGMENT:

                    if (null == getSupportFragmentManager().findFragmentByTag(HomeFragmentU.TAG)) {
                        addToBackStack(containerId, fragment, fragmentTag);
                    } else {
                        getSupportFragmentManager().popBackStackImmediate(HomeFragmentU.TAG, 0);
                    }

                    break;
                case UappConstants.ADD_FROM_HAMBURGER:

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId,new HomeFragmentU(), HomeFragmentU.TAG);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addToBackStack(containerId, fragment, fragmentTag);

                    break;
                case UappConstants.CLEAR_TILL_HOME:

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    addToBackStack(containerId,new HomeFragmentU(), HomeFragmentU.TAG);

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
    private void addToBackStack(int containerID, Fragment fragment, String fragmentTag){
        fragmentTransaction.replace(containerID,fragment,fragmentTag);
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
}
