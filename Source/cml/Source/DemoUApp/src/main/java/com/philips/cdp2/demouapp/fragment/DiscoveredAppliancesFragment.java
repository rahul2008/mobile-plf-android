/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager.ApplianceListener;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.util.AppIdProvider;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.ApplianceAdapter;
import com.philips.cdp2.demouapp.util.PermissionResultListener;
import com.philips.cdp2.demouapp.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp2.commlib.core.CommCentral.getAppIdProvider;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;

public class DiscoveredAppliancesFragment extends Fragment {

    private static final String TAG = "DiscoveredApplFragment";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private PermissionResultListener permissionResultListener;

    private CommCentral commCentral;

    private DiscoveryStrategy lanDiscoveryStrategy;
    private DiscoveryStrategy bleDiscoveryStrategy;

    private final AppIdProvider appIdProvider = getAppIdProvider();

    private View view;
    private ApplianceAdapter applianceAdapter;

    private final Set<String> discoveryFilterModelIds = new HashSet<>();

    private Switch lanDiscoverySwitch;
    private Switch bleDiscoverySwitch;

    private AppIdProvider.AppIdListener appIdListener = appId -> updateAppId();

    private DiscoveryListener lanDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onNetworkNodeDiscovered(final NetworkNode networkNode) {

        }

        @Override
        public void onNetworkNodeLost(final NetworkNode networkNode) {

        }

        @Override
        public void onDiscoveryStopped() {

        }

