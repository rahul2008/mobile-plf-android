/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

public class IAPSettings {
    private int themeIndex;
    private String language;
    private String country;

    public IAPSettings(final String country, final String language, final int themeIndex) {
        this.themeIndex = themeIndex;
        this.language = language;
        this.country = country;
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }
}
