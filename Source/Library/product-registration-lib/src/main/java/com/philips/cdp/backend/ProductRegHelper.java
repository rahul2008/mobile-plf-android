package com.philips.cdp.backend;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.PrxRequest;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.network.NetworkWrapper;
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
public class ProductRegHelper {

    private static ProductRegHelper productRegHelper;
    private Context mContext = null;
    private String TAG = getClass() + "";

    private ProductRegHelper() {
    }

    public static ProductRegHelper getInstance() {
        if (productRegHelper == null) {
            productRegHelper = new ProductRegHelper();
            return productRegHelper;
        }
        return productRegHelper;
    }

    public void cancelRequest(String requestTag) {
    }

    public void registerProduct(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        mContext = context;
        RegistrationBuilder registrationBuilder = (RegistrationBuilder) prxDataBuilder;
        Map<String, String> params = getProductRegParams(registrationBuilder);

        Map<String, String> headers = new HashMap<>();
        headers.put("x-accessToken", registrationBuilder.getAccessToken());

        PrxRequest prxRequest = new PrxRequest(RequestType.POST.getMethod(), prxDataBuilder.getRequestUrl(), params, headers, new Response.Listener<JSONObject>() {
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
                        else if (error instanceof NoConnectionError) {
                            listener.onResponseError("No internet connection", 0);
                        } else
                            listener.onResponseError(ErrorType.UNKNOWN.getDescription(), 0);
                    } catch (Exception e) {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                    }
                }
            }
        });
        new NetworkWrapper(mContext).executeCustomRequest(prxRequest);
    }

    private Map<String, String> getProductRegParams(final RegistrationBuilder registrationBuilder) {
        Map<String, String> params = new HashMap<>();
        params.put("purchaseDate", registrationBuilder.getPurchaseDate());
        params.put("productSerialNumber", registrationBuilder.getProductSerialNumber());
        params.put("registrationChannel", registrationBuilder.getRegistrationChannel());
        return params;
    }

    private void handleError(final int statusCode, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_EXPIRED.getId()) {
            onAccessTokenExpire((RegistrationBuilder) prxDataBuilder, listener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            onAccessTokenExpire((RegistrationBuilder) prxDataBuilder, listener);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getId()) {
            listener.onResponseError(ErrorType.INVALID_VALIDATION.getDescription(), statusCode);
        }
    }

    private void onAccessTokenExpire(final RegistrationBuilder prxDataBuilder, final ResponseListener listener) {
        UserWithProduct userWithProduct = new UserWithProduct(mContext);
        userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {
                RegistrationBuilder registrationBuilder = prxDataBuilder;
                registrationBuilder.setAccessToken(response);
                registerProduct(mContext, prxDataBuilder, listener);
            }

            @Override
            public void onRegisterFailedWithFailure(final int error) {
                return;
            }
        });
    }
}
