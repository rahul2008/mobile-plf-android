/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.connect;

import android.content.Context;
import android.view.View;

import com.example.cdpp.bluelibexampleapp.R;
import com.example.cdpp.bluelibexampleapp.device.BaseDeviceAdapter;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;

import java.util.List;
import java.util.Locale;

public class ConnectDeviceAdapter extends BaseDeviceAdapter<SHNDeviceFoundInfo> {

    public ConnectDeviceAdapter(List<SHNDeviceFoundInfo> items) {
        super(items);
    }

    @Override
    public void onBindViewHolder(BaseDeviceAdapter.DeviceViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();

        final SHNDeviceFoundInfo deviceFoundInfo = mItems.get(position);
        final SHNDevice device = deviceFoundInfo.getShnDevice();

        holder.rssiView.setVisibility(View.VISIBLE);
        holder.addressView.setVisibility(View.VISIBLE);

        holder.rssiView.setText(String.format(Locale.US, context.getString(R.string.device_detail_rssi), deviceFoundInfo.getRssi()));
        holder.nameView.setText(device.getName() == null ? context.getString(R.string.unknown) : device.getName());
        holder.addressView.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    protected SHNDeviceFoundInfo getItem(int position) {
        return mItems.get(position);
    }
}
