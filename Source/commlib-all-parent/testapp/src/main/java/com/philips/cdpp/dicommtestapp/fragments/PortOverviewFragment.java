package com.philips.cdpp.dicommtestapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdpp.dicommtestapp.MainActivity;
import com.philips.cdpp.dicommtestapp.R;
import com.philips.cdpp.dicommtestapp.appliance.GenericAppliance;
import com.philips.cdpp.dicommtestapp.appliance.PropertyPort;
import com.philips.cdpp.dicommtestapp.presenters.PortOverviewPresenter;

import java.util.List;

import nl.rwslinkman.presentable.Presenter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortOverviewFragment extends SampleAppFragment<PropertyPort> implements PresentableItemClickListener<PropertyPort> {
    private static final String TAG = "PortOverviewFragment";

    private DICommPortListener<PropertyPort> mPortListener = new DICommPortListener<PropertyPort>() {
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
    private ApplianceManager.ApplianceListener<GenericAppliance> mApplianceListener = new ApplianceManager.ApplianceListener<GenericAppliance>() {
        @Override
        public void onApplianceFound(@NonNull GenericAppliance appliance) {
            String msg = String.format("onApplianceFound: %s", appliance.getName());
            Log.d(TAG, msg);
            notifyListUpdated();
        }

        @Override
        public void onApplianceUpdated(@NonNull GenericAppliance appliance) {
            String msg = String.format("onApplianceUpdated: %s", appliance.getName());
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
        applianceManager.addApplianceListener(mApplianceListener);

        // TODO Remove when ApplianceManager allows to listen for appliance changes
        GenericAppliance currentAppliance = getMainActivity().getAppliance();
        List<PropertyPort> portList = currentAppliance.getPropertyPorts();
        // Show ports of the selected appliance
        updateList(portList);

        titleView.setText(getString(R.string.ports_overview_title_message));
        String subtitleText = String.format("Available on %s", currentAppliance.getModelNumber());
        subtitleView.setText(subtitleText);

        for(PropertyPort port : portList)
        {
            port.addPortListener(mPortListener);
            port.setEnabled(false);
            port.reloadProperties();
        }
    }

    @Override
    public void onPause() {
        CommCentral central = getConnectionService().getCommCentral();
        central.getApplianceManager().removeApplianceListener(mApplianceListener);

        // TODO Remove when ApplianceManager allows to listen for appliance changes
        GenericAppliance currentAppliance = getMainActivity().getAppliance();
        for(PropertyPort port : currentAppliance.getPropertyPorts())
        {
            port.removePortListener(mPortListener);
        }
        super.onPause();
    }

    @Override
    public void onItemClicked(PropertyPort item) {
        if(!item.isEnabled()) {
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
