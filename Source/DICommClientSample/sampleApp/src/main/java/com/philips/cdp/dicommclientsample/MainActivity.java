/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclientsample.airpurifier.AirPurifier;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CommCentral commCentral;
    private ArrayAdapter<Appliance> applianceAdapter;

    private final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener<AirPurifier>() {
        @Override
        public void onApplianceFound(@NonNull AirPurifier foundAppliance) {
            foundAppliance.getWifiPort().addPortListener(wifiPortListener);

            applianceAdapter.clear();
            applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());
        }

        @Override
        public void onApplianceUpdated(@NonNull AirPurifier updatedAppliance) {
            // NOOP
        }
    };

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
                CurrentApplianceManager.getInstance().setCurrentAppliance(applianceAdapter.getItem(position));
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });

        ((TextView) findViewById(R.id.textViewAppId)).setText(DICommClientWrapper.getAppId());

        this.commCentral = ((App) getApplication()).getCommCentral();
        this.commCentral.getApplianceManager().addApplianceListener(this.applianceListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        applianceAdapter.clear();
        applianceAdapter.addAll(this.commCentral.getApplianceManager().getAvailableAppliances());

        try {
            this.commCentral.startDiscovery();
        } catch (MissingPermissionException e) {
            Log.e(TAG, "Missing permission for discovery: " + e.getMessage());
        } catch (TransportUnavailableException e) {
            Log.e(TAG, "Transport unavailable for discovery: " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.commCentral.stopDiscovery();
    }

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
