package com.philips.platform.datasync.settings;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UCoreSettings {

    private String locale,unitSystem;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(String unitSystem) {
        this.unitSystem = unitSystem;
    }
}
