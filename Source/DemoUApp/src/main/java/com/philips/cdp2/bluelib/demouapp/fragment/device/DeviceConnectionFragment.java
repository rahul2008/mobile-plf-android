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
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;

public class DeviceConnectionFragment extends Fragment {

    private SHNDevice mDevice;
    private Button connectButton;
    private TextView connectStatus;

    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice device) {
            showState();
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            showState();
        }

        @Override
        public void onReadRSSI(int rssi) {
            // Nothing to do
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.bll_fragment_device_connection, container, false);

        connectButton = rootview.findViewById(R.id.bll_ButtonConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClick();
            }
        });

        connectStatus = rootview.findViewById(R.id.bll_textViewConnectionState);

        mDevice = BluelibUapp.get().getSelectedDevice();
        if (mDevice != null) {
            mDevice.registerSHNDeviceListener(mDeviceListener);
        }

        showState();

        return rootview;
    }

    private void performClick() {
        if (mDevice == null) {
            return;
        }

        switch (mDevice.getState()) {
            case Disconnected:
                mDevice.connect();
                break;
            case Connected:
                mDevice.disconnect();
                break;
        }
    }

    private void showState() {
        if (mDevice == null) {
            return;
        }

        switch (mDevice.getState()) {
            case Disconnected:
                connectButton.setEnabled(true);
                connectButton.setText(R.string.bll_connect);
                break;
            case Disconnecting:
                connectButton.setEnabled(false);
                break;
            case Connecting:
                connectButton.setEnabled(false);
                break;
            case Connected:
                connectButton.setEnabled(true);
                connectButton.setText(R.string.bll_disconnect);
                break;
        }

        connectStatus.setText(mDevice.getState().name());
    }
}
