/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

import java.util.ArrayList;

/**
 * Adapter for list of ble ref node
 */

public class BleDeviceListAdapter extends BaseAdapter {
    private ArrayList<RefAppBleReferenceAppliance> mLeDevices;
    private LayoutInflater mInflator;
    private Context context;

    public BleDeviceListAdapter(Context context, @NonNull ArrayList<RefAppBleReferenceAppliance> refAppBleReferenceApplianceList) {
        super();
        this.context=context;
        mLeDevices = refAppBleReferenceApplianceList;
        mInflator = LayoutInflater.from(context);
    }

    public void addDevice(@NonNull RefAppBleReferenceAppliance device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        RefAppBleReferenceAppliance device = (RefAppBleReferenceAppliance) getItem(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.RA_unknown_device);
        viewHolder.deviceAddress.setText(device.getNetworkNode().getCppId());
        view.setTag(R.string.RA_ble_ref_device, device);
        return view;
    }


    static class ViewHolder {
    TextView deviceName;
    TextView deviceAddress;
    }
}
