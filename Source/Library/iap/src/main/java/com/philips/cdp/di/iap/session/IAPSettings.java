/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

public class IAPSettings {
    int themeIndex;
    String language;
    String country;

    public IAPSettings(final String country, final String language, final int themeIndex) {
        this.themeIndex = themeIndex;
        this.language = language;
        this.country = country;
    }
}
