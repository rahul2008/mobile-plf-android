/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.adapter.BaseDeviceAdapter;
import com.philips.pins.shinelib.SHNDevice;

import java.util.List;

public class AssociatedDeviceAdapter extends BaseDeviceAdapter<SHNDevice> {

    public AssociatedDeviceAdapter(List<SHNDevice> items) {
        super(items);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bll_listitem_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseDeviceAdapter.DeviceViewHolder holder, final int position) {
        final SHNDevice device = mItems.get(position);

        holder.addressView.setVisibility(View.VISIBLE);
        holder.addressView.setText(device.getAddress());

        holder.nameView.setVisibility(View.VISIBLE);
        holder.nameView.setText(device.getName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public SHNDevice getItem(int position) {
        return mItems.get(position);
    }
}
