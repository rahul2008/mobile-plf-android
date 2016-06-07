package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegConnectionFragment extends ProdRegBaseFragment {
    @Override
    public String getActionbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_connection, container, false);
        return view;
    }
}
