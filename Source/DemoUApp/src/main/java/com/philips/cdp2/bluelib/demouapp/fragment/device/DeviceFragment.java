/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.device;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.util.UiUtils;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";
    private static final long CONNECTION_TIMEOUT = 30000L;

    private FloatingActionButton mFab;
    private SHNDevice mDevice;

    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice device) {
            updateUiState(device);
            updateConnectButtonState(device.getState());
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            SHNLogger.w(TAG, "onFailedToConnect.");
        }

        @Override
        public void onReadRSSI(int rssi) {
            // Nothing to do
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.bll_fragment_device, container, false);

        mDevice = BluelibUapp.get().getSelectedDevice();
        if (mDevice != null) {
            mDevice.registerSHNDeviceListener(mDeviceListener);

            updateUiState(mDevice);
            updateConnectButtonState(mDevice.getState());
        }

        mFab = rootview.findViewById(R.id.bll_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDevice == null) {
                    return;
                }
                mDevice.connect(CONNECTION_TIMEOUT);
            }
        });

        setupFragments();

        return rootview;
    }

    private void setupFragments() {
        if (mDevice == null) {
            return;
        }

        // Device Information capability
        SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) mDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);

        if (di == null) {
            SHNLogger.w(TAG, "Device Information capability not available.");
        } else {
            SHNLogger.w(TAG, "Device Information capability found.");
            //addFragment(new DeviceInformationFragment());
        }

        // Battery capability
        final SHNCapabilityBattery battery = (SHNCapabilityBattery) mDevice.getCapabilityForType(SHNCapabilityType.BATTERY);

        if (battery == null) {
            SHNLogger.w(TAG, "Battery capability not available.");
        } else {
            SHNLogger.i(TAG, "Battery capability found.");
            //addFragment(new BatteryFragment());
        }

        // Firmware update capability
        final SHNCapabilityFirmwareUpdate firmwareUpdate = (SHNCapabilityFirmwareUpdate) mDevice.getCapabilityForType(SHNCapabilityType.FIRMWARE_UPDATE);

        if (firmwareUpdate == null) {
            SHNLogger.w(TAG, "Firmware update capability not available.");
        } else {
            SHNLogger.i(TAG, "Firmware update capability found.");
            //addFragment(new FirmwareUpgradeFragment());
        }
    }

    public void addFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.bll_capabilities, fragment)
                .commit();
    }

    private void updateUiState(@NonNull SHNDevice device) {
        switch (device.getState()) {
            case Connected:
                UiUtils.showVolatileMessage(this.getView(), String.format(Locale.US, getString(R.string.bll_device_connected), device.getName()));
                setupFragments();
                break;
            case Connecting:
                UiUtils.showPersistentMessage(this.getView(), getString(R.string.bll_device_connecting));
                break;
            case Disconnected:
                UiUtils.showVolatileMessage(this.getView(), getString(R.string.bll_device_disconnected));
                break;
            case Disconnecting:
                UiUtils.showPersistentMessage(this.getView(), getString(R.string.bll_device_disconnecting));
                break;
        }
    }

    private void updateConnectButtonState(SHNDevice.State state) {
        if (mFab == null) {
            return;
        }
        mFab.setEnabled(SHNDevice.State.Disconnected.equals(state));
        mFab.setVisibility(mFab.isEnabled() ? View.VISIBLE : View.INVISIBLE);
    }
}
