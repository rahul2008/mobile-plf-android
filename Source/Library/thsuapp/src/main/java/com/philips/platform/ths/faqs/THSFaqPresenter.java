/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSRestClient;

import java.net.URL;

import static com.philips.platform.ths.utility.THSConstants.THS_SDK_SERVICE_ID;


public class THSFaqPresenter implements THSBasePresenter{
    THSFaqFragment mThsFaqFragment;

    public THSFaqPresenter(THSFaqFragment thsFaqFragment) {
        mThsFaqFragment = thsFaqFragment;
    }

    protected void getFaq(){
        THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_SDK_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

            }

            @Override
            public void onSuccess(URL url) {
                new THSRestClient().execute("https://stg.philips.com/dam/b2c/apps/70000/en_US/thsfaq.json");
            }
        });
    }

    @Override
    public void onEvent(int componentID) {

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
