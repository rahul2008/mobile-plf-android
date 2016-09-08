/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.associate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.R;
import com.example.cdpp.bluelibexampleapp.device.BaseDeviceAdapter;
import com.example.cdpp.bluelibexampleapp.detail.DeviceDetailActivity;
import com.philips.pins.shinelib.SHNAssociationProcedure;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceAssociation;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

public class AssociatedDevicesFragment extends Fragment {

    private static final String TAG = "AssociatedDevicesFragment";

    private SHNCentral mShnCentral;
    private SHNDeviceAssociation mShnDeviceAssociation;

    private DeviceDefinitionAdapter mDeviceDefinitionAdapter;
    private List<SHNDeviceDefinitionInfo> mDeviceDefinitions = new ArrayList<>();

    private AssociatedDeviceAdapter mAssociatedDeviceAdapter;
    private List<SHNDevice> mAssociatedDevices = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.myLooper());

    private SHNDeviceAssociation.SHNDeviceAssociationListener mDeviceAssociationListener = new SHNDeviceAssociation.SHNDeviceAssociationListener() {
        @Override
        public void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure) {
            showMessage("Association started.");
        }

        @Override
        public void onAssociationStopped() {
            showMessage("Association stopped.");
        }

        @Override
        public void onAssociationSucceeded(SHNDevice shnDevice) {
            showMessage(String.format("Association with '%s' succeeded.", shnDevice.getName()));

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAssociatedDeviceAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onAssociationFailed(SHNResult shnError) {
            showMessage("Association failed: " + shnError.name());
        }

        @Override
        public void onAssociatedDevicesUpdated() {
            showMessage("Association devices updated.");

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAssociatedDeviceAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public AssociatedDevicesFragment() {
    }

    public static AssociatedDevicesFragment newInstance() {
        return new AssociatedDevicesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_associate, container, false);

        // Obtain reference to BlueLib instance
        mShnCentral = BlueLibExampleApplication.get().getShnCentral();

        // Setup device association
        mShnDeviceAssociation = mShnCentral.getShnDeviceAssociation();
        mShnDeviceAssociation.setShnDeviceAssociationListener(mDeviceAssociationListener);

        // Setup device definitions list
        mDeviceDefinitions = mShnCentral.getSHNDeviceDefinitions().getRegisteredDeviceDefinitions();
        mDeviceDefinitionAdapter = new DeviceDefinitionAdapter(mDeviceDefinitions);
        mDeviceDefinitionAdapter.setOnItemClickListener(new BaseDeviceAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, View itemView) {
                SHNDeviceDefinitionInfo info = mDeviceDefinitionAdapter.getItem(position);

                SHNLogger.i(TAG, "Selected device definition: " + info.getDeviceTypeName());

                mShnDeviceAssociation.startAssociationForDeviceType(info.getDeviceTypeName());
            }

            @Override
            public void onItemLongClick(int position, View itemView) {
                // Nothing to do
            }
        });

        RecyclerView deviceDefinitionsView = (RecyclerView) rootView.findViewById(R.id.deviceDefinitions);
        deviceDefinitionsView.setAdapter(mDeviceDefinitionAdapter);

        // Setup associated devices list
        mAssociatedDevices = mShnCentral.getShnDeviceAssociation().getAssociatedDevices();
        mAssociatedDeviceAdapter = new AssociatedDeviceAdapter(mAssociatedDevices);
        mAssociatedDeviceAdapter.setOnItemClickListener(new BaseDeviceAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, View itemView) {
                final SHNDevice device = mAssociatedDeviceAdapter.getItem(position);
                BlueLibExampleApplication.get().setSelectedDevice(device);

                SHNLogger.i(TAG, "Selected associated device: " + device.getName());

                startActivity(new Intent(getActivity(), DeviceDetailActivity.class));
            }

            @Override
            public void onItemLongClick(int position, View itemView) {
                final SHNDevice device = mAssociatedDeviceAdapter.getItem(position);

                mShnDeviceAssociation.removeAssociatedDevice(device);
                mAssociatedDeviceAdapter.notifyDataSetChanged();

                showMessage(String.format("Association with '%s' removed.", device.getName()));
            }
        });

        RecyclerView associatedDevicesView = (RecyclerView) rootView.findViewById(R.id.associatedDevices);
        associatedDevicesView.setAdapter(mAssociatedDeviceAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAssociatedDeviceAdapter.notifyDataSetChanged();
    }

    private void showMessage(String s) {
        SHNLogger.i(TAG, s);

        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
