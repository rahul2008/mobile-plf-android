package com.example.cdpp.bluelibreferenceapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = "DeviceDetail";

    public static final String ARG_ITEM_ID = "arg_item_id";

    private SHNDevice mDevice;
    private SHNDevice.SHNDeviceListener mDeviceListener = new SHNDevice.SHNDeviceListener() {

        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            Log.d(TAG, "onStateUpdated.");

            switch (mDevice.getState()) {
                case Connected:
                    Log.i(TAG, "Device connected.");
                    break;
                case Connecting:
                    Log.i(TAG, "Device connecting...");
                    break;
                case Disconnected:
                    Log.i(TAG, "Device disconnected.");
                    break;
                case Disconnecting:
                    Log.i(TAG, "Device disconnecting...");
                    break;
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            Log.w(TAG, "onFailedToConnect.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDevice != null) {

                    mDevice.connect();
                }
            }
        });

        mDevice = ReferenceApplication.get().getSelectedDevice();
        if (mDevice != null) {
            mDevice.registerSHNDeviceListener(mDeviceListener);
        }

        if (mDevice != null) {
            setTitle(mDevice.getName());

            TextView deviceDetails = (TextView) findViewById(R.id.device_details);

            StringBuilder builder = new StringBuilder(getString(R.string.device_detail_device_name))
                    .append(mDevice.getName())
                    .append("\n")
                    .append(getString(R.string.device_detail_device_type_name))
                    .append(mDevice.getDeviceTypeName())
                    .append("\n")
                    .append(getString(R.string.device_detail_device_address))
                    .append(mDevice.getAddress())
                    .append("\n")
                    .append(getString(R.string.device_detail_device_state))
                    .append(mDevice.getState());

            deviceDetails.setText(builder.toString());
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
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            navigateUpTo(new Intent(this, ReferenceActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
