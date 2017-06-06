/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclientsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclientsample.appliance.airpurifier.AirPurifier;
import com.philips.cdp.dicommclientsample.appliance.reference.WifiReferenceAppliance;
import com.philips.cdp2.commlib.core.appliance.Appliance;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DiscoveryManager<?> discoveryManager;
    private ArrayAdapter<Appliance> applianceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        listViewAppliances.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Appliance appliance = applianceAdapter.getItem(position);
                CurrentApplianceManager.getInstance().setCurrentAppliance(appliance);

                if (appliance instanceof AirPurifier) {
                    startActivity(new Intent(MainActivity.this, AirPurifierActivity.class));
                } else if (appliance instanceof WifiReferenceAppliance) {
                    startActivity(new Intent(MainActivity.this, WifiReferenceApplianceActivity.class));
                }
            }
        });

        discoveryManager = DiscoveryManager.getInstance();

        ((TextView) findViewById(R.id.textViewAppId)).setText(DICommClientWrapper.getAppId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        discoveryManager.addDiscoveryEventListener(discoveryEventListener);
        discoveryManager.start();

        applianceAdapter.clear();
        applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
    }

    @Override
    protected void onPause() {
        super.onPause();

        discoveryManager.removeDiscoverEventListener(discoveryEventListener);
        discoveryManager.stop();
    }

    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    applianceAdapter.clear();
                    applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
                }
            });

            for (Appliance appliance : discoveryManager.getAllDiscoveredAppliances()) {
                appliance.getWifiPort().addPortListener(wifiPortListener);
            }
        }
    };

    private DICommPortListener<WifiPort> wifiPortListener = new DICommPortListener<WifiPort>() {

        @Override
        public void onPortUpdate(final WifiPort port) {
            Log.d(TAG, "onPortUpdate() called with: " + "port = [" + port + "]");
            WifiPortProperties portProperties = port.getPortProperties();
            if (portProperties != null) {
                Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
            }
        }

        @Override
        public void onPortError(final WifiPort port, final Error error, final String errorData) {
            Log.d(TAG, "onPortError() called with: " + "port = [" + port + "], error = [" + error + "], errorData = [" + errorData + "]");
        }
    };
}
