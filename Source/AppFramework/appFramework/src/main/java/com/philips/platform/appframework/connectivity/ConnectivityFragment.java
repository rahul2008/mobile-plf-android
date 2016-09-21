/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.User;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shineplugincopperlib.ShinePluginCopper;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;

public class ConnectivityFragment extends AppFrameworkBaseFragment implements View.OnClickListener, ConnectivityContract.View {
    public static final String TAG = ConnectivityFragment.class.getSimpleName();
    private EditText editText = null;
    private EditText momentValueEditText = null;
    private String editTextValue, momentId, nucleousValue;
    private String accessTokenValue;

    private TextView connectionState;
    private SHNCentral shnCentral;
    private SHNDevice shineDevice;
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;
    private User user;
    private ProgressDialog dialog = null;

    /**
     * Presenter object for Connectivity
     */
    private ConnectivityPresenter connectivityPresenter;

    public ConnectivityFragment() {
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.app_connectivity_title);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        connectivityPresenter = new ConnectivityPresenter(this, getActivity());
        View rootView = inflater.inflate(R.layout.af_connectivity_fragment, container, false);
        editText = (EditText) rootView.findViewById(R.id.nucleous_value_editbox);
        momentValueEditText = (EditText) rootView.findViewById(R.id.moment_value_editbox);
        Button btnGetMoment = (Button) rootView.findViewById(R.id.get_momentumvalue_button);
        btnGetMoment.setOnClickListener(this);
        Button btnStartConnectivity = (Button) rootView.findViewById(R.id.start_connectivity_button);
        btnStartConnectivity.setOnClickListener(this);
        user = new User(getActivity().getApplicationContext());
        accessTokenValue = user.getHsdpAccessToken();
        connectionState = (TextView) rootView.findViewById(R.id.connectionState);
        try {
            Handler mainHandler = new Handler(getActivity().getMainLooper());
            shnCentral = new SHNCentral.Builder(getActivity().getApplicationContext()).setHandler(mainHandler).create();
            shnDeviceDefinitionInfo = ShinePluginCopper.getSHNDeviceDefinitionInfo();//new SHNDeviceDefinitionInfoCopper();
            shnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);
        } catch (SHNBluetoothHardwareUnavailableException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private SHNCapabilityBattery setListenerAndReturnSHNCapabilityBattery() {
        SHNCapabilityBattery shnCapabilityBattery = getSHNCapabilityBattery();
        if (shnCapabilityBattery != null) {
            shnCapabilityBattery.setSetSHNCapabilityBatteryListener(shnCapabilityBatteryListener);
        }
        return shnCapabilityBattery;
    }

    private SHNCapabilityBattery getSHNCapabilityBattery() {
        SHNDevice connectedDevice = shineDevice;
        if (connectedDevice != null && connectedDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.BATTERY)) {
            return ((SHNCapabilityBattery) connectedDevice.getCapabilityForType(SHNCapabilityType.BATTERY));
        }
        return null;
    }

    SHNCapabilityBattery.SHNCapabilityBatteryListener shnCapabilityBatteryListener = new SHNCapabilityBattery.SHNCapabilityBatteryListener() {
        @Override
        public void onBatteryLevelChanged(int level) {
        }
    };

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.start_connectivity_button:
                startConnectivity();
                break;
            case R.id.get_momentumvalue_button:
                editTextValue=editText.getText().toString();
                processMoment(user, editTextValue);
                break;
            default:
        }
    }

    private void startConnectivity() {
        SHNDevice.SHNDeviceListener shineDeviceListener = new SHNDevice.SHNDeviceListener() {
            @Override
            public void onStateUpdated(SHNDevice shnDevice) {
                connectionState.setText(shnDevice.getState().toString());
                if (shnDevice.getState().toString() == "Connected") {
                    SHNCapabilityBattery shnCapabilityBattery = setListenerAndReturnSHNCapabilityBattery();
                    if (shnCapabilityBattery != null) {
                        shnCapabilityBattery.getBatteryLevel(new SHNIntegerResultListener() {
                            @Override
                            public void onActionCompleted(int value, SHNResult result) {
                                editText.setText(String.valueOf(value));
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {
                connectionState.setText(shnDevice.getState().toString() + shnResult);
            }
        };
        shineDevice = shnCentral.createSHNDeviceForAddressAndDefinition("06:05:04:03:02:AB", shnDeviceDefinitionInfo);
        shineDevice.registerSHNDeviceListener(shineDeviceListener);
        shineDevice.connect();
        connectionState.setText(shineDevice.getState().toString());
    }

    public void processMoment(final User user, String momentValue) {
        Log.i(TAG, "Moment value" + momentValue);
        showProgressDialog("Posting data in datacore, Please wait...");
        connectivityPresenter.postMoment(user, momentValue);
    }

    @Override
    public void updateUIOnPostMomentSuccess(final String momentId) {
        Log.i(TAG, "Moment Id" + momentId);
        showProgressDialog("Getting moment from datacore, Please wait...");
        connectivityPresenter.getMoment(user, momentId);
    }

    @Override
    public void updateUIOnPostMomentError(final VolleyError volleyError) {
        dismissProgressDialog();
        Log.d(TAG, "Error while setting moment value");
    }

    @Override
    public void updateUIOnGetMomentSuccess(final String momentValue) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                momentValueEditText.setText(momentValue);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void updateUIOnGetMomentError(final VolleyError volleyError) {
        dismissProgressDialog();
        Log.d(TAG, "Error while getting moment value");
    }

    public void showProgressDialog(String text) {
        dismissProgressDialog();
        dialog = ProgressDialog.show(getActivity(), "", text);
    }

    public void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}


