/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.support.v4.app.FragmentManager;

public class IAPSettings {
    private int mThemeIndex;
    private String mLanguage;
    private String mCountry;

    //Add fragment launch support
    private boolean mLaunchAsFragment;
    private int mContainerID;
    private android.support.v4.app.FragmentManager mFragmentManager;

    private boolean mUseLocalData;

    public IAPSettings(final String country, final String language, final int themeIndex) {
        this.mThemeIndex = themeIndex;
        this.mLanguage = language;
        this.mCountry = country;
    }

    public void setUseLocalData(final boolean useLocalData) {
        this.mUseLocalData = useLocalData;
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public int getThemeIndex() {
        return mThemeIndex;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setFragProperties(FragmentManager manager, int containerID) {
        mFragmentManager = manager;
        mContainerID = containerID;
    }

    public boolean isLaunchAsFragment() {
        return mLaunchAsFragment;
    }

    public void setLaunchAsFragment(boolean launchAsFragment) {
        mLaunchAsFragment = launchAsFragment;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public int getContainerID() {
        return mContainerID;
    }
}
