/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

public class IAPConfig {
    int themeIndex;
    String language;
    String country;

    public IAPConfig(final String mLanguage, final String mCountry, final int mThemeIndex) {
        this.themeIndex = mThemeIndex;
        this.language = mLanguage;
        this.country = mCountry;
    }
}
