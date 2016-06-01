package com.philips.cdp.prodreg.ui;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiLauncher {

    protected int mEnterAnimation;
    protected int mExitAnimation;

    public UiLauncher() {
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
}
