/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.aildemo;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.aikm.AIKMInterface;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
import com.philips.platform.appinfra.aikm.model.OnGetServicesListener;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeyBagActivity extends AppCompatActivity {


    private EditText serviceIdEditText;
    private TextView responseTextView;
    private AISDResponse.AISDPreference aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDCountryPreference;
    private AppInfraInterface appInfra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keybag);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        serviceIdEditText = (EditText) findViewById(R.id.service_id_edt);
        responseTextView = (TextView) findViewById(R.id.response_view);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rbtn_language) {
                    aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDLanguagePreference;
                } else
                    aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDCountryPreference;
            }
        });
    }

    public void onClick(View view) {
        final AIKMInterface aiKmInterface = appInfra.getAiKmInterface();

        if (aiKmInterface != null) {

            String serviceIdsFromEditText = serviceIdEditText.getText().toString();
            if (!TextUtils.isEmpty(serviceIdsFromEditText)) {
                String[] serviceIds = serviceIdsFromEditText.split(",");

                try {
                    aiKmInterface.getValueForServiceIds(new ArrayList<>(Arrays.asList(serviceIds)), aikmServiceDiscoveryPreference, null, new OnGetServicesListener() {
                        @Override
                        public void onSuccess(List<AIKMService> aikmServices) {
                            updateView(aikmServices);
                        }

                        @Override
                        public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES error, String message) {
                            Log.e(getClass().getSimpleName(), message);
                            Toast.makeText(KeyBagActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (AIKMJsonFileNotFoundException | JSONException e) {
                    if (e instanceof AIKMJsonFileNotFoundException)
                        Log.e("error ", " Json file not found ");
                    else
                        Log.e("error ", "in Json Structure ");
                }
            } else
                Toast.makeText(KeyBagActivity.this, "Please enter service id", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(KeyBagActivity.this, "Aikm service disabled or not found in AppConfig.json", Toast.LENGTH_SHORT).show();
    }

    private void updateView(List<AIKMService> aikmServiceList) {
        StringBuilder stringBuilder = new StringBuilder();
        String serviceUrl = null;
        Map testDSKey = null;
        if (aikmServiceList != null && aikmServiceList.size() != 0) {
            for (int i = 0; i < aikmServiceList.size(); i++) {
                stringBuilder.append("ServiceId: ");
                AIKMService aikmService = aikmServiceList.get(i);
                stringBuilder.append(aikmService.getServiceId());
                stringBuilder.append(", ");
                if (TextUtils.isEmpty(aikmService.getmError())) {
                    stringBuilder.append("Url:");
                    stringBuilder.append("  ");
                    stringBuilder.append(aikmService.getConfigUrls());

                    if (aikmService.getServiceId().equals("appinfra.keybag")) {
                        serviceUrl = aikmService.getConfigUrls();
                    }

                } else {
                    stringBuilder.append("Error:");
                    stringBuilder.append("  ");
                    stringBuilder.append(aikmService.getmError());
                }
                if (!TextUtils.isEmpty(aikmService.getLocale())) {
                    stringBuilder.append(", Locale:");
                    stringBuilder.append(aikmService.getLocale());
                }
                stringBuilder.append(", ");
                Map keyBag = aikmService.getAIKMap();
                if (keyBag != null) {
                    for (Object object : keyBag.entrySet()) {
                        Map.Entry pair = (Map.Entry) object;
                        String key = (String) pair.getKey();
                        String value = (String) pair.getValue();

                        stringBuilder.append("KeyBag Data --- ");
                        stringBuilder.append(key);
                        stringBuilder.append(":");
                        stringBuilder.append(value);
                        stringBuilder.append("  ");


                        if (aikmService.getServiceId().equals("appinfra.keybag")) {
                            testDSKey = keyBag;
                        }
                    }
                }
                AIKMService.AIKMapError keyBagError = aikmService.getAIKMapError();
                if (null != keyBagError) {
                    stringBuilder.append("error while fetching key bag -- ");
                    stringBuilder.append(keyBagError.getDescription());
                }
                stringBuilder.append("\n");
                stringBuilder.append("\n");
            }
        }
        responseTextView.setText(stringBuilder.toString());
//        if (serviceUrl != null && testDSKey != null)
//            makeRestCall(serviceUrl, testDSKey);

    }

   /* private void makeRestCall(String serviceUrl, final Map<String, String> testDSKey) {

        Uri.Builder builder = Uri.parse(serviceUrl)
                .buildUpon();

        for (String key : testDSKey.keySet()) {
            builder.appendQueryParameter(key, testDSKey.get(key));
        }


        StringRequest mStringRequest = null;
        try {
            mStringRequest = new StringRequest(Request.Method.GET, builder.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG", "" + response);
                    Toast.makeText(KeyBagActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG error", "" + error);
                    Toast.makeText(KeyBagActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }, null, null, null)

            {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null && response.data != null) {
                        return super.parseNetworkResponse(response);
                    } else {
                        return Response.error(new VolleyError("Response is null"));
                    }
                }
            };
        } catch (Exception e) {
            Log.d("HttpForbiddenException", e.toString());
        }
        if (mStringRequest!=null && mStringRequest.getCacheEntry() != null) {
            String cachedResponse = new String(mStringRequest.getCacheEntry().data);
            Log.i("CACHED DATA: ", "" + cachedResponse);
        }
        // mStringRequest.setShouldCache(false); // set false to disable cache

            appInfra.getRestClient().getRequestQueue().add(mStringRequest);
    }*/
}
