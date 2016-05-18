/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

public class IAPSettings {
    private int mThemeIndex;
    private String mLanguage;
    private String mCountry;

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
}
