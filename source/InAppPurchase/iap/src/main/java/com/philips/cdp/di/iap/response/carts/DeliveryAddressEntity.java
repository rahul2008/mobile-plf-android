package com.philips.cdp.di.iap.response.carts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryAddressEntity {
    /**
     * isocode : US
     */

    private CountryEntity country;
    private String firstName;
    private String formattedAddress;
    private String id;
    private String lastName;
    private String line1;
    private String line2;
    private String phone;
    private String postalCode;
    private boolean shippingAddress;
    private String titleCode;
    private String town;
    private boolean visibleInAddressBook;

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setShippingAddress(boolean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setVisibleInAddressBook(boolean visibleInAddressBook) {
        this.visibleInAddressBook = visibleInAddressBook;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFormattedAddress() {
        return formattedAddress;
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

    public String getPhone() {
        return phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public boolean isShippingAddress() {
        return shippingAddress;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public String getTown() {
        return town;
    }

    public boolean isVisibleInAddressBook() {
        return visibleInAddressBook;
    }


}
