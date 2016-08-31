package com.example.cdpp.bluelibreferenceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNDevice;

public class PeripheralDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "arg_item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SHNDevice selectedDevice = ReferenceApplication.get().getSelectedDevice();
        if (selectedDevice != null) {
            setTitle(ReferenceApplication.get().getSelectedDevice().getName());

            TextView peripheralDetails = (TextView) findViewById(R.id.peripheral_details);

            StringBuilder builder = new StringBuilder(getString(R.string.peripheral_detail_device_name))
                    .append(selectedDevice.getName())
                    .append("\n")
                    .append(getString(R.string.peripheral_detail_device_type_name))
                    .append(selectedDevice.getDeviceTypeName())
                    .append("\n")
                    .append(getString(R.string.peripheral_detail_device_address))
                    .append(selectedDevice.getAddress())
                    .append("\n")
                    .append(getString(R.string.peripheral_detail_device_state))
                    .append(selectedDevice.getState());

            peripheralDetails.setText(builder.toString());
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
            navigateUpTo(new Intent(this, PeripheralListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
