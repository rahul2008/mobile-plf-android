/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.addresses;

import com.philips.cdp.di.iap.session.NetworkConstants;

public class Addresses {
    private Country country;
    private String firstName;
    private String id;
    private String lastName;
    private String line1;
    private String line2;
    private String phone;
    private String postalCode;
    private boolean shippingAddress;
    private String titleCode;
    private String town;
    private String email;
    private String state;
    private String formattedAddress;
    private Region region;
    private boolean visibleInAddressBook;

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }

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
        if (line2 == null) {
            line2 = NetworkConstants.EMPTY_RESPONSE;
        }
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
        if (titleCode == null) {
            titleCode = NetworkConstants.EMPTY_RESPONSE;
        }
        return titleCode;
    }

    public String getTown() {
        return town;
    }

    public boolean isVisibleInAddressBook() {
        return visibleInAddressBook;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public Region getRegion() {
        return region;
    }
}
