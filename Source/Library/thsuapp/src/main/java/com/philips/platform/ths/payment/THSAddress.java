package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;

/**
 * Created by philips on 7/24/17.
 */

public class THSAddress {
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    Address address;

}