        @Override
        public void onDiscoveryFailedToStart() {
            UiUtils.showMessage(view, getString(R.string.cml_lan_discovery_failed_to_start));
        }
    };

    private DiscoveryListener bleDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onNetworkNodeDiscovered(final NetworkNode networkNode) {

        }

        @Override
        public void onNetworkNodeLost(final NetworkNode networkNode) {

        }

        @Override
        public void onDiscoveryStopped() {
            
        }

        @Override
        public void onDiscoveryFailedToStart() {
            UiUtils.showMessage(view, getString(R.string.cml_ble_discovery_failed_to_start));
        }
    };

    private void onAppliancesChanged() {
        applianceAdapter.clear();

        final Set<Appliance> appliances = commCentral.getApplianceManager().getAppliances();
        Collections.sort(new ArrayList<>(appliances), (o1, o2) -> o1.getName().compareTo(o2.getName()));
        for (Appliance appliance : appliances) {
            appliance.getWifiPort().addPortListener(wifiPortListener);
        }
        applianceAdapter.addAll(appliances);
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

    private ApplianceListener applianceListener = new ApplianceListener() {
        @Override
        public void onApplianceFound(@NonNull Appliance foundAppliance) {
            onAppliancesChanged();
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance updatedAppliance) {
            onAppliancesChanged();
        }

        @Override
        public void onApplianceLost(@NonNull Appliance lostAppliance) {
            onAppliancesChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cml_fragment_discovered_appliances, container, false);

        commCentral = CommlibUapp.get().getDependencies().getCommCentral();
        lanDiscoveryStrategy = commCentral.getTransportContext(LanTransportContext.class).getDiscoveryStrategy();
        bleDiscoveryStrategy = commCentral.getTransportContext(BleTransportContext.class).getDiscoveryStrategy();

        lanDiscoveryStrategy.addDiscoveryListener(lanDiscoveryListener);
        bleDiscoveryStrategy.addDiscoveryListener(bleDiscoveryListener);

        applianceAdapter = new ApplianceAdapter(getContext());

        final EditText editFilterModelId = view.findViewById(R.id.editFilterModelId);
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

        lanDiscoverySwitch = view.findViewById(R.id.cml_sw_startstop_lan_discovery);
        lanDiscoverySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startLanDiscovery();
            } else {
                stopLanDiscovery();
            }
        });

        bleDiscoverySwitch = view.findViewById(R.id.cml_sw_startstop_ble_discovery);
        bleDiscoverySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startBleDiscovery();
            } else {
                stopBleDiscovery();
            }
        });

        final ListView listViewAppliances = view.findViewById(R.id.cml_listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener((parent, view, position, id) -> {
            final Appliance appliance = applianceAdapter.getItem(position);

            if (appliance != null) {
                CommlibUapp.get().nextFragment(ApplianceFragmentFactory.newInstance(ApplianceFragment.class, appliance));
            }
        });

        final FloatingActionButton fab = view.findViewById(R.id.cml_clearAppliancesButton);
        fab.setOnClickListener(v -> commCentral.clearDiscoveredAppliances());

        return view;
    }

    private void updateAppId() {
        ((TextView) view.findViewById(R.id.cml_textViewAppId)).setText(appIdProvider.getAppId());
    }

    @Override
    public void onResume() {
        super.onResume();

        commCentral.getApplianceManager().addApplianceListener(applianceListener);

        applianceAdapter.clear();
        applianceAdapter.addAll(commCentral.getApplianceManager().getAppliances());

        appIdProvider.addAppIdListener(appIdListener);
        updateAppId();
    }

    @Override
    public void onPause() {
        super.onPause();

        commCentral.getApplianceManager().removeApplianceListener(applianceListener);

        appIdProvider.removeAppIdListener(appIdListener);
    }

    private Boolean tryStartingDiscovery(DiscoveryStrategy discoveryStrategy) {
        try {
            discoveryStrategy.start(discoveryFilterModelIds);
            return true;
        } catch (MissingPermissionException e) {
            String errorMessage = "Error starting discovery for " + discoveryStrategy.getClass().getSimpleName() + ": " + e.getMessage();
            Log.e(TAG, errorMessage);
            showIndefiniteMessage(view, errorMessage);
            return false;
        }
    }

    private void startLanDiscovery() {
        lanDiscoverySwitch.setChecked(tryStartingDiscovery(lanDiscoveryStrategy));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            return;
        }

        // We need location permissions in order to read the current WiFi SSID.
        // Without it, the WiFi SSID is always "<unknown ssid>"
        acquirePermission(new PermissionResultListener() {
            @Override
            public void onPermissionGranted() {}

            @Override
            public void onPermissionDenied() {
                showIndefiniteMessage(view, "⚠️ Cloud communication will not work without location permissions!");
            }
        });
    }

    private void stopLanDiscovery() {
        lanDiscoveryStrategy.stop();
        lanDiscoverySwitch.setChecked(false);
    }

    private void startBleDiscovery() {
        acquirePermission(new PermissionResultListener() {
            @Override
            public void onPermissionGranted() {
                bleDiscoverySwitch.setChecked(tryStartingDiscovery(bleDiscoveryStrategy));
            }

            @Override
            public void onPermissionDenied() {
                showIndefiniteMessage(view, "BLE discovery requires location permissions!");
                bleDiscoverySwitch.setChecked(false);
            }
        });
    }

    private void stopBleDiscovery() {
        bleDiscoveryStrategy.stop();
        bleDiscoverySwitch.setChecked(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLanDiscovery();
        lanDiscoveryStrategy.removeDiscoveryListener(lanDiscoveryListener);
        bleDiscoveryStrategy.removeDiscoveryListener(bleDiscoveryListener);
    }

    public static DiscoveredAppliancesFragment newInstance() {
        return new DiscoveredAppliancesFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    int grantResult = grantResults[0];
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        this.permissionResultListener.onPermissionGranted();
                    } else {
                        this.permissionResultListener.onPermissionDenied();
                    }
                    this.permissionResultListener = null;
                }
            }
        }
    }

    private void acquirePermission(@NonNull PermissionResultListener permissionResultListener) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionResultListener.onPermissionGranted();
        } else {
            this.permissionResultListener = permissionResultListener;
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private void updateModelIds(final String commaSeparatedModelIds) {
        discoveryFilterModelIds.clear();

        if (!TextUtils.isEmpty(commaSeparatedModelIds)) {
            discoveryFilterModelIds.addAll(Arrays.asList((commaSeparatedModelIds).split(",")));
        }
    }
}
