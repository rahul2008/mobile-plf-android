/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSRestClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.philips.platform.ths.utility.THSConstants.THS_FAQ_SERVICE_ID;


public class THSFaqPresenter implements THSBasePresenter{
    THSFaqFragment mThsFaqFragment;
    THSRestClient mTHSRestClient;

    public THSFaqPresenter(THSFaqFragment thsFaqFragment) {
        mThsFaqFragment = thsFaqFragment;

    }

    protected void getFaq(){
        THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_FAQ_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                mThsFaqFragment.showError("Service discovery failed - >" + s);
            }

            @Override
            public void onSuccess(URL url) {
                mTHSRestClient = new THSRestClient(THSFaqPresenter.this);
                //TODO: Spoorti - Remove the hardcoding after the service discovery starts returning the right value
                //mTHSRestClient.execute(url.toString());
                mTHSRestClient.execute("https://www.philips.com/dam/b2c/apps/70000/en_US/thsfaq.json");
            }
        });
    }

    @Override
    public void onEvent(int componentID) {

    }

    public void parseJson(String jsonString) {
        HashMap map = new HashMap();
        if(jsonString!=null) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<ArrayList<THSFaqPojo>>() {
            }.getType();
            ArrayList<THSFaqPojo> fags = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
            for (THSFaqPojo thsfaq: fags) {
                map.put(thsfaq.getSection(),thsfaq.getFaq());
            }
        }
        mThsFaqFragment.updateFaqs(map);
    }

    /*public void api(){
        RestInterface mRestInterface;;
        mRestInterface = THSManager.getInstance().getAppInfra().getRestClient();
        JsonObjectRequest jsonRequest = null;
        try {
            jsonRequest = new JsonObjectRequest(Request.Method.GET,
                    "Telehealth.FAQURL", ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
                    null, null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("LOG", "" + response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("LOG", "" + error);
                    String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                }
            });
        } catch (Exception e) {
            Log.e("LOG REST SD", e.toString());
        }
        if (null != jsonRequest) {
            mRestInterface.getRequestQueue().add(jsonRequest);
        }
    }*/




}
