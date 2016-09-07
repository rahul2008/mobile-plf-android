package com.example.cdpp.bluelibexampleapp.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.R;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = "DeviceDetail";

    public static final String ARG_ITEM_ID = "arg_item_id";

    private Handler mHandler = new Handler(Looper.myLooper());

    private SHNDevice mDevice;
    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            switch (shnDevice.getState()) {
                case Connected:
                    SHNLogger.i(TAG, "Device connected: " + shnDevice.getName());
                    setupDeviceCapabilities(shnDevice);
                    break;
                case Connecting:
                    SHNLogger.i(TAG, "Device connecting...");
                    break;
                case Disconnected:
                    SHNLogger.i(TAG, "Device disconnected.");
                    break;
                case Disconnecting:
                    SHNLogger.i(TAG, "Device disconnecting...");
                    break;
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            SHNLogger.w(TAG, "onFailedToConnect.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Setup connection button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDevice == null) {
                    return;
                }
                mDevice.connect();
            }
        });

        // Obtain selected device instance
        mDevice = BlueLibExampleApplication.get().getSelectedDevice();
        if (mDevice != null) {
            mDevice.registerSHNDeviceListener(mDeviceListener);
            setTitle(mDevice.getName());
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDevice != null) {
            mDevice.unregisterSHNDeviceListener(mDeviceListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDeviceCapabilities(final SHNDevice shnDevice) {
        // Device Information capability
        SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) shnDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);

        if (di == null) {
            SHNLogger.w(TAG, "Device Information capability not available.");
        } else {
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, R.id.textViewFirmwareValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, R.id.textViewHardwareValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, R.id.textViewManufacturerValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, R.id.textViewModelNumberValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, R.id.textViewSerialNumberValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, R.id.textViewSoftwareRevisionValue);
            displayDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, R.id.textViewSystemIDValue);
        }

        // Battery capability
        final SHNCapabilityBattery battery = (SHNCapabilityBattery) shnDevice.getCapabilityForType(SHNCapabilityType.BATTERY);

        if (battery == null) {
            SHNLogger.w(TAG, "Battery capability not available.");
        } else {
            SHNLogger.i(TAG, "Battery capability found.");

            // Battery level
            displayBatteryLevel(battery);

            // Listen for battery level changes
            battery.setSetSHNCapabilityBatteryListener(new SHNCapabilityBattery.SHNCapabilityBatteryListener() {
                @Override
                public void onBatteryLevelChanged(int level) {
                    updateBatteryLevel(level);
                }
            });
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
                setTextByViewId(error.name(), textViewId);
            }
        });
    }

    private void displayBatteryLevel(SHNCapabilityBattery battery) {
        if (battery == null) {
            return;
        }

        battery.getBatteryLevel(new SHNIntegerResultListener() {
            @Override
            public void onActionCompleted(int value, SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    updateBatteryLevel(value);
                }
            }
        });
    }

    private void updateBatteryLevel(int batteryLevel) {
        setTextByViewId(String.format("%d%%", batteryLevel), R.id.textViewBatteryValue);
    }

    private void setTextByViewId(final String text, final int textViewId) {
        final TextView tv = (TextView) findViewById(textViewId);
        if (tv == null) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tv.setText(text.trim());
            }
        });
    }
}
