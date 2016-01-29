package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.model.ModelQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

public class NetworkController {
    RequestQueue volleyQueue;
    Context context;

    NetworkController(Context context) {
        this.context = context;
        volleyQueue = Volley.newRequestQueue(context, new HurlStack());
    }

    public void sendRequest(int requestCode, final RequestListener requestListener) {
        ModelQuery model = getModel(requestCode);
        volleyQueue.add(createRequest(requestCode, model,requestListener));
    }

    private JsonObjectRequest createRequest(final int requestCode, final ModelQuery modelQuery , final RequestListener requestListener) {

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                requestListener.onError(msg);
            }
        };

        Response.Listener response = new Response.Listener<JsonObject>() {

            @Override
            public void onResponse(final JsonObject response) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                msg.obj = modelQuery.parseResponse(response);
                requestListener.onSuccess(msg);
            }
        };

        return new JsonObjectRequest(modelQuery.getMethod(requestCode), modelQuery.getUrl(requestCode),
                getJsonParams(modelQuery.requestBody()), response, error);
    }

    //Add model specific implementation
    private ModelQuery getModel(final int requestCode) {
        switch(requestCode){
            case RequestCode.GET_CART:
                return new CartModel();
            default:
                return null;
        }
    }

    /**
     * Forms the json object with the payload passed
     * @param mParams payload bundle
     * @return JsonObject
     */
    private JSONObject getJsonParams(Bundle mParams) {
        JSONObject params = null;

        try {
            if (mParams != null) {
                Set<String> keys = mParams.keySet();

                if (keys.size() > 0) {
                    params = new JSONObject();

                    for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        String value = mParams.getString(key);
                        params.put(key, value);
                    }

                    return params;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
