package com.philips.cdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.demo.R;
import com.philips.cdp.dicom.DetailActivity;
import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

public class DiComActivity extends AppCompatActivity {

    private String TAG = getClass().toString();
    private DiscoveryManager<?> mDiscoveryManager;
    private ArrayAdapter<DICommAppliance> mApplianceAdapter;
    private DICommPortListener mWifiPortListener = new DICommPortListener() {

        @Override
        public void onPortUpdate(final DICommPort<?> port) {
            Log.d(TAG, "onPortUpdate() called with: " + "port = [" + port + "]");
            WifiPortProperties portProperties = ((WifiPort) port).getPortProperties();
            if (portProperties != null) {
                Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
            }
        }

        @Override
        public void onPortError(final DICommPort<?> port, final com.philips.cdp.dicommclient.request.Error error, final String errorData) {
            Log.d(TAG, "onPortError() called with: " + "port = [" + port + "], error = [" + error + "], errorData = [" + errorData + "]");
        }
    };
    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mApplianceAdapter.clear();
                    mApplianceAdapter.addAll(mDiscoveryManager.getAllDiscoveredAppliances());
                }
            });

            for (DICommAppliance appliance : mDiscoveryManager.getAllDiscoveredAppliances()) {
                appliance.getWifiPort().addPortListener(mWifiPortListener);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom);

        mApplianceAdapter = new ArrayAdapter<DICommAppliance>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                DICommAppliance appliance = getItem(position);
                ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));
                return view;
            }
        };

        final ListView listViewAppliances = (ListView) findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(mApplianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                CurrentApplianceManager.getInstance().setCurrentAppliance(mApplianceAdapter.getItem(position));
                startActivity(new Intent(DiComActivity.this, DetailActivity.class));
            }
        });

        mDiscoveryManager = DiscoveryManager.getInstance();

        ((TextView) findViewById(R.id.textViewAppId)).setText(DICommClientWrapper.getAppId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mDiscoveryManager.addDiscoveryEventListener(discoveryEventListener);
        mDiscoveryManager.start();

        mApplianceAdapter.clear();
        mApplianceAdapter.addAll(mDiscoveryManager.getAllDiscoveredAppliances());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDiscoveryManager.removeDiscoverEventListener(discoveryEventListener);
        mDiscoveryManager.stop();
    }
}


