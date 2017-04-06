/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlibexplorer.MainActivity;
import com.philips.cdp2.commlibexplorer.R;
import com.philips.cdp2.commlibexplorer.appliance.GenericAppliance;
import com.philips.cdp2.commlibexplorer.appliance.PropertyPort;
import com.philips.cdp2.commlibexplorer.presenters.PortOverviewPresenter;

import java.util.ArrayList;
import java.util.Set;

import nl.rwslinkman.presentable.Presenter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

public class PortOverviewFragment extends DiCommTestAppFragment<PropertyPort> implements PresentableItemClickListener<PropertyPort> {
    private static final String TAG = "PortOverviewFragment";

    private DICommPortListener<PropertyPort> portListener = new DICommPortListener<PropertyPort>() {
        @Override
        public void onPortUpdate(PropertyPort propertyPort) {
            Log.e(TAG, "onPortUpdate on " + propertyPort.getPortName() + ": " + propertyPort.toString());
            propertyPort.setErrorText("");
            propertyPort.setStatusText("Online");
            propertyPort.setEnabled(true);
            notifyListUpdated();
        }

        @Override
        public void onPortError(PropertyPort propertyPort, Error error, String s) {
            String msgLines[] = s.split("\\r?\\n");
            String errorMsg = msgLines[0];
            Log.e(TAG, "onPortError on " + propertyPort.getPortName() + ": " + errorMsg);
            propertyPort.setErrorText(errorMsg);
            propertyPort.setEnabled(false);
            notifyListUpdated();
        }
    };
    private ApplianceManager.ApplianceListener<Appliance> applianceListener = new ApplianceManager.ApplianceListener<Appliance>() {
        @Override
        public void onApplianceFound(@NonNull Appliance appliance) {
            String msg = String.format("onApplianceFound: %s", appliance.getName());
            Log.d(TAG, msg);
            notifyListUpdated();
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance appliance) {
            String msg = String.format("onApplianceUpdated: %s", appliance.getName());
            Log.d(TAG, msg);
            notifyListUpdated();
        }

        @Override
        public void onApplianceLost(@NonNull Appliance lostAppliance) {
            String msg = String.format("onApplianceLost: %s", lostAppliance.getName());
            Log.d(TAG, msg);
            notifyListUpdated();
        }
    };

    public static PortOverviewFragment newInstance() {
        Bundle args = new Bundle();
        PortOverviewFragment fragment = new PortOverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PortOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    Presenter getListPresenter() {
        return new PortOverviewPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();

        setListItemClickListener(this);

        CommCentral central = getConnectionService().getCommCentral();
        ApplianceManager applianceManager = central.getApplianceManager();
        applianceManager.addApplianceListener(applianceListener);

        // TODO Remove when ApplianceManager allows to listen for appliance changes
        GenericAppliance currentAppliance = getMainActivity().getAppliance();
        Set<PropertyPort> portList = currentAppliance.getPropertyPorts();
        // Show ports of the selected appliance
        updateList(new ArrayList<>(portList));

        titleView.setText(getString(R.string.ports_overview_title_message));
        String subtitleText = String.format("Available on %s", currentAppliance.getModelNumber());
        subtitleView.setText(subtitleText);

        for (PropertyPort port : portList) {
            port.addPortListener(portListener);
            port.setEnabled(false);
            port.reloadProperties();
        }
    }

    @Override
    public void onPause() {
        CommCentral central = getConnectionService().getCommCentral();
        central.getApplianceManager().removeApplianceListener(applianceListener);

        // TODO Remove when ApplianceManager allows to listen for appliance changes
        GenericAppliance currentAppliance = getMainActivity().getAppliance();
        for (PropertyPort port : currentAppliance.getPropertyPorts()) {
            port.removePortListener(portListener);
        }
        super.onPause();
    }

    @Override
    public void onItemClicked(PropertyPort item) {
        if (!item.isEnabled()) {
            String toastMsg = String.format("The '%s' port is unavailable at this time.", item.getPortName());
            Toast.makeText(getMainActivity(), toastMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        // Only for available ports
        MainActivity activity = getMainActivity();
        activity.storePort(item);

        PortDetailFragment fragment = PortDetailFragment.newInstance();
        activity.navigateTo(fragment);
    }
}
