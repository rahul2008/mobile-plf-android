/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;


public class DevicePortFragment extends Fragment {

    private Appliance currentAppliance;
    private EditText deviceNameEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_device_port, container, false);

        currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        deviceNameEdit = (EditText) rootview.findViewById(R.id.device_name);
        Button setButton = (Button) rootview.findViewById(R.id.btn_set);

        setButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentAppliance.getDevicePort().putProperties("name", deviceNameEdit.getText().toString());
                    }
                }
        );

        currentAppliance.getDevicePort().addPortListener(new DICommPortListener() {
            @Override
            public void onPortUpdate(DICommPort port) {
                String devicename = ((DevicePort) port).getPortProperties().getName();
                deviceNameEdit.setText(devicename);
            }

            @Override
            public void onPortError(DICommPort port, Error error, String errorData) {

            }
        });

        currentAppliance.getDevicePort().reloadProperties();

        return rootview;
    }
}
