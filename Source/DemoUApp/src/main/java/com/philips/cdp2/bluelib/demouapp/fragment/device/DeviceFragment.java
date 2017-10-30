/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.fragment.capability.BatteryFragment;
import com.philips.cdp2.bluelib.demouapp.fragment.capability.DeviceInformationFragment;
import com.philips.cdp2.bluelib.demouapp.fragment.capability.FirmwareUpdateFragment;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.utility.SHNLogger;

public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";

    private SHNDevice mDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.bll_fragment_device, container, false);

        mDevice = BluelibUapp.get().getSelectedDevice();

        setupFragments();

        return rootview;
    }

    private void setupFragments() {
        if (mDevice == null) {
            return;
        }

        addFragment(new DeviceConnectionFragment());

        // Device Information capability
        SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) mDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);

        if (di == null) {
            SHNLogger.w(TAG, "Device Information capability not available.");
        } else {
            SHNLogger.w(TAG, "Device Information capability found.");
            addFragment(new DeviceInformationFragment());
        }

        // Battery capability
        final SHNCapabilityBattery battery = (SHNCapabilityBattery) mDevice.getCapabilityForType(SHNCapabilityType.BATTERY);

        if (battery == null) {
            SHNLogger.w(TAG, "Battery capability not available.");
        } else {
            SHNLogger.i(TAG, "Battery capability found.");
            addFragment(new BatteryFragment());
        }

        // Firmware update capability
        final SHNCapabilityFirmwareUpdate firmwareUpdate = (SHNCapabilityFirmwareUpdate) mDevice.getCapabilityForType(SHNCapabilityType.FIRMWARE_UPDATE);

        if (firmwareUpdate == null) {
            SHNLogger.w(TAG, "Firmware update capability not available.");
        } else {
            SHNLogger.i(TAG, "Firmware update capability found.");
            addFragment(new FirmwareUpdateFragment());
        }
    }

    public void addFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.bll_capabilities, fragment)
                .commit();
    }
}
