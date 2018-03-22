package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.Settings;

import java.io.Serializable;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class OrmSettings implements Settings, Serializable {

    public static final long serialVersionUID = 11L;

    private int id;

    private String locale;
    private String unit;
    private String timeZone;

    OrmSettings() {
    }

    public OrmSettings(final String unit, final String locale, final String timeZone) {
        this.unit = unit;
        this.locale = locale;
        this.timeZone = timeZone;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public void setUnit(String unit) {
       this.unit=unit;
    }

    @Override
    public void setLocale(String locale) {
       this.locale=locale;
    }

    @Override
    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setID(int id) {

    }

    @Override
    public String toString() {
        return "" + unit + " " + locale + " " + timeZone;
    }
}
