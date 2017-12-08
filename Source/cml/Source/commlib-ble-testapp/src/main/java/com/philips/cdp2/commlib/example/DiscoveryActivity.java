/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager.ApplianceListener;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp2.commlib.example.appliance.ReferenceAppliance.CPPID;

public class DiscoveryActivity extends AppCompatActivity {

    private static final String TAG = "DiscoveryActivity";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private CommCentral commCentral;
    private ArrayAdapter<Appliance> applianceAdapter;

    private TextView txtState;
    private EditText editFilterModelId;
    private final Set<String> discoveryFilterModelIds = new HashSet<>();

    private Runnable permissionCallback;

    private final ApplianceListener applianceListener = new ApplianceListener() {
        @Override
        public void onApplianceFound(@NonNull Appliance foundAppliance) {
            Log.d(TAG, "Found appliance: " + foundAppliance.getNetworkNode().getCppId());

            applianceAdapter.clear();
            applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance bleReferenceAppliance) {
            // NOOP
        }

        @Override
        public void onApplianceLost(@NonNull Appliance lostAppliance) {
            Log.d(TAG, "Lost appliance: " + lostAppliance.getNetworkNode().getCppId());

            applianceAdapter.clear();
            applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());
        }
    };

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartDiscovery:
                    findViewById(R.id.btnStartDiscovery).setEnabled(false);
                    findViewById(R.id.btnStopDiscovery).setEnabled(true);

                    startDiscovery();
                    break;
                case R.id.btnStopDiscovery:
                    findViewById(R.id.btnStartDiscovery).setEnabled(true);
                    findViewById(R.id.btnStopDiscovery).setEnabled(false);

                    stopDiscovery();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        commCentral = ((App) getApplication()).getCommCentral();
        this.commCentral.getApplianceManager().addApplianceListener(this.applianceListener);

        findViewById(R.id.btnStartDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnStopDiscovery).setOnClickListener(buttonClickListener);

        txtState = (TextView) findViewById(R.id.txtState);
        editFilterModelId = (EditText) findViewById(R.id.editFilterModelId);
        editFilterModelId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateModelIds(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        applianceAdapter = new ArrayAdapter<Appliance>(this, R.layout.appliance, R.id.appliance_name) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Appliance appliance = getItem(position);

                ((TextView) view.findViewById(R.id.appliance_name)).setText(String.format("%s (%s)", appliance.getName(), appliance.getDeviceType()));
                ((TextView) view.findViewById(R.id.appliance_cpp_id)).setText(appliance.getNetworkNode().getCppId());
                ((TextView) view.findViewById(R.id.appliance_model_id)).setText(appliance.getNetworkNode().getModelId());

                return view;
            }
        };

        final ListView listViewAppliances = (ListView) findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Intent applianceActivityIntent = new Intent(DiscoveryActivity.this, BleReferenceApplianceActivity.class);
                applianceActivityIntent.putExtra(CPPID, applianceAdapter.getItem(position).getNetworkNode().getCppId());

                startActivity(applianceActivityIntent);
            }
        });

        // Init view
        updateState(getString(R.string.lblStateIdle));
    }

    private void updateModelIds(final String commaSeparatedModelIds) {
        discoveryFilterModelIds.clear();

        if (!TextUtils.isEmpty(commaSeparatedModelIds)) {
            discoveryFilterModelIds.addAll(Arrays.asList((commaSeparatedModelIds).split(",")));
        }
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
            this.commCentral.startDiscovery(discoveryFilterModelIds);
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
