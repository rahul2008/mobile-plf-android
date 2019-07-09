package com.philips.cdp.di.ecs.test;

import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.HybrisStore;
import com.philips.cdp.di.ecs.volley.IAPJsonRequest;
import com.philips.cdp.di.ecs.volley.SynchronizedNetwork;
import com.philips.cdp.di.ecs.volley.SynchronizedNetworkListener;

import org.json.JSONObject;

public class FetchConfiguration {


    public void fetchConfiguration() {
        IAPJsonRequest request = new IAPJsonRequest(Request.Method.GET, new HybrisStore().getRawConfigUrl(), null,
                null, null);
        SynchronizedNetwork synchronizedNetwork = getSynchronizedNetwork();
        synchronizedNetwork.performRequest(request, new SynchronizedNetworkListener() {
            @Override
            public void onSyncRequestSuccess(final Response<JSONObject> jsonObjectResponse) {
                if(jsonObjectResponse == null || jsonObjectResponse.result == null){
                    postError(null);
                    return;
                }
                HybrisConfigResponse resp = new Gson().fromJson(jsonObjectResponse.result.toString(),
                        HybrisConfigResponse.class);

            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                postError(volleyError);
            }

            private void postError(VolleyError volleyError) {
                Message msg = Message.obtain();

            }
        });
    }

    SynchronizedNetwork getSynchronizedNetwork() {
        HurlStack hurlStack = new HurlStack(null);
        return new SynchronizedNetwork(hurlStack);
    }
}
