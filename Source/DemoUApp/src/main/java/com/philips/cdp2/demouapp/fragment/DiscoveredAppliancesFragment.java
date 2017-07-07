/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.ApplianceAdapter;

import static android.content.ContentValues.TAG;

public class DiscoveredAppliancesFragment extends Fragment {

    private DiscoveryManager<?> discoveryManager = DiscoveryManager.getInstance();
    private ApplianceAdapter applianceAdapter;

    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            getActivity().runOnUiThread(new Runnable() {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_discovered_appliances, container, false);

        applianceAdapter = new ApplianceAdapter(getContext());

        final ListView listViewAppliances = (ListView) rootview.findViewById(R.id.cml_listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Appliance appliance = applianceAdapter.getItem(position);
                CurrentApplianceManager.getInstance().setCurrentAppliance(appliance);

                CommlibUapp.get().nextFragment(new ApplianceFragment());
            }
        });

        ((TextView) rootview.findViewById(R.id.cml_textViewAppId)).setText(DICommClientWrapper.getAppId());

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        discoveryManager.addDiscoveryEventListener(discoveryEventListener);
        discoveryManager.start();

        applianceAdapter.clear();
        applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
    }

    @Override
    public void onPause() {
        super.onPause();

        discoveryManager.removeDiscoverEventListener(discoveryEventListener);
        discoveryManager.stop();
    }

    public static DiscoveredAppliancesFragment newInstance() {
        return new DiscoveredAppliancesFragment();
    }

}
