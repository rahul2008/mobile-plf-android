/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.fragments;

import android.os.Bundle;
import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlibexplorer.R;
import com.philips.cdp2.commlibexplorer.appliance.PropertyPort;
import com.philips.cdp2.commlibexplorer.appliance.property.PortSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.Property;
import com.philips.cdp2.commlibexplorer.presenters.PortDetailPresenter;

import java.util.ArrayList;

import nl.rwslinkman.presentable.Presenter;

public class PortDetailFragment extends CommLibExplorerAppFragment<Property> {
    private static final String TAG = "PortDetailFragment";
    private DICommPortListener<PropertyPort> portListener = new DICommPortListener<PropertyPort>() {
        @Override
        public void onPortUpdate(PropertyPort propertyPort) {
            Log.d(TAG, "onPortUpdate on " + propertyPort.getPortName() + ": " + propertyPort.toString());
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

        PropertyPort currentPort = (PropertyPort) getMainActivity().getPort();
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

        PropertyPort currentPort = (PropertyPort) getMainActivity().getPort();
        currentPort.removePortListener(portListener);
    }

    @Override
    Presenter getListPresenter() {
        return new PortDetailPresenter();
    }
}
