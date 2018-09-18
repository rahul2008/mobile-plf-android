package com.philips.cdp.di.iap.screens;


import com.philips.cdp.di.iap.address.AddressFields;

public class DLSAddressPresenter {

    private final DLSAddressContractor addressContractor;

    public DLSAddressPresenter(DLSAddressContractor addressContractor) {

        this.addressContractor = addressContractor;
    }

    public DLSAddressContractor getAddressContractor() {
        return addressContractor;
    }

    public void setContinueButtonState(boolean b) {
        addressContractor.setContinueButtonState(b);
    }

    public void setBillingAddressFields(AddressFields addressFields) {
        addressContractor.setBillingAddressFields(addressFields);
    }
}