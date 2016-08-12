package com.philips.cdp.di.iap.integration;

import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * Created by Apple on 12/08/16.
 */
public class IAPDependencies extends UappDependencies {
    private String language;
    private String country;

    public IAPDependencies(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }
}
