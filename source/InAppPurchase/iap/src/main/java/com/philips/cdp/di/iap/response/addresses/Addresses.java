/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.addresses;

public class Addresses {
    private Country country;
    private String firstName;
    private String id;
    private String lastName;
    private String line1;
    private String line2;
    private String postalCode;
    private String town;

    public String getVisibleInAddressBook() {
        return visibleInAddressBook;
    }

    public void setVisibleInAddressBook(final String visibleInAddressBook) {
        this.visibleInAddressBook = visibleInAddressBook;
    }

    private String visibleInAddressBook;

    public boolean isShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final boolean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    private boolean shippingAddress;

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    private String phone;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(final String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    private String formattedAddress;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    private String title;

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
