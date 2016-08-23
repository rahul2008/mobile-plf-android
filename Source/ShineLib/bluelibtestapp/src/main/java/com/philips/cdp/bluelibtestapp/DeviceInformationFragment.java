/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;
import java.util.Map;

public class DeviceInformationFragment extends Fragment {

    private static final String TAG = DeviceInformationFragment.class.getSimpleName();

    private View fragmentView;
    private SHNDevice shnSelectedDevice;
    private SHNCapabilityDeviceInformation shnCapabilityDeviceInformation;

    public static DeviceInformationFragment newInstance() {
        DeviceInformationFragment fragment = new DeviceInformationFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SHNLogger.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SHNLogger.d(TAG, "onCreateView() called with: " + "inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        fragmentView = inflater.inflate(R.layout.fragment_device_information, container, false);
        TestApplication testApplication = (TestApplication) getActivity().getApplication();
        shnSelectedDevice = testApplication.getSelectedDevice();
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentView = null;
    }

    @Override
    public void onResume() {
        SHNLogger.d(TAG, "onResume() called with: " + "");
        super.onResume();

        shnSelectedDevice.registerSHNDeviceListener(deviceListener);
        setupDeviceInformationCapability(shnSelectedDevice);
    }

    @Override
    public void onPause() {
        SHNLogger.d(TAG, "onPause() called with: " + "");
        super.onPause();
        shnSelectedDevice.unregisterSHNDeviceListener(deviceListener);
    }

    private SHNDevice.SHNDeviceListener deviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            SHNLogger.d(TAG, "onStateUpdated() called with: " + "shnDevice = [" + shnDevice + "], shnDevice.getState() = [" + shnDevice.getState() + "]");
            switch (shnDevice.getState()) {
                case Disconnected:
                    break;
                case Connected:
                    setupDeviceInformationCapability(shnDevice);
                    break;
                case Connecting:
                    break;
                case Disconnecting:
                    break;
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            SHNLogger.d(TAG, "onFailedToConnect() called with: " + "shnDevice = [" + shnDevice + "], result = [" + result + "]");
        }
    };

    private void setupDeviceInformationCapability(SHNDevice shnDevice) {

        SHNCapabilityDeviceDiagnostics dia = new SHNCapabilityDeviceDiagnostics() {

            @Override
            public void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener) {

            }
        };

        dia.readDeviceDiagnostics(new SHNMapResultListener<String, String>() {
            @Override
            public void onActionCompleted(@NonNull final Map<String, String> value, @NonNull final SHNResult result) {

            }
        });

        SHNLogger.d(TAG, "setupDeviceInformationCapability() called with: " + "shnDevice = [" + shnDevice + "]");
        shnCapabilityDeviceInformation = (SHNCapabilityDeviceInformation) shnDevice.getCapabilityForType(SHNCapabilityType.DeviceInformation);
        if (shnCapabilityDeviceInformation != null) {
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, R.id.textViewFirmwareValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, R.id.textViewHardwareValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, R.id.textViewManufacturerValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, R.id.textViewModelNumberValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, R.id.textViewSerialNumberValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, R.id.textViewSoftwareRevisionValue);
            readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, R.id.textViewSystemIDValue);
        }
    }

    private void readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType type, final int textViewId) {
        shnCapabilityDeviceInformation.readDeviceInformation(type, new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date lastCacheUpdate) {
                SHNLogger.d(TAG, "onDeviceInformation() called with: " + "deviceInformationType = [" + deviceInformationType + "], value = [" + value + "], lastCacheUpdate = [" + lastCacheUpdate + "]");
                String text = value + "\n" + lastCacheUpdate.toString();
                setTextOnView(text, textViewId);
            }

            @Override
            public void onError(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error) {
                SHNLogger.d(TAG, "onError() called with: " + "deviceInformationType = [" + deviceInformationType + "], error = [" + error + "]");
                setTextOnView(error.name(), textViewId);
            }

            private void setTextOnView(final String text, final int textViewId) {
                if (fragmentView != null) {
                    TextView tv = (TextView) fragmentView.findViewById(textViewId);
                    if (tv != null) {
                        tv.setText(text.trim());
                    }
                }
            }
        });
    }
}
