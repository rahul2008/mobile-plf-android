package com.example.cdpp.bluelibexampleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = "DeviceDetail";

    public static final String ARG_ITEM_ID = "arg_item_id";

    private SHNDevice mDevice;
    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            SHNLogger.d(TAG, "onStateUpdated.");

            switch (shnDevice.getState()) {
                case Connected:
                    SHNLogger.i(TAG, "Device connected.");

                    obtainDeviceInformation(shnDevice);

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
        mDevice = ReferenceApplication.get().getSelectedDevice();
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

    private void obtainDeviceInformation(final SHNDevice shnDevice) {
        final SHNCapabilityDeviceDiagnostics diagnostics = new SHNCapabilityDeviceDiagnostics() {

            @Override
            public void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener) {
                SHNLogger.d(TAG, "Reading device diagnostics...");
            }
        };

        diagnostics.readDeviceDiagnostics(new SHNMapResultListener<String, String>() {
            @Override
            public void onActionCompleted(@NonNull final Map<String, String> value, @NonNull final SHNResult result) {
                SHNLogger.d(TAG, "Done reading device diagnostics.");

                SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) shnDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);

                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, R.id.textViewFirmwareValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, R.id.textViewHardwareValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, R.id.textViewManufacturerValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, R.id.textViewModelNumberValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, R.id.textViewSerialNumberValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, R.id.textViewSoftwareRevisionValue);
                renderDeviceInformation(di, SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, R.id.textViewSystemIDValue);
            }
        });
    }

    private void renderDeviceInformation(SHNCapabilityDeviceInformation deviceInformation, SHNCapabilityDeviceInformation.SHNDeviceInformationType type, final int textViewId) {
        if (deviceInformation == null) {
            return;
        }

        deviceInformation.readDeviceInformation(type, new SHNCapabilityDeviceInformation.Listener() {
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
        });
    }

    private void setTextOnView(final String text, final int textViewId) {
        TextView tv = (TextView) findViewById(textViewId);
        if (tv != null) {
            tv.setText(text.trim());
        }
    }
}
