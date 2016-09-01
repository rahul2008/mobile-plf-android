/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DiscoveryManager<?> discoveryManager;
    private ArrayAdapter<DICommAppliance> applianceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applianceAdapter = new ArrayAdapter<DICommAppliance>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                DICommAppliance appliance = getItem(position);
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

        discoveryManager = DiscoveryManager.getInstance();

        //RegistrationHelper.getInstance().registerUserRegistrationListener(this.registrationListener);

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

    @Override
    protected void onDestroy() {
        //RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this.registrationListener);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(this, RegistrationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

            for (DICommAppliance appliance : discoveryManager.getAllDiscoveredAppliances()) {
                appliance.getWifiPort().addPortListener(wifiPortListener);
            }
        }
    };

    private DICommPortListener wifiPortListener = new DICommPortListener() {

        @Override
        public void onPortUpdate(final DICommPort<?> port) {
            Log.d(TAG, "onPortUpdate() called with: " + "port = [" + port + "]");
            WifiPortProperties portProperties = ((WifiPort) port).getPortProperties();
            if (portProperties != null) {
                Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
            }
        }

        @Override
        public void onPortError(final DICommPort<?> port, final Error error, final String errorData) {
            Log.d(TAG, "onPortError() called with: " + "port = [" + port + "], error = [" + error + "], errorData = [" + errorData + "]");
        }
    };

    private UserRegistrationListener registrationListener = new UserRegistrationListener() {
        @Override
        public void onUserRegistrationComplete(final Activity activity) {

        }

        @Override
        public void onPrivacyPolicyClick(final Activity activity) {

        }

        @Override
        public void onTermsAndConditionClick(final Activity activity) {

        }

        @Override
        public void onUserLogoutSuccess() {

        }

        @Override
        public void onUserLogoutFailure() {

        }

        @Override
        public void onUserLogoutSuccessWithInvalidAccessToken() {

        }
    };
}
