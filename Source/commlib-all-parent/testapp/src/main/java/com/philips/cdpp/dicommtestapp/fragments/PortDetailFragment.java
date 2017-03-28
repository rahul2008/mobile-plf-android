package com.philips.cdpp.dicommtestapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdpp.dicommtestapp.R;
import com.philips.cdpp.dicommtestapp.appliance.PropertyPort;
import com.philips.cdpp.dicommtestapp.appliance.property.PortSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.Property;
import com.philips.cdpp.dicommtestapp.presenters.PortDetailPresenter;

import java.util.ArrayList;

import nl.rwslinkman.presentable.Presenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortDetailFragment extends DiCommTestAppFragment<Property>
{
    private static final String TAG = "PortDetailFragment";
    private DICommPortListener<PropertyPort> portListener = new DICommPortListener<PropertyPort>() {
        @Override
        public void onPortUpdate(PropertyPort propertyPort) {
            Log.e(TAG, "onPortUpdate on " + propertyPort.getPortName() + ": " + propertyPort.toString());
            notifyListUpdated();
        }

        @Override
        public void onPortError(PropertyPort propertyPort, Error error, String s) {
            Log.e(TAG, "onPortError on " + propertyPort.getPortName() + ": " + error.getErrorMessage());
            String msgLines[] = s.split("\\r?\\n");
            propertyPort.setErrorText(msgLines[0]);
            notifyListUpdated();
        }
    };

    public static PortDetailFragment newInstance() {
        Bundle args = new Bundle();
        PortDetailFragment fragment = new PortDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PortDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        PropertyPort currentPort = getMainActivity().getPort();
        currentPort.addPortListener(portListener);
        currentPort.reloadProperties();

        PortSpecification portSpec = currentPort.getPortSpecification();
        updateList(new ArrayList<>(portSpec.getProperties()));

        titleView.setText(getString(R.string.port_detail_title_message));
        String msg = String.format("Available in '%s' port", currentPort.getPortName());
        subtitleView.setText(msg);
    }

    @Override
    public void onPause() {
        super.onPause();

        PropertyPort currentPort = getMainActivity().getPort();
        currentPort.removePortListener(portListener);
    }

    @Override
    Presenter getListPresenter() {
        return new PortDetailPresenter();
    }
}
