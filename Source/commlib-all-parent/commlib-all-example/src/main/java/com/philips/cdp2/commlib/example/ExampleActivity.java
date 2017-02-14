/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.commlib.core.CommCentral;
import com.philips.commlib.core.appliance.Appliance;
import com.philips.commlib.core.appliance.ApplianceManager;
import com.philips.commlib.core.exception.MissingPermissionException;

public class ExampleActivity extends AppCompatActivity {

    private static final String TAG = "ExampleActivity";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private CommCentral commCentral;
    private ArrayAdapter<Appliance> applianceAdapter;

    private TextView txtState;

    private Runnable permissionCallback;

    private final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener<BleReferenceAppliance>() {
        @Override
        public void onApplianceFound(@NonNull BleReferenceAppliance foundAppliance) {
            Log.d(TAG, "Found appliance: " + foundAppliance.getNetworkNode().getCppId());

            applianceAdapter.clear();
            applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());
        }

        @Override
        public void onApplianceUpdated(@NonNull BleReferenceAppliance bleReferenceAppliance) {
            // NOOP
        }
    };

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartDiscovery:
                    startDiscovery();
                    break;
                case R.id.btnStopDiscovery:
                    stopDiscovery();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commCentral = ((App)getApplication()).getCommCentral();
        this.commCentral.getApplianceManager().addApplianceListener(this.applianceListener);

        findViewById(R.id.btnStartDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnStopDiscovery).setOnClickListener(buttonClickListener);

        txtState = (TextView) findViewById(R.id.txtState);

        applianceAdapter = new ArrayAdapter<Appliance>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Appliance appliance = getItem(position);

                ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));

                return view;
            }
        };

        final ListView listViewAppliances = (ListView) findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                stopDiscovery();

                Intent applianceActivityIntent = new Intent(ExampleActivity.this, ApplianceActivity.class);
                applianceActivityIntent.putExtra(ApplianceActivity.CPPID, applianceAdapter.getItem(position).getNetworkNode().getCppId());

                startActivity(applianceActivityIntent);
            }
        });

        // Init view
        updateState(getString(R.string.lblStateIdle));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(this.permissionCallback);
                } else {
                    updateState(getString(R.string.lblStateError));
                }
            }
        }
    }

    private void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private void startDiscovery() {
        applianceAdapter.clear();
        applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());

        try {
            this.commCentral.startDiscovery();
            updateState(getString(R.string.lblStateDiscovering));
        } catch (MissingPermissionException e) {
            updateState(getString(R.string.lblStatePermissionError));

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            });
        }
    }

    private void stopDiscovery() {
        this.commCentral.stopDiscovery();
        updateState(getString(R.string.lblStateIdle));
    }

    private void updateState(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtState.setText(state);
            }
        });
    }
}
