package com.philips.cdpp.dicommtestapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdpp.dicommtestapp.MainActivity;
import com.philips.cdpp.dicommtestapp.R;
import com.philips.cdpp.dicommtestapp.appliance.GenericAppliance;
import com.philips.cdpp.dicommtestapp.background.BackgroundConnectionService;
import com.philips.cdpp.dicommtestapp.presenters.DiscoveredDevicePresenter;
import com.philips.cdpp.dicommtestapp.strategy.CommStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import nl.rwslinkman.presentable.Presenter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

public class DeviceDiscoveryFragment extends SampleAppFragment<GenericAppliance> implements PresentableItemClickListener<GenericAppliance> {
    private static final String TAG = "DeviceDiscoveryFragment";

    private DiscoveryEventListener mDiscoveryListener = new DiscoveryEventListener() {
        @Override
        public void onDiscoveredAppliancesListChanged() {
            Log.e(TAG, "onDiscoveredAppliancesListChanged: ");
            updateDiscoveredDeviceList();
        }
    };
    private ApplianceManager.ApplianceListener<Appliance> mApplianceListener = new ApplianceManager.ApplianceListener<Appliance>() {
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
    private CommStrategy mUsedStrategy;

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
        mUsedStrategy = service.getCommStrategy();

        setListItemClickListener(this);
        CommCentral commCentral = service.getCommCentral();

        String subtitleMsg;
        if(commCentral != null) {
            updateDiscoveredDeviceList();

            // TODO Update when CommLib Discovery moves away from DiscoveryManager
            DiscoveryManager.getInstance().addDiscoveryEventListener(mDiscoveryListener);
            commCentral.getApplianceManager().addApplianceListener(mApplianceListener);
            startDiscovery(commCentral);

            subtitleMsg = String.format("Using %s as TransportContext", mUsedStrategy.getType().value());
        } else {
            subtitleMsg = "Error selecting strategy";
        }

        titleView.setText(getString(R.string.device_discovery_title_message));
        subtitleView.setText(subtitleMsg);
    }

    private void startDiscovery(CommCentral commCentral) {
        if(mUsedStrategy.getType() == CommStrategy.CommStrategyType.LAN) {
            // WiFi discovery
            DiscoveryManager.getInstance().start();
        }
        else if(mUsedStrategy.getType() == CommStrategy.CommStrategyType.BLE) {
            // Bluetooth discovery
            startScanning(commCentral, getMainActivity());
        }
        else {
            // None
            Log.wtf(TAG, "Non-existing strategy chosen. Unable to start scanning");
        }
    }

    @Override
    public void onPause() {
        DiscoveryManager.getInstance().removeDiscoverEventListener(mDiscoveryListener);
        DiscoveryManager.getInstance().stop();
        getConnectionService().getCommCentral().getApplianceManager().removeApplianceListener(mApplianceListener);
        super.onPause();
    }

    private void startScanning(final CommCentral central, final MainActivity activity) {
        try {
            central.startDiscovery();
        } catch (MissingPermissionException e) {
            e.printStackTrace();
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
                List<? extends Appliance> availableAppliances;
                if(mUsedStrategy.getType() == CommStrategy.CommStrategyType.BLE) {
                    // BLE devices in CommCentral
                    CommCentral central = getConnectionService().getCommCentral();
                    Set<? extends Appliance> bleAppliances = central.getApplianceManager().getAvailableAppliances();
                    availableAppliances = convertToList(bleAppliances);
                } else if(mUsedStrategy.getType() == CommStrategy.CommStrategyType.LAN){
                    // LAN devices in DiscoveryManager
                    availableAppliances = DiscoveryManager.getInstance().getAllDiscoveredAppliances();
                } else {
                    availableAppliances = null;
                }

                if(availableAppliances == null) {
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
    private List<GenericAppliance> convertToList(Collection<? extends Appliance> availableAppliances) {
        List<GenericAppliance> applianceList = new ArrayList<>();
        for(Appliance appl : availableAppliances) {
            if(appl instanceof GenericAppliance) {
                applianceList.add((GenericAppliance) appl);
            }
        }
        return applianceList;
    }
}
