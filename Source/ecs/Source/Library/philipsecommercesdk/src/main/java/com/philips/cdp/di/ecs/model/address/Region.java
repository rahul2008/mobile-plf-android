package com.philips.cdp.di.ecs.model.address;

import java.io.Serializable;

public class Region implements Serializable{
    private static final long serialVersionUID = -8194436630244580653L;

    private String countryIso;
    private String isocode;

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

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
    public void setIsocodeShort(String isocodeShort) {
        this.isocodeShort = isocodeShort;
    }

    public String getName() {
        return name;
    }
}
