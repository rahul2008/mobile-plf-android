/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.fragments;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlibexplorer.R;

public class PairingPortFragment extends BaseFragment {

    private static final String TAG = "PairingPort";
    private View snackbarLayout;

    public PairingPortFragment() {
        // Required empty public constructor
    }

    private DICommPortListener<PairingPort> portListener = new DICommPortListener<PairingPort>() {
        @Override
        public void onPortUpdate(PairingPort propertyPort) {
            Log.d(TAG, "onPortUpdate on " + propertyPort.getDICommPortName() + ": " + propertyPort.toString());
            if (snackbarLayout != null) {
                Snackbar.make(snackbarLayout, "Done", Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPortError(PairingPort propertyPort, Error error, String s) {
            Log.e(TAG, "onPortError on " + propertyPort.getDICommPortName() + ": " + error.getErrorMessage());
            if (snackbarLayout != null) {
                Snackbar.make(snackbarLayout, "Error: " + error.getErrorMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().getPort().addPortListener(portListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().getPort().removePortListener(portListener);
    }

    @Override
    final void setupFragmentView(final View fragmentView) {
        fragmentView.findViewById(R.id.pair_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((PairingPort) getMainActivity().getPort()).triggerPairing(
                        getTextFor(R.id.client_provider, fragmentView),
                        getTextFor(R.id.client_type, fragmentView),
                        getTextFor(R.id.client_id, fragmentView),
                        getTextFor(R.id.secret_key, fragmentView),
                        getTextFor(R.id.type, fragmentView),
                        getTextFor(R.id.permissions, fragmentView).split(",")
                );
            }
        });

        snackbarLayout = fragmentView;
    }

    private String getTextFor(int id, View fragmentView) {
        return ((TextInputEditText) fragmentView.findViewById(id)).getText().toString();
    }

    @Override
    final protected int getLayoutId() {
        return R.layout.fragment_port_pairing;
    }
}
