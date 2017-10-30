/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.associate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.adapter.AssociatedDeviceAdapter;
import com.philips.cdp2.bluelib.demouapp.adapter.BaseDeviceAdapter;
import com.philips.cdp2.bluelib.demouapp.adapter.DeviceDefinitionAdapter;
import com.philips.cdp2.bluelib.demouapp.fragment.device.DeviceFragment;
import com.philips.cdp2.bluelib.demouapp.util.UiUtils;
import com.philips.pins.shinelib.SHNAssociationProcedure;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceAssociation;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.List;

public class AssociatedDevicesFragment extends Fragment {

    private static final String TAG = "AssociatedDevicesFragment";

    private SHNDeviceAssociation mShnDeviceAssociation;

    private DeviceDefinitionAdapter mDeviceDefinitionAdapter;

    private AssociatedDeviceAdapter mAssociatedDeviceAdapter;

    private View mView;

    private SHNDeviceAssociation.SHNDeviceAssociationListener mDeviceAssociationListener = new SHNDeviceAssociation.SHNDeviceAssociationListener() {
        @Override
        public void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure) {
            UiUtils.showPersistentMessage(mView, getContext().getString(R.string.bll_association_started));
        }

        @Override
        public void onAssociationStopped() {
            UiUtils.showVolatileMessage(mView, getContext().getString(R.string.bll_association_stopped));
        }

        @Override
        public void onAssociationSucceeded(SHNDevice shnDevice) {
            UiUtils.showVolatileMessage(mView, String.format(getContext().getString(R.string.bll_association_succeeded), shnDevice.getName()));
            mAssociatedDeviceAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAssociationFailed(SHNResult shnError) {
            UiUtils.showVolatileMessage(mView, getContext().getString(R.string.bll_association_failed) + shnError.name());
        }

        @Override
        public void onAssociatedDevicesUpdated() {
            UiUtils.showVolatileMessage(mView, getContext().getString(R.string.bll_association_devices_updated));

            mAssociatedDeviceAdapter.notifyDataSetChanged();
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
        mView = inflater.inflate(R.layout.bll_fragment_associate, container, false);

        // Obtain reference to BlueLib instance
        SHNCentral shnCentral = BluelibUapp.get().getDependencies().getShnCentral();

        // Setup device association
        mShnDeviceAssociation = shnCentral.getShnDeviceAssociation();
        mShnDeviceAssociation.setShnDeviceAssociationListener(mDeviceAssociationListener);

        // Setup device definitions list
        List<SHNDeviceDefinitionInfo> deviceDefinitions = shnCentral.getSHNDeviceDefinitions().getRegisteredDeviceDefinitions();
        mDeviceDefinitionAdapter = new DeviceDefinitionAdapter(deviceDefinitions);
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

        RecyclerView deviceDefinitionsView = (RecyclerView) mView.findViewById(R.id.bll_deviceDefinitions);
        deviceDefinitionsView.setAdapter(mDeviceDefinitionAdapter);

        // Setup associated devices list
        List<SHNDevice> associatedDevices = shnCentral.getShnDeviceAssociation().getAssociatedDevices();
        mAssociatedDeviceAdapter = new AssociatedDeviceAdapter(associatedDevices);
        mAssociatedDeviceAdapter.setOnItemClickListener(new BaseDeviceAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, View itemView) {
                final SHNDevice device = mAssociatedDeviceAdapter.getItem(position);
                BluelibUapp.get().setSelectedDevice(device);

                SHNLogger.i(TAG, "Selected associated device: " + device.getName());
                BluelibUapp.get().nextFragment(new DeviceFragment());
            }

            @Override
            public void onItemLongClick(final int position, View itemView) {
                UiUtils.showConfirmationMessage(mView, getString(R.string.bll_association_question_remove), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SHNDevice device = mAssociatedDeviceAdapter.getItem(position);

                        mShnDeviceAssociation.removeAssociatedDevice(device);
                        mAssociatedDeviceAdapter.notifyDataSetChanged();

                        UiUtils.showVolatileMessage(mView, String.format(getContext().getString(R.string.bll_association_removed), device.getName()));
                    }
                });
            }
        });

        RecyclerView associatedDevicesView = (RecyclerView) mView.findViewById(R.id.bll_associatedDevices);
        associatedDevicesView.setAdapter(mAssociatedDeviceAdapter);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAssociatedDeviceAdapter.notifyDataSetChanged();
    }
}
