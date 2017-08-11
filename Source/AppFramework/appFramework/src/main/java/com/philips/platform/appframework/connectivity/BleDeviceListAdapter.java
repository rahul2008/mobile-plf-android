package com.philips.platform.appframework.connectivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

import java.util.ArrayList;

/**
 * Created by philips on 11/08/17.
 */

public class BleDeviceListAdapter extends BaseAdapter {
    private ArrayList<BleReferenceAppliance> mLeDevices;
    private LayoutInflater mInflator;
    private Context context;

    public BleDeviceListAdapter(Context context,ArrayList<BleReferenceAppliance> bleReferenceApplianceList) {
        super();
        this.context=context;
        mLeDevices = bleReferenceApplianceList;
        mInflator = LayoutInflater.from(context);
    }

    public void addDevice(BleReferenceAppliance device) {
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

        BleReferenceAppliance device = (BleReferenceAppliance) getItem(i);
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
