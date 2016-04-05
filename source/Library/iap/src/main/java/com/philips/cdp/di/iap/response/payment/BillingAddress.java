package com.philips.cdp.di.iap.response.payment;

import java.io.Serializable;

public class BillingAddress implements Serializable{
    private static final long serialVersionUID = -8630783510798816126L;
    private Country country;
    private String firstName;
    private String id;
    private String lastName;
    private String line1;
    private String line2;
    private String postalCode;
    private String town;

    public Country getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
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
