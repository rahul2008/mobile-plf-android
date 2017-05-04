package com.philips.platform.appframework.connectivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

import java.util.ArrayList;
import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BLEScanDialogFragment extends DialogFragment {

    private BLEScanDialogListener bleScanDialogListener;

    private LeDeviceListAdapter leDeviceListAdapter;

    private ProgressBar progressBar;

    private ArrayList<BleReferenceAppliance> storedpplianceList = new ArrayList<>();

    /**
     * Set saved appliance list
     *
     * @param savedApplianceList
     */
    public void setSavedApplianceList(Set<Appliance> savedApplianceList) {
        if (savedApplianceList != null) {
            for (Appliance appliance : savedApplianceList) {
                if (appliance instanceof BleReferenceAppliance) {
                    storedpplianceList.add((BleReferenceAppliance) appliance);
                }
            }
        }
    }

    public interface BLEScanDialogListener {
        void onDeviceSelected(BleReferenceAppliance bleRefAppliance);
    }

    public void setBLEDialogListener(BLEScanDialogListener bleScanDialogListener) {
        this.bleScanDialogListener = bleScanDialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reference_device_scan_dialog, null);
        builder.setView(view);
        leDeviceListAdapter = new LeDeviceListAdapter(storedpplianceList);
        ListView deviceListView = (ListView) view.findViewById(R.id.device_listview);
        progressBar = (ProgressBar) view.findViewById(R.id.scanning_progress_bar);
        deviceListView.setAdapter(leDeviceListAdapter);
        showProgressBar();
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                BleReferenceAppliance bleReferenceAppliance = (BleReferenceAppliance) view.getTag(R.string.ble_ref_device);
                bleScanDialogListener.onDeviceSelected(bleReferenceAppliance);
                dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void addDevice(BleReferenceAppliance shnDevice) {
        leDeviceListAdapter.addDevice(shnDevice);
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BleReferenceAppliance> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter(ArrayList<BleReferenceAppliance> bleReferenceApplianceList) {
            super();
            mLeDevices = bleReferenceApplianceList;
            mInflator = getActivity().getLayoutInflater();
        }

        public void addDevice(BleReferenceAppliance device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                notifyDataSetChanged();
            }
        }

        public BleReferenceAppliance getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
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

            BleReferenceAppliance device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getNetworkNode().getCppId());
            view.setTag(R.string.ble_ref_device, device);
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public int getDeviceCount() {
        return leDeviceListAdapter.getCount();
    }
}
