package com.philips.cdp.di.ecs.model.cart;


import com.philips.cdp.di.ecs.model.address.Country;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryAddressEntity  implements Serializable {

    private Country country;
    private String firstName;
    private String formattedAddress;
    private String id;
    private String lastName;
    private String line1;
    private String houseNumber;
    private String line2;
    private String phone;
    private String postalCode;
    private boolean shippingAddress;
    private String titleCode;
    private String town;
    private boolean visibleInAddressBook;

    public Country getCountry() {
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

    public String getHouseNumber() {
        return houseNumber;
    }
}
