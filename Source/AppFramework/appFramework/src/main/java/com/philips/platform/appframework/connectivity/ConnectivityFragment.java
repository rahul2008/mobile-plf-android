/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectivityFragment extends AppFrameworkBaseFragment {
    public static final String TAG = ConnectivityFragment.class.getSimpleName();
    private TextView textView;
    private ImageView imageView;
    private EditText editText = null;
    private EditText momentValueEditText = null;
    private String editTextValue, momentId, nucleousValue;
    private static String accessTokenValue;

    private TextView connectionState;
    private SHNCentral shnCentral;
    private SHNDevice shineDevice;
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;
    private User user;

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
        momentValueEditText = (EditText) rootView.findViewById(R.id.moment_value_editbox);
        Button btnGetMoment = (Button) rootView.findViewById(R.id.get_momentumvalue_button);
        Button btnStartConnectivity = (Button) rootView.findViewById(R.id.start_connectivity_button);
        user = new User(getActivity().getApplicationContext());
//        System.out.println("  ******* Environment : " + RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
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

        btnStartConnectivity.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
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
                                                }
        );

        btnGetMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextValue = String.valueOf(editText.getText());
                Log.i("created json", "hello " + editTextValue);
                new postMoment().execute();
            }
        });

        setDateToView();
        return rootView;
    }

    private void setDateToView() {
        Bundle bundle = getArguments();
    }
//

    private class getMoment extends AsyncTask<String, Void, String> {
        Response response = null;
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Getting moment from datacore, Please wait...");

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com//api/users/"+user.getHsdpUUID()+"/moments/" + momentId)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "bearer " + accessTokenValue)
                    .addHeader("performerid", user.getHsdpUUID())
                    .addHeader("api-version", "7")
                    .addHeader("appagent", "PlatformInfra Postman")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "0ab954ca-1c3a-b822-a9b6-9e3a6ded33cd")
                    .build();

            try {
                response = client.newCall(request).execute();
                String responseString = response.body().string();

                Log.i("response", "response text" + responseString);

                nucleousValue = getValue(responseString);

                Log.i("value of moment", "success" + nucleousValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response != null ? response.toString() : null;
        }

        private String getValue(String response) {
            String value = null;

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray1 = jsonObject.getJSONArray("details");

                JSONObject detailsValue = jsonArray1.getJSONObject(0);

                value = detailsValue.getString("value");
            } catch (JSONException ex) {

            }
            return value;
        }

        protected void onPreExecute() {
            //dialog.dismiss();
            Log.i("thread", "Started...");
            dialog.show();
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            momentValueEditText.setText(nucleousValue);
            Log.i("Debug test", "response" + result);
        }
    }

    private class postMoment extends AsyncTask<String, Void, String> {
        Response response = null;
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Posting data in datacore, Please wait...");

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"details\" : [\r\n        {\r\n            \"type\" : \"NucleousLevel\",\r\n            \"value\" : " + editTextValue + "\r\n        }\r\n    ],\r\n    \"measurements\" : [\r\n        {\r\n            \"details\" : [\r\n                {\r\n                    \"type\" : \"NucleousLevel\",\r\n                    \"value\" : 60\r\n                }\r\n            ],\r\n            \"timestamp\" : \"2015-08-14T07:07:14.000Z\",\r\n            \"type\" : \"Duration\",\r\n            \"unit\" : \"percentage\",\r\n            \"value\" : 60\r\n        }\r\n    ],\r\n    \"timestamp\" : \"2015-08-13T14:54:25+0200\",\r\n    \"type\" : \"NucleousLevel\"\r\n}\r\n");
            Request request = new Request.Builder()
                    .url("https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com//api/users/"+user.getHsdpUUID()+"/moments")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "bearer " + accessTokenValue)
                    .addHeader("performerid", user.getHsdpUUID())
                    .addHeader("api-version", "7")
                    .addHeader("appagent", "PlatformInfra Postman")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "ba0eff76-322f-9fa3-effe-97e8b0e09e93")
                    .build();

            Log.i("Test headers", request.headers().toString());
            try {
                response = client.newCall(request).execute();
                String value = response.body().string();
                Log.i("Details value", value);
                JSONObject jsonObject = new JSONObject(value);
                momentId = jsonObject.getString("momentId");
                Log.w("Json", momentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response != null ? response.toString() : null;
        }

        protected void onPreExecute() {
            //dialog.dismiss();
            Log.i("thread", "Started...");
            dialog.show();
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            new getMoment().execute();
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
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


