package com.philips.platform.appframework.connectivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;
import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BLEScanDialogFragment extends DialogFragment {

    private static final String TAG = "BLEScanDialogFragment";
    private BLEScanDialogListener bleScanDialogListener;

    private BleDeviceListAdapter leDeviceListAdapter;

    private ProgressBar progressBar;

    private ArrayList<RefAppBleReferenceAppliance> storedApplianceList = new ArrayList<>();

    /**
     * Set saved appliance list
     *
     * @param savedApplianceList
     */
    public void setSavedApplianceList(Set<Appliance> savedApplianceList) {
        if (savedApplianceList != null) {
            for (Appliance appliance : savedApplianceList) {
                if (appliance instanceof RefAppBleReferenceAppliance) {
                    storedApplianceList.add((RefAppBleReferenceAppliance) appliance);
                    RALog.d(TAG, "Added Applicance to appliance list");
                }
            }
        }
    }

    public interface BLEScanDialogListener {
        void onDeviceSelected(RefAppBleReferenceAppliance bleRefAppliance);
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
        leDeviceListAdapter = new BleDeviceListAdapter(getActivity(), storedApplianceList);
        ListView deviceListView = (ListView) view.findViewById(R.id.device_listview);
        progressBar = (ProgressBar) view.findViewById(R.id.scanning_progress_bar);
        deviceListView.setAdapter(leDeviceListAdapter);
        showProgressBar();
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                RefAppBleReferenceAppliance refAppBleReferenceAppliance = (RefAppBleReferenceAppliance) view.getTag(R.string.RA_ble_ref_device);
                bleScanDialogListener.onDeviceSelected(refAppBleReferenceAppliance);
                dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void addDevice(RefAppBleReferenceAppliance shnDevice) {
        leDeviceListAdapter.addDevice(shnDevice);
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
