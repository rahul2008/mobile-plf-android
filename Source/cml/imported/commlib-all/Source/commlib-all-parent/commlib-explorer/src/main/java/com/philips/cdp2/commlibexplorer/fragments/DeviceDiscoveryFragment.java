/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlibexplorer.MainActivity;
import com.philips.cdp2.commlibexplorer.R;
import com.philips.cdp2.commlibexplorer.appliance.GenericAppliance;
import com.philips.cdp2.commlibexplorer.background.BackgroundConnectionService;
import com.philips.cdp2.commlibexplorer.presenters.DiscoveredDevicePresenter;
import com.philips.cdp2.commlibexplorer.strategy.CommStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import nl.rwslinkman.presentable.Presenter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

public class DeviceDiscoveryFragment extends CommLibExplorerAppFragment<GenericAppliance> implements PresentableItemClickListener<GenericAppliance> {
    private static final String TAG = "DeviceDiscoveryFragment";

    private DiscoveryEventListener discoveryListener = new DiscoveryEventListener() {
        @Override
        public void onDiscoveredAppliancesListChanged() {
            Log.e(TAG, "onDiscoveredAppliancesListChanged: ");
            updateDiscoveredDeviceList();
        }
    };
    private ApplianceManager.ApplianceListener<Appliance> applianceListener = new ApplianceManager.ApplianceListener<Appliance>() {
        @Override
        public void onApplianceFound(@NonNull Appliance appliance) {
            String msg = String.format("onApplianceFound: %s", appliance.getName());
            Log.e(TAG, msg);
            updateDiscoveredDeviceList();
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance appliance) {
            String msg = String.format("onApplianceUpdated: %s", appliance.getName());
            Log.e(TAG, msg);
            updateDiscoveredDeviceList();
        }

        @Override
        public void onApplianceLost(@NonNull Appliance lostAppliance) {
            String msg = String.format("onApplianceLost: %s", lostAppliance.getName());
            Log.e(TAG, msg);
            updateDiscoveredDeviceList();
        }
    };
    private CommStrategy usedStrategy;

    public DeviceDiscoveryFragment() {
        // NOP
    }

    public static DeviceDiscoveryFragment newInstance() {
        Bundle args = new Bundle();
        DeviceDiscoveryFragment fragment = new DeviceDiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    Presenter getListPresenter() {
        return new DiscoveredDevicePresenter();
    }

    @Override
    public void onResume() {
        super.onResume();

        BackgroundConnectionService service = getConnectionService();
        usedStrategy = service.getCommStrategy();

        setListItemClickListener(this);
        CommCentral commCentral = service.getCommCentral();

        String subtitleMsg;
        if (commCentral == null) {
            subtitleMsg = "Error selecting strategy";
        } else {
            updateDiscoveredDeviceList();

            // TODO Update when CommLib Discovery moves away from DiscoveryManager
            DiscoveryManager.getInstance().addDiscoveryEventListener(discoveryListener);
            commCentral.getApplianceManager().addApplianceListener(applianceListener);
            startDiscovery(commCentral);

            subtitleMsg = String.format("Using %s as TransportContext", usedStrategy.getType().value());
        }

        titleView.setText(getString(R.string.device_discovery_title_message));
        subtitleView.setText(subtitleMsg);
    }

    private void startDiscovery(CommCentral commCentral) {
        switch (usedStrategy.getType()) {
            case LAN:
                // WiFi discovery
                DiscoveryManager.getInstance().start();
                break;
            case BLE:
                // Bluetooth discovery
                startScanning(commCentral, getMainActivity());
                break;
            default:
                // None
                Log.wtf(TAG, "Non-existing strategy chosen. Unable to start scanning");
                break;
        }
    }

    @Override
    public void onPause() {
        DiscoveryManager.getInstance().removeDiscoverEventListener(discoveryListener);
        DiscoveryManager.getInstance().stop();
        getConnectionService().getCommCentral().getApplianceManager().removeApplianceListener(applianceListener);
        super.onPause();
    }

    private void startScanning(final CommCentral central, final MainActivity activity) {
        try {
            central.startDiscovery();
        } catch (MissingPermissionException e) {
            Log.e(TAG, "Missing permission, trying to acquire");
            activity.acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startScanning(central, activity);
                }
            });
        }
    }

    private void updateDiscoveredDeviceList() {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Set<Appliance> availableAppliances;
                switch (usedStrategy.getType()) {
                    case BLE:
                        // BLE devices in CommCentral
                        CommCentral central = getConnectionService().getCommCentral();
                        availableAppliances = central.getApplianceManager().getAvailableAppliances();
                        break;
                    case LAN:
                        // LAN devices in DiscoveryManager
                        availableAppliances = new CopyOnWriteArraySet<>();
                        availableAppliances.addAll(DiscoveryManager.getInstance().getAllDiscoveredAppliances());
                        break;
                    default:
                        availableAppliances = null;
                        break;
                }

                if (availableAppliances == null) {
                    Log.e(TAG, "run: No devices found because no CommStrategy was set");
                    return;
                }

                List<GenericAppliance> applianceList = convertToList(availableAppliances);
                updateList(applianceList);
            }
        });
    }

    @Override
    public void onItemClicked(GenericAppliance item) {
        navigateToPortOverview(item);
    }

    private void navigateToPortOverview(GenericAppliance item) {
        MainActivity activity = (MainActivity) getActivity();
        activity.storeAppliance(item);

        PortOverviewFragment portOverviewFragment = PortOverviewFragment.newInstance();
        activity.navigateTo(portOverviewFragment);
    }

    @NonNull
    private List<GenericAppliance> convertToList(Collection<Appliance> availableAppliances) {
        List<GenericAppliance> applianceList = new ArrayList<>();
        for (Appliance appl : availableAppliances) {
            if (appl instanceof GenericAppliance) {
                applianceList.add((GenericAppliance) appl);
            }
        }
        return applianceList;
    }
}
