/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.associate;

import com.example.cdpp.bluelibexampleapp.device.BaseDeviceAdapter;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;

import java.util.List;

public class DeviceDefinitionAdapter extends BaseDeviceAdapter<SHNDeviceDefinitionInfo> {

    public DeviceDefinitionAdapter(List<SHNDeviceDefinitionInfo> items) {
        super(items);
    }

    @Override
    public void onBindViewHolder(BaseDeviceAdapter.DeviceViewHolder holder, final int position) {
        final SHNDeviceDefinitionInfo deviceDefinition = mItems.get(position);

        holder.nameView.setText(deviceDefinition.getDeviceTypeName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    protected SHNDeviceDefinitionInfo getItem(int position) {
        return mItems.get(position);
    }
}
