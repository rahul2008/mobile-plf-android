/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.utility.SHNLogger;

public class BatteryFragment extends Fragment implements SHNDevice.SHNDeviceListener {
    private static final String TAG = BatteryFragment.class.getSimpleName();

    private View fragmentView;
    private Switch enableBatteryNotification;

    private SHNDevice shnSelectedDevice;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public static BatteryFragment newInstance() {
        return new BatteryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SHNLogger.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SHNLogger.e(TAG, "onCreateView: " + fragmentView);
        if (fragmentView == null) {
            TestApplication application = (TestApplication) getActivity().getApplication();

            shnSelectedDevice = application.getSelectedDevice();
            fragmentView = inflater.inflate(R.layout.fragment_battery, container, false);

            Button getBatteryLevelButton = (Button) fragmentView.findViewById(R.id.buttonGetBatteryLevel);
            getBatteryLevelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setupBatteryCapability(shnSelectedDevice);
                }
            });

            enableBatteryNotification = (Switch) fragmentView.findViewById(R.id.switchNotification);
            onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setupBatteryNotification(shnSelectedDevice, isChecked);
                }
            };
            enableBatteryNotification.setOnCheckedChangeListener(onCheckedChangeListener);
        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        SHNLogger.e(TAG, "onResume");
        super.onResume();
        shnSelectedDevice.registerSHNDeviceListener(this);
    }

    @Override
    public void onPause() {
        SHNLogger.e(TAG, "onPause");
        super.onPause();
        shnSelectedDevice.unregisterSHNDeviceListener(this);
    }

    @Override
    public void onStateUpdated(SHNDevice shnDevice) {
        SHNLogger.i(TAG, "onStateUpdated");
        if (shnDevice.getState() == SHNDevice.State.Connected) {
            setupBatteryCapability(shnDevice);
        }
    }

    @Override
    public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {

    }

    private void setupBatteryCapability(SHNDevice shnDevice) {
        SHNCapabilityBattery shnCapabilityBattery = (SHNCapabilityBattery) shnDevice.getCapabilityForType(SHNCapabilityType.Battery);
        if (shnCapabilityBattery != null) {
            SHNLogger.i(TAG, "found shnCapabilityBattery");
            shnCapabilityBattery.setSetSHNCapabilityBatteryListener(batteryListener);
            shnCapabilityBattery.getBatteryLevel(new SHNIntegerResultListener() {

                @Override
                public void onActionCompleted(int value, SHNResult result) {
                    if (result == SHNResult.SHNOk) {
                        updateBatteryLevelTextView(value);
                    }
                }
            });
        }
    }

    private void updateBatteryLevelTextView(int batteryLevel) {
        TextView tv = (TextView) fragmentView.findViewById(R.id.textViewBatteryValue);
        tv.setText(String.format(getResources().getString(R.string.digit), batteryLevel));
    }

    private Handler handler = new Handler();

    private void setupBatteryNotification(SHNDevice shnDevice, final boolean enabled) {
        SHNCapabilityBattery shnCapabilityBattery = (SHNCapabilityBattery) shnDevice.getCapabilityForType(SHNCapabilityType.Battery);
        if (shnCapabilityBattery != null) {
            SHNLogger.i(TAG, "found shnCapabilityBattery");
            shnCapabilityBattery.setBatteryLevelNotifications(enabled, new SHNResultListener() {
                @Override
                public void onActionCompleted(SHNResult result) {
                    if (result != SHNResult.SHNOk) {
                        enableBatteryNotification.setOnCheckedChangeListener(null);
                        Runnable delayed = new Runnable() {
                            @Override
                            public void run() {
                                enableBatteryNotification.setChecked(!enabled);
                                enableBatteryNotification.setOnCheckedChangeListener(onCheckedChangeListener);
                            }
                        };
                        handler.postDelayed(delayed, 1500);
                    }
                }
            });
        }
    }

    private SHNCapabilityBattery.SHNCapabilityBatteryListener batteryListener = new SHNCapabilityBattery.SHNCapabilityBatteryListener() {
        @Override
        public void onBatteryLevelChanged(int level) {
            BatteryFragment.this.updateBatteryLevelTextView(level);
        }
    };
}
