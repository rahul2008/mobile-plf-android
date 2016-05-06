package com.philips.cdp.di.iap.address;

import android.os.Message;

import com.philips.cdp.di.iap.controller.AddressController;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockAddressListener implements AddressController.AddressListener {
    @Override
    public void onGetAddress(final Message msg) {
    }

    @Override
    public void onCreateAddress(final Message msg) {
    }

    @Override
    public void onSetDeliveryAddress(final Message msg) {
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
    }

    @Override
    public void onGetRegions(final Message msg) {

    }
}
