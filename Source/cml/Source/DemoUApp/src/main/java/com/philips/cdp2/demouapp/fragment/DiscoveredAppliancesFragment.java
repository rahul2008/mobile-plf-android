/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.philips.cdp2.demouapp.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp2.commlib.core.CommCentral.getAppIdProvider;

public class DiscoveredAppliancesFragment extends Fragment {

    private static final String TAG = "DiscoveredApplFragment";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private Runnable permissionCallback;

    private CommCentral commCentral;

    private DiscoveryStrategy lanDiscoveryStrategy;
    private DiscoveryStrategy bleDiscoveryStrategy;

    private final AppIdProvider appIdProvider = getAppIdProvider();

    private View view;
    private ApplianceAdapter applianceAdapter;

    private final Set<String> discoveryFilterModelIds = new HashSet<>();

    private Switch lanDiscoverySwitch;
    private Switch bleDiscoverySwitch;

    private AppIdProvider.AppIdListener appIdListener = new AppIdProvider.AppIdListener() {
        @Override
        public void onAppIdChanged(String appId) {
            updateAppId();
        }
    };

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
            UiUtils.showMessage(getActivity(), view, getString(R.string.cml_lan_discovery_failed_to_start));
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
            UiUtils.showMessage(getActivity(), view, getString(R.string.cml_ble_discovery_failed_to_start));
        }
    };

    private void onAppliancesChanged() {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    applianceAdapter.clear();

                    final Set<Appliance> appliances = commCentral.getApplianceManager().getAvailableAppliances();
                    Collections.sort(new ArrayList<>(appliances), new Comparator<Appliance>() {
                        @Override
                        public int compare(Appliance o1, Appliance o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    for (Appliance appliance : appliances) {
                        appliance.getWifiPort().addPortListener(wifiPortListener);
                    }
                    applianceAdapter.addAll(appliances);
                }
            });
        }
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
        lanDiscoverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startLanDiscovery();
                } else {
                    stopLanDiscovery();
                }
            }
        });

        bleDiscoverySwitch = view.findViewById(R.id.cml_sw_startstop_ble_discovery);
        bleDiscoverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startBleDiscovery();
                } else {
                    stopBleDiscovery();
                }
            }
        });

        final ListView listViewAppliances = view.findViewById(R.id.cml_listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Appliance appliance = applianceAdapter.getItem(position);

                if (appliance != null) {
                    CommlibUapp.get().nextFragment(ApplianceFragmentFactory.newInstance(ApplianceFragment.class, appliance));
                }
            }
        });

        final FloatingActionButton fab = view.findViewById(R.id.cml_clearAppliancesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commCentral.clearDiscoveredAppliances();
            }
        });

        return view;
    }

    private void updateAppId() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) view.findViewById(R.id.cml_textViewAppId)).setText(appIdProvider.getAppId());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        commCentral.getApplianceManager().addApplianceListener(applianceListener);

        applianceAdapter.clear();
        applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());

        appIdProvider.addAppIdListener(appIdListener);
        updateAppId();
    }

    @Override
    public void onPause() {
        super.onPause();

        commCentral.getApplianceManager().removeApplianceListener(applianceListener);

        appIdProvider.removeAppIdListener(appIdListener);
    }

    private void startLanDiscovery() {
        try {
            lanDiscoveryStrategy.start(discoveryFilterModelIds);
        } catch (MissingPermissionException e) {
            Log.e(TAG, "Error starting discovery: " + e.getMessage());

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startLanDiscovery();
                }
            });
        }
    }

    private void stopLanDiscovery() {
        lanDiscoveryStrategy.stop();
        lanDiscoverySwitch.setChecked(false);
    }

    private void startBleDiscovery() {
        try {
            bleDiscoveryStrategy.start(discoveryFilterModelIds);
        } catch (MissingPermissionException e) {
            Log.e(TAG, "Error starting discovery: " + e.getMessage());

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startBleDiscovery();
                }
            });
        }
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(this.permissionCallback);
                }
            }
        }
    }

    private void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private void updateModelIds(final String commaSeparatedModelIds) {
        discoveryFilterModelIds.clear();

        if (!TextUtils.isEmpty(commaSeparatedModelIds)) {
            discoveryFilterModelIds.addAll(Arrays.asList((commaSeparatedModelIds).split(",")));
        }
    }
}
