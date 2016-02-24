package com.philips.cdp.productselection.launchertype;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;


/**
 * @author naveen@philips.com
 * @date 24/january/2016
 */
public class UiLauncher {

    /**
     * Enter {@Link android.view.animation} of the ProductSelection Component Screens
     */
    protected int mEnterAnimation;


    /**
     * Exit {@Link android.view.animation} of the ProductSelection Component Screens
     */
    protected int mExitAnimation;


    public int getEnterAnimation() {
        return mEnterAnimation;
    }

    public int getExitAnimation() {
        return mExitAnimation;
    }


    public void setAnimation(int enterAnimation, int exitAnimation) {
        this.mEnterAnimation = enterAnimation;
        this.mExitAnimation = exitAnimation;
    }
}
