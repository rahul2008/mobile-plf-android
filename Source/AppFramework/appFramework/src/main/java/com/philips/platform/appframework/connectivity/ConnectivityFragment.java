/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.philips.cdp.registration.User;
import java.io.IOException;


public class ConnectivityFragment extends AppFrameworkBaseFragment {
    private TextView textView;
    private ImageView imageView;
    private EditText editText = null;

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

        User user = new User(getActivity().getApplicationContext());
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
}
