package com.philips.cdp.backend;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.ErrorType;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.HttpsTrustManager;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProcessNetwork {
    private static final int MAX_RETRY_COUNT = 3;
    private Context context;
    private String TAG = getClass() + "";
    private RequestQueue requestQueue;
    private boolean isHttpsRequest = false;

    public ProcessNetwork(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    protected void productRegistrationRequest(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {

        PrxLogger.d(TAG, "Url : " + prxDataBuilder.getRequestUrl());
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, prxDataBuilder.getRequestUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResponseData responseData = prxDataBuilder.getResponseData(response);
                listener.onResponseSuccess(responseData);

                PrxLogger.d(TAG, "Response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    final NetworkResponse networkResponse = error.networkResponse;
                    try {
                        if (networkResponse != null)
                            handleError(networkResponse.statusCode, prxDataBuilder, listener);
                    } catch (Exception e) {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                RegistrationBuilder registrationBuilder = (RegistrationBuilder) prxDataBuilder;
                params.put("x-accessToken", registrationBuilder.getAccessToken());
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        if (isHttpsRequest)
            HttpsTrustManager.allowAllSSL();
        requestQueue.add(mJsonObjectRequest);
    }

    private void handleError(final int statusCode, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError("invalid product", statusCode);
        } else if (statusCode == ErrorType.INTERNAL_SERVER_ERROR.getId()) {
            listener.onResponseError("internal server error", statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            UserWithProduct userWithProduct = new UserWithProduct(context);
            userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
                @Override
                public void onRegisterSuccess(final String response) {
                    RegistrationBuilder registrationBuilder = (RegistrationBuilder) prxDataBuilder;
                    registrationBuilder.setAccessToken(response);
                    productRegistrationRequest(registrationBuilder, listener);
                }

                @Override
                public void onRegisterFailedWithFailure(final int error) {

                }
            });
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getId()) {
            listener.onResponseError("invalid validation", statusCode);
        }
    }

    public boolean isHttpsRequest() {
        return isHttpsRequest;
    }

    public void setHttpsRequest(boolean isHttpsRequest) {
        this.isHttpsRequest = isHttpsRequest;
    }
}
