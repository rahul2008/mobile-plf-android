package com.philips.cdp.backend;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.ErrorType;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.productbuilder.RegistrationDataBuilder;
import com.philips.cdp.prxclient.HttpsTrustManager;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.ProductRequest;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProcessNetwork {
    private static final int MAX_RETRY_COUNT = 3;
    private Context context;
    private String TAG = getClass() + "";
    private RequestQueue requestQueue;
    private boolean isHttpsRequest = true;
    private int accessTokenRetryCount = 0;

    public ProcessNetwork(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void executeRequest(final RegistrationDataBuilder registrationDataBuilder, final ResponseListener listener) {

        PrxLogger.d(TAG, "Url : " + registrationDataBuilder.getRequestUrl());
        ProductRequest productRequest = new ProductRequest(registrationDataBuilder.getMethod(), registrationDataBuilder.getRequestUrl(), registrationDataBuilder.getParams(), registrationDataBuilder.getHeaders(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResponseData responseData = registrationDataBuilder.getResponseData(response);
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
                            handleError(networkResponse.statusCode, registrationDataBuilder, listener, error);
                    } catch (Exception e) {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                    }
                }
            }
        });
        if (isHttpsRequest)
            HttpsTrustManager.allowAllSSL();
        requestQueue.add(productRequest);
    }

    public void executeGetRequest(final RegistrationDataBuilder registrationDataBuilder, final ResponseListener listener) {

        PrxLogger.d(TAG, "Url : " + registrationDataBuilder.getRequestUrl());
        ProductRequest productRequest = new ProductRequest(Request.Method.GET, registrationDataBuilder.getRequestUrl(), registrationDataBuilder.getParams(), registrationDataBuilder.getHeaders(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResponseData responseData = registrationDataBuilder.getResponseData(response);
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
                            handleError(networkResponse.statusCode, registrationDataBuilder, listener, error);
                    } catch (Exception e) {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                    }
                }
            }
        });
        if (isHttpsRequest)
            HttpsTrustManager.allowAllSSL();
        requestQueue.add(productRequest);
    }

    private void handleError(final int statusCode, final PrxDataBuilder prxDataBuilder, final ResponseListener listener, final VolleyError error) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_EXPIRED.getId()) {
            onAccessTokenExpire((RegistrationBuilder) prxDataBuilder, listener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            listener.onResponseError(ErrorType.ACCESS_TOKEN_INVALID.getDescription(), statusCode);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getId()) {
            listener.onResponseError(ErrorType.INVALID_VALIDATION.getDescription(), statusCode);
        } else if (error instanceof NoConnectionError) {
            listener.onResponseError("No internet connection", statusCode);
        }
    }

    private void onAccessTokenExpire(final RegistrationBuilder prxDataBuilder, final ResponseListener listener) {
        UserWithProduct userWithProduct = new UserWithProduct(context);
        userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {
                RegistrationBuilder registrationBuilder = prxDataBuilder;
                registrationBuilder.setAccessToken(response);
                executeRequest(registrationBuilder, listener);
            }

            @Override
            public void onRegisterFailedWithFailure(final int error) {
                return;
            }
        });
    }

    public boolean isHttpsRequest() {
        return isHttpsRequest;
    }

    public void setHttpsRequest(boolean isHttpsRequest) {
        this.isHttpsRequest = isHttpsRequest;
    }
}
