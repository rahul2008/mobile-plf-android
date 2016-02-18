/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;

public class ShippingAddressFragment extends BaseNoAnimationFragment implements AddressController.AddressListener{

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shipping_address_layout, container, false);
        return rootView;
    }

    @Override
    public void onFinish() {

    }

}
