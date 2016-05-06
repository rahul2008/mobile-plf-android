package com.philips.cdp.di.iap.response.placeorder;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryAddressEntity {
    private CountryEntity country;
    private String id;
    private String line1;
    private String line2;
    private String postalCode;
    private String town;

    public CountryEntity getCountry() {
        return country;
    }

    public String getId() {
        return id;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTown() {
        return town;
    }
}
