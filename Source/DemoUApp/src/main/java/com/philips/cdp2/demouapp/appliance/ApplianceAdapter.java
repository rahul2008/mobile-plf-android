/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.philips.cdp2.commlib.core.appliance.Appliance;

public class ApplianceAdapter extends ArrayAdapter<Appliance> {

    public ApplianceAdapter(final @NonNull Context context) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1);
    }

    @NonNull
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Appliance appliance = getItem(position);

        ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
        ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));

        return view;
    }
}
