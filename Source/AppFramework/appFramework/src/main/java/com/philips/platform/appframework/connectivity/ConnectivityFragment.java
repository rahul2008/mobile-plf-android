/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shineplugincopperlib.SHNDeviceDefinitionInfoCopper;
import com.philips.pins.shineplugincopperlib.ShinePluginCopper;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.philips.cdp.registration.User;
import java.io.IOException;
import java.util.List;

import android.view.View;
import android.widget.TextView;

public class ConnectivityFragment extends AppFrameworkBaseFragment {
    private TextView textView;
    private ImageView imageView;
    private EditText editText = null;
    private TextView connectionState;
    private SHNCentral shnCentral;
    private SHNDevice shineDevice;
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;
    public ConnectivityFragment() {
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.app_connectivity_title);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.af_connectivity_fragment, container, false);
        editText = (EditText) rootView.findViewById(R.id.nucleous_value_editbox);
        String editTextValue = String.valueOf(editText.getText());
        Button btnGetMoment = (Button) rootView.findViewById(R.id.get_momentumvalue_button);
        Button btnStartConnectivity = (Button) rootView.findViewById(R.id.start_connectivity_button);
        User user = new User(getActivity().getApplicationContext());

        connectionState = (TextView) rootView.findViewById(R.id.connectionState);
         try {
            Handler mainHandler = new Handler(getActivity().getMainLooper());
            shnCentral = new SHNCentral.Builder(getActivity().getApplicationContext()).setHandler(mainHandler).create();
            shnDeviceDefinitionInfo = ShinePluginCopper.getSHNDeviceDefinitionInfo();//new SHNDeviceDefinitionInfoCopper();
            shnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);
        }
        catch (SHNBluetoothHardwareUnavailableException e) {
            e.printStackTrace();
        }

        btnStartConnectivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 SHNDevice.SHNDeviceListener shineDeviceListener = new SHNDevice.SHNDeviceListener() {
                    @Override
                    public void onStateUpdated(SHNDevice shnDevice) {
                        connectionState.setText(shnDevice.getState().toString());
                        if(shnDevice.getState().toString() == "Connected") {
                            SHNCapabilityBattery shnCapabilityBattery = setListenerAndReturnSHNCapabilityBattery();
                            if (shnCapabilityBattery != null) {
                                shnCapabilityBattery.getBatteryLevel(new SHNIntegerResultListener() {
                                    @Override
                                    public void onActionCompleted(int value, SHNResult result) {
                                        editText.setText(String.valueOf(value));                                     }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {
                        connectionState.setText(shnDevice.getState().toString() + shnResult );
                    }
                };
                shineDevice = shnCentral.createSHNDeviceForAddressAndDefinition("06:05:04:03:02:AB", shnDeviceDefinitionInfo);
                shineDevice.registerSHNDeviceListener(shineDeviceListener);
                shineDevice.connect();
                connectionState.setText(shineDevice.getState().toString());
          }}
        );

        final String accessTokenValue = user.getAccessToken();
        btnGetMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"details\" : [\r\n        {\r\n            \"type\" : \"BatteryLevel\",\r\n            \"value\" :  + editTextValue + \r\n        }\r\n    ],\r\n    \"measurements\" : [\r\n        {\r\n            \"details\" : [\r\n                {\r\n                    \"type\" : \"BatteryLevel\",\r\n                    \"value\" : \"65\"\r\n                }\r\n            ],\r\n            \"timestamp\" : \"2015-08-14T07:07:14.000Z\",\r\n            \"type\" : \"Duration\",\r\n            \"unit\" : \"sec\",\r\n            \"value\" : 65\r\n        }\r\n    ],\r\n    \"timestamp\" : \"2015-08-13T14:54:25+0200\",\r\n    \"type\" : \"BatteryLevel\"\r\n}\r\n");
                Request request = new Request.Builder()
                        .url("https://referenceplatform-ds-platforminfradev.cloud.pcftest.com//api/users/eb750bd4-4aa4-434b-8ff3-e5c71e97ed4b/moments")
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", "bearer "+accessTokenValue)
                        .addHeader("performerid", "eb750bd4-4aa4-434b-8ff3-e5c71e97ed4b")
                        .addHeader("api-version", "7")
                        .addHeader("appagent", "PlatformInfra Postman")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "3dc111b6-863d-f7fa-db2b-bfbba9909ed2")
                        .build();
                try {
//                    Response response = client.newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setDateToView();
        return rootView;
    }

    private void setDateToView() {
        Bundle bundle = getArguments();
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
}
