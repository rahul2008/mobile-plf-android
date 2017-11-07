/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.util.UiUtils;

import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.readPin;

public class PinFragment extends Fragment {

    private static final String TAG = "PinFragment";

    private EditText txtPin;

    private Appliance currentAppliance;
    private View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cml_fragment_pinning, container, false);

        rootview.findViewById(R.id.cml_btnGetPin).setOnClickListener(buttonClickListener);
        rootview.findViewById(R.id.cml_btnSetPin).setOnClickListener(buttonClickListener);

        txtPin = rootview.findViewById(R.id.cml_txtPin);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        Appliance appliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        if (appliance == null) {
            getFragmentManager().popBackStack();
            return;
        }
        currentAppliance = appliance;
    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            if (viewId == R.id.cml_btnGetPin) {
                txtPin.setText(readPin(currentAppliance));
            } else if (viewId == R.id.cml_btnSetPin) {
                String newPin = txtPin.getText().toString();

                //set to null if empty string, to enable "virgin" state
                if (newPin.equals("")) {
                    newPin = null;
                }

                try {
                    acceptPinFor(currentAppliance, newPin);
                } catch (IllegalArgumentException e) {
                    UiUtils.showMessage(getActivity(), rootview, getString(R.string.base64_pin_error));
                }
            } else {
                DICommLog.d(TAG, "Unknown view clicked");
            }
        }
    };
}
