package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.PrxRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

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

        final ResponseListener listenerLocal = getLocalResponseListener(prxDataBuilder, listener);
        PrxRequest prxRequest = new PrxRequest(RequestType.POST, prxDataBuilder.getRequestUrl(), params, headers, listenerLocal, prxDataBuilder);
        RequestManager requestManager = new RequestManager();
        requestManager.init(context);
        requestManager.executeCustomRequest(prxRequest);
    }

    @NonNull
    private ResponseListener getLocalResponseListener(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                listener.onResponseSuccess(responseData);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    handleError(responseCode, prxDataBuilder, listener);
                } catch (Exception e) {
                    PrxLogger.e(TAG, "Volley Error : " + e);
                }
            }
        };
    }

    private Map<String, String> getProductRegParams(final RegistrationBuilder registrationBuilder) {
        Map<String, String> params = new HashMap<>();

        final String purchaseDate = registrationBuilder.getPurchaseDate();
        validatePurchaseDate(params, purchaseDate);
        params.put("productSerialNumber", registrationBuilder.getProductSerialNumber());
        params.put("registrationChannel", registrationBuilder.getRegistrationChannel());
        return params;
    }

    private void validatePurchaseDate(final Map<String, String> params, final String purchaseDate) {
        if (purchaseDate != null && purchaseDate.length() > 0)
            params.put("purchaseDate", purchaseDate);
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
        } else if (statusCode == ErrorType.NO_INTERNET_CONNECTION.getId()) {
            listener.onResponseError(ErrorType.NO_INTERNET_CONNECTION.getDescription(), statusCode);
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
