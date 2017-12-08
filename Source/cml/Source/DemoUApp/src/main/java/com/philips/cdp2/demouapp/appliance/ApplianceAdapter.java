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
import com.philips.cdp2.commlib.demouapp.R;

public class ApplianceAdapter extends ArrayAdapter<Appliance> {

    public ApplianceAdapter(final @NonNull Context context) {
        super(context, R.layout.cml_appliance_list_item, R.id.appliance_name);
    }

    @NonNull
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Appliance appliance = getItem(position);

        ((TextView) view.findViewById(R.id.appliance_name)).setText(String.format("%s (%s)", appliance.getName(), appliance.getDeviceType()));
        ((TextView) view.findViewById(R.id.appliance_cpp_id)).setText(appliance.getNetworkNode().getCppId());
        ((TextView) view.findViewById(R.id.appliance_model_id)).setText(appliance.getNetworkNode().getModelId());

        return view;
    }
}
