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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.cdp2.demouapp.port.ble.BleParamsPort;
import com.philips.cdp2.demouapp.port.ble.BleParamsPortProperties;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class BleApplianceFragment extends Fragment {

    private View rootView;
    private Switch switchContinuousConnection;
    private EditText editSlowAdMin;
    private EditText editSlowAdMax;
    private EditText editSlowAdTimeout;
    private Switch switchSlowAdEnabled;
    private EditText editFastAdMin;
    private EditText editFastAdMax;
    private EditText editFastAdTimeout;
    private EditText editConnMin;
    private EditText editConnMax;
    private EditText editConnSlaveLatency;
    private EditText editConnSuperTimeout;

    private BleReferenceAppliance currentAppliance;

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cml_btnGetBleParams) {
                currentAppliance.getBleParamsPort().reloadProperties();
            } else if (v.getId() == R.id.cml_btnSetBleParams) {
                final Map<String, Object> params = new HashMap<>();

                params.put("slowadmin", parseInt(valueOf(editSlowAdMin.getText())));
                params.put("slowadmax", parseInt(valueOf(editSlowAdMax.getText())));
                params.put("slowadtimeout", parseInt(valueOf(editSlowAdTimeout.getText())));
                params.put("slowadenabled", switchSlowAdEnabled.isChecked());
                params.put("fastadmin", parseInt(valueOf(editFastAdMin.getText())));
                params.put("fastadmax", parseInt(valueOf(editFastAdMax.getText())));
                params.put("fastadtimeout", parseInt(valueOf(editFastAdTimeout.getText())));
                params.put("connmin", parseInt(valueOf(editConnMin.getText())));
                params.put("connmax", parseInt(valueOf(editConnMax.getText())));
                params.put("connslavelatency", parseInt(valueOf(editConnSlaveLatency.getText())));
                params.put("connsupertimeout", parseInt(valueOf(editConnSuperTimeout.getText())));

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
                updateView(properties);
            }
        }

        @Override
        public void onPortError(BleParamsPort port, Error error, @Nullable String errorData) {
            showIndefiniteMessage(getActivity(), rootView, error.getErrorMessage());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cml_fragment_ble_appliance, container, false);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        final Appliance appliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);
        if (appliance instanceof BleReferenceAppliance) {
            currentAppliance = (BleReferenceAppliance) appliance;
        }
        switchContinuousConnection = rootView.findViewById(R.id.cml_switch_continuous_connection);
        switchContinuousConnection.setChecked(false);
        switchContinuousConnection.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                handleContinuousConnectionChanged();
            }
        });

        editSlowAdMin = rootView.findViewById(R.id.cml_edit_ble_slowadmin);
        editSlowAdMax = rootView.findViewById(R.id.cml_edit_ble_slowadmax);
        editSlowAdTimeout = rootView.findViewById(R.id.cml_edit_ble_slowadtimeout);
        switchSlowAdEnabled = rootView.findViewById(R.id.cml_switch_ble_slowadenabled);
        editFastAdMin = rootView.findViewById(R.id.cml_edit_ble_fastadmin);
        editFastAdMax = rootView.findViewById(R.id.cml_edit_ble_fastadmax);
        editFastAdTimeout = rootView.findViewById(R.id.cml_edit_ble_fastadtimeout);
        editConnMin = rootView.findViewById(R.id.cml_edit_ble_connmin);
        editConnMax = rootView.findViewById(R.id.cml_edit_ble_connmax);
        editConnSlaveLatency = rootView.findViewById(R.id.cml_edit_ble_connslavelatency);
        editConnSuperTimeout = rootView.findViewById(R.id.cml_edit_ble_connsupertimeout);

        final Button btnGetParams = rootView.findViewById(R.id.cml_btnGetBleParams);
        btnGetParams.setOnClickListener(buttonClickListener);
        final Button btnSetParams = rootView.findViewById(R.id.cml_btnSetBleParams);
        btnSetParams.setOnClickListener(buttonClickListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentAppliance == null) {
            getFragmentManager().popBackStack();
            return;
        }

        currentAppliance.getBleParamsPort().addPortListener(portListener);
        handleContinuousConnectionChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Don't keep the connection open when leaving the screen.
        switchContinuousConnection.setChecked(false);

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

    private void updateView(final BleParamsPortProperties properties) {
        if (isAdded()) {
            editSlowAdMin.setText(valueOf(properties.slowadmin));
            editSlowAdMax.setText(valueOf(properties.slowadmax));
            editSlowAdTimeout.setText(valueOf(properties.slowadtimeout));
            switchSlowAdEnabled.setChecked(properties.slowadenabled);
            editFastAdMin.setText(valueOf(properties.fastadmin));
            editFastAdMax.setText(valueOf(properties.fastadmax));
            editFastAdTimeout.setText(valueOf(properties.fastadtimeout));
            editConnMin.setText(valueOf(properties.connmin));
            editConnMax.setText(valueOf(properties.connmax));
            editConnSlaveLatency.setText(valueOf(properties.connslavelatency));
            editConnSuperTimeout.setText(valueOf(properties.connsupertimeout));
        }
    }
}
