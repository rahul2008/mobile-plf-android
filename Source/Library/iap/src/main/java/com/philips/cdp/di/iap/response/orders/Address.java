package com.philips.cdp.di.iap.response.orders;

public class Address {

    private Country country;
    private String firstName;
    private String id;
    private String lastName;
    private String line1;
    private String line2;
    private String phone1;
    private String postalCode;
    private Region region;
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

    public String getPhone1() {
        return phone1;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Region getRegion() {
        return region;
    }

    public String getTown() {
        return town;
    }

}
