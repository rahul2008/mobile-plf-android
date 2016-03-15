/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.address;

import java.io.Serializable;

public class AddressFields implements Serializable {

    private static final long serialVersionUID = 8533528348900570462L;

    private String firstName;
    private String lastName;
    private String titleCode;
    private String countryIsocode;
    private String email;
    private String line1;
    private String line2;
    private String postalCode;
    private String town;
    private String phoneNumber;
    private String state;

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getCountryIsocode() {
        return countryIsocode;
    }

    public void setCountryIsocode(String countryIsocode) {
        this.countryIsocode = countryIsocode;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
