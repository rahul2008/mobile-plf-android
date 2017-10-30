/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.capability;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;

public class DeviceInformationFragment extends Fragment {

    private static final String TAG = "DeviceInformationFragment";

    private View rootView;
    private SHNDevice mDevice;
    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice device) {
            switch (device.getState()) {
                case Connected:
                    showDeviceInformation();
                    break;
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            // Nothing to do
        }

        @Override
        public void onReadRSSI(int rssi) {
            // Nothing to do
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bll_fragment_device_information, container, false);

        mDevice = BluelibUapp.get().getSelectedDevice();
        if (mDevice != null) {
            mDevice.registerSHNDeviceListener(mDeviceListener);
        }

        showDeviceInformation();

        return rootView;
    }

    private void showDeviceInformation() {
        SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) mDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);

        if (di == null) {
            SHNLogger.w(TAG, "Device Information capability not available.");
        } else {
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, R.id.bll_textViewFirmwareValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, R.id.bll_textViewHardwareValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, R.id.bll_textViewManufacturerValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, R.id.bll_textViewModelNumberValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, R.id.bll_textViewSerialNumberValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, R.id.bll_textViewSoftwareRevisionValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, R.id.bll_textViewSystemIDValue);
        }
    }

    private void displayDeviceInformation(SHNCapabilityDeviceInformation deviceInformation, SHNCapabilityDeviceInformation.SHNDeviceInformationType type, final int textViewId) {
        if (deviceInformation == null) {
            return;
        }

        deviceInformation.readDeviceInformation(type, new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date lastCacheUpdate) {
                setTextByViewId(value, textViewId);
            }

            @Override
            public void onError(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error) {
                SHNLogger.e(TAG, "Error reading device information: " + error.name());

                setTextByViewId(getString(R.string.bll_unknown), textViewId);
            }
        });
    }

    private void setTextByViewId(final String text, final int textViewId) {
        if(rootView == null) {
            return;
        }

        final TextView tv = rootView.findViewById(textViewId);
        if (tv == null) {
            return;
        }
        tv.setText(text.trim());
    }
}
