/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.capability;

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
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

public class BatteryFragment extends Fragment {

    private static final String TAG = "BatteryFragment";

    private View rootview;
    private TextView batteryValue;
    private SHNDevice mDevice;

    private SHNIntegerResultListener batteryListener = new SHNIntegerResultListener() {
        @Override
        public void onActionCompleted(int value, SHNResult result) {
            if(result == SHNResult.SHNOk) {
                batteryValue.setText(String.format(Locale.US, "%d%%", value));
            } else {
                SHNLogger.w(TAG, "Error retrieving battery level.");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.bll_fragment_battery, container, false);

        mDevice = BluelibUapp.get().getSelectedDevice();

        batteryValue = rootview.findViewById(R.id.bll_battery_value);
        Button getButton = rootview.findViewById(R.id.bll_btn_get_battery);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SHNCapabilityBattery battery = (SHNCapabilityBattery) mDevice.getCapabilityForType(SHNCapabilityType.BATTERY);

                if (battery == null) {
                    SHNLogger.w(TAG, "Battery capability not available.");
                } else {
                    battery.getBatteryLevel(batteryListener);
                }
            }
        });

        return rootview;
    }
}
