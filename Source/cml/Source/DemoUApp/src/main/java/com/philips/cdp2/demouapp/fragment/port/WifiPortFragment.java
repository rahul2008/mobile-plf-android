/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptNewPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.rejectNewPinFor;
import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;

public class WifiPortFragment extends Fragment {

    private static final String TAG = "WifiPortFragment";
    private static final String PROPERTY_SSID = "ssid";
    private static final String PROPERTY_PASSWORD = "password";
    private static final String PROPERTY_PROTECTION = "protection";
    private static final String PROPERTY_IPADDRESS = "ipaddress";
    private static final String PROPERTY_NETMASK = "netmask";
    private static final String PROPERTY_GATEWAY = "gateway";
    private static final String PROPERTY_DHCP = "dhcp";
    private static final String PROPERTY_MACADDRESS = "macaddress";
    private static final String PROPERTY_CPPID = "cppid";
    private static final String PROPERTY_CTN = "ctn";
    private static final String PROPERTY_TRAVEL_SSID = "travelssid";
    private static final String PROPERTY_TRAVEL_PASSWORD = "travelpassword";

    private View rootview;
    private EditText ssidEdit;
    private EditText passwordEdit;
    private EditText protectionEdit;
    private EditText ipAddressEdit;
    private EditText netmaskEdit;
    private EditText gatewayEdit;
    private EditText macAddressEdit;
    private EditText cppIDEdit;
    private EditText travelSsidEdit;
    private EditText travelPasswordEdit;
    private Appliance currentAppliance;

    private DICommPortListener<WifiPort> portListener = new DICommPortListener<WifiPort>() {
        @Override
        public void onPortUpdate(WifiPort port) {
            if (isAdded()) {
                WifiPortProperties properties = port.getPortProperties();
                if (properties == null) {
                    return;
                }
                ssidEdit.setText(properties.getSsid());
                passwordEdit.setText(properties.getPassword());
                protectionEdit.setText(properties.getProtection());
                ipAddressEdit.setText(properties.getIpaddress());
                netmaskEdit.setText(properties.getNetmask());
                gatewayEdit.setText(properties.getGateway());
                macAddressEdit.setText(properties.getMacaddress());
                cppIDEdit.setText(properties.getCppid());
                travelSsidEdit.setText(properties.getTravelSsid());
                travelPasswordEdit.setText(properties.getTravelPassword());
            }
        }

        @Override
        public void onPortError(WifiPort port, Error error, @Nullable String errorData) {
            DICommLog.e(TAG, String.format(Locale.US, "Wifi port error: [%s], data: [%s]", error.getErrorMessage(), errorData));

            if (isAdded() && error == Error.INSECURE_CONNECTION) {
                promptCertificateMismatch();
            }
        }
    };

    private final DialogInterface.OnClickListener mismatchDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_NEUTRAL:
                    dialog.dismiss();
                    break;
                case BUTTON_NEGATIVE:
                    if (currentAppliance != null) rejectNewPinFor(currentAppliance);
                    break;
                case BUTTON_POSITIVE:
                    if (currentAppliance != null) acceptNewPinFor(currentAppliance);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cml_fragment_wifi_port, container, false);
        ssidEdit = rootview.findViewById(R.id.cml_ssid);
        passwordEdit = rootview.findViewById(R.id.cml_password);
        protectionEdit = rootview.findViewById(R.id.cml_protection);
        ipAddressEdit = rootview.findViewById(R.id.cml_ipaddress);
        netmaskEdit = rootview.findViewById(R.id.cml_netmask);
        gatewayEdit = rootview.findViewById(R.id.cml_gateway);
        macAddressEdit = rootview.findViewById(R.id.cml_macaddress);
        cppIDEdit = rootview.findViewById(R.id.cml_cppid);
        travelSsidEdit = rootview.findViewById(R.id.cml_travelssid);
        travelPasswordEdit = rootview.findViewById(R.id.cml_travelpassword);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        currentAppliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);

        Button setButton = rootview.findViewById(R.id.cml_btn_set);
        Button getButton = rootview.findViewById(R.id.cml_btn_get);

        ((CompoundButton) rootview.findViewById(R.id.cml_switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        setButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentAppliance.getWifiPort().putProperties(getPortProperties());
                    }
                }
        );

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAppliance.getWifiPort().reloadProperties();
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentAppliance == null) {
            getFragmentManager().popBackStack();
            return;
        }

        currentAppliance.getWifiPort().addPortListener(portListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (currentAppliance != null) {
            currentAppliance.getWifiPort().removePortListener(portListener);
        }
    }

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.getWifiPort().subscribe();
            } else {
                currentAppliance.getWifiPort().unsubscribe();
            }
        }
    };

    private void promptCertificateMismatch() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.cml_prompt_certificate_mismatch_message);
        builder.setTitle(R.string.cml_prompt_certificate_mismatch_title);

        builder.setNeutralButton(R.string.cml_prompt_certificate_mismatch_cancel, mismatchDialogClickListener);
        builder.setNegativeButton(R.string.cml_prompt_certificate_mismatch_reject, mismatchDialogClickListener);
        builder.setPositiveButton(R.string.cml_prompt_certificate_mismatch_accept, mismatchDialogClickListener);

        builder.show();
    }

    private Map<String, Object> getPortProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put(PROPERTY_TRAVEL_SSID, travelSsidEdit.getText().toString());
        map.put(PROPERTY_TRAVEL_PASSWORD, travelPasswordEdit.getText().toString());
        return map;
    }
}
