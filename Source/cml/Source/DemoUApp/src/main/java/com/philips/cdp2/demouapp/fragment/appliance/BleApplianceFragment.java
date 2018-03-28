/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.appliance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.cdp2.demouapp.port.ble.BleParamsPort;
import com.philips.cdp2.demouapp.port.ble.BleParamsPortProperties;

import java.util.HashMap;
import java.util.Map;

public class BleApplianceFragment extends Fragment {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private TextView txtBleParams;
    private Switch switchContinuousConnection;

    private BleReferenceAppliance currentAppliance;

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cml_btnSetBleParams) {
                final Map<String, Object> params = new HashMap<>();

                // TODO obtain values from UI
                params.put("connmin", 15);
                params.put("connmax", 30);
                params.put("slowadenabled", false);

                currentAppliance.getBleParamsPort().putProperties(params);
            }
        }
    };

    private final DICommPortListener<BleParamsPort> portListener = new DICommPortListener<BleParamsPort>() {
        @Override
        public void onPortUpdate(BleParamsPort port) {
            if (isAdded()) {
                BleParamsPortProperties properties = port.getPortProperties();
                if (properties == null) {
                    return;
                }
                updateResult(gson.toJson(properties));
            }
        }

        @Override
        public void onPortError(BleParamsPort port, Error error, @Nullable String errorData) {
            updateResult(error.getErrorMessage());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cml_fragment_ble_appliance, container, false);

        switchContinuousConnection = rootView.findViewById(R.id.cml_switch_continuous_connection);
        switchContinuousConnection.setChecked(true);
        switchContinuousConnection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                handleContinuousConnectionChanged();
            }
        });

        txtBleParams = rootView.findViewById(R.id.cml_txtBleParams);

        final Button btnSetParams = rootView.findViewById(R.id.cml_btnSetBleParams);
        btnSetParams.setOnClickListener(buttonClickListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Appliance appliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        if (appliance == null || !(appliance instanceof BleReferenceAppliance)) {
            getFragmentManager().popBackStack();
            return;
        }

        currentAppliance = (BleReferenceAppliance) appliance;
        currentAppliance.getBleParamsPort().addPortListener(portListener);
        currentAppliance.getBleParamsPort().reloadProperties();

        handleContinuousConnectionChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (currentAppliance != null) {
            currentAppliance.getBleParamsPort().removePortListener(portListener);
        }
    }

    private void handleContinuousConnectionChanged() {
        if (currentAppliance == null) {
            return;
        }

        if (switchContinuousConnection.isChecked()) {
            currentAppliance.enableCommunication();
        } else {
            currentAppliance.disableCommunication();
        }
    }

    private void updateResult(final String result) {
        if (isAdded()) {
            txtBleParams.setText(result);
        }
    }
}
