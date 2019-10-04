package com.philips.cdp.di.mec.model;

import java.io.Serializable;

public class Region implements Serializable{
    private static final long serialVersionUID = -8194436630244580653L;

    private String countryIso;
    private String isocode;
    private String isocodeShort;
    private String name;

    public String getCountryIso() {
        return countryIso;
    }

    public String getIsocode() {
        return isocode;
    }

    public String getIsocodeShort() {
        return isocodeShort;
    }

    public String getName() {
        return name;
    }
}
