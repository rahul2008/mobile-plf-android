package com.philips.cdp.prodreg.launcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiLauncher {

    protected int mEnterAnimation;
    protected int mExitAnimation;
    private boolean isFirstLaunch;

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

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(final boolean firstLaunch) {
        this.isFirstLaunch = firstLaunch;
    }
}
