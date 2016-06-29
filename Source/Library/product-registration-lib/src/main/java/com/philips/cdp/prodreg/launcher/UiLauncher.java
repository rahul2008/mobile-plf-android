package com.philips.cdp.prodreg.launcher;

import com.philips.cdp.prodreg.register.Product;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiLauncher {

    protected int mEnterAnimation;
    protected int mExitAnimation;
    private boolean isFirstLaunch;
    private ArrayList<Product> regProdList;

    public UiLauncher() {
    }

    public ArrayList<Product> getRegProdList() {
        return regProdList;
    }

    public void setRegProdList(final ArrayList<Product> regProdList) {
        this.regProdList = regProdList;
    }

    public int getEnterAnimation() {
        return this.mEnterAnimation;
    }

    public int getExitAnimation() {
        return this.mExitAnimation;
    }

    public void setAnimation(int enterAnimation, int exitAnimation) {
        this.mEnterAnimation = enterAnimation;
        this.mExitAnimation = exitAnimation;
    }

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(final boolean firstLaunch) {
        this.isFirstLaunch = firstLaunch;
    }

}
