package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.productbuilder.ProductMetaDataBuilder;
import com.philips.cdp.productbuilder.RegisteredBuilder;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.productbuilder.RegistrationDataBuilder;
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
public class ProdRegHelper {

    private Context mContext = null;
    private String TAG = getClass() + "";
    private String requestType;

    public ProdRegHelper(Context context) {
        this.mContext = context;
    }

    public void cancelRequest(String requestTag) {
    }

    public void registerProduct(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        mContext = context;
        final RegistrationBuilder registrationBuilder = (RegistrationBuilder) prxDataBuilder;
        callMetadata(context, prxDataBuilder, listener, registrationBuilder);
    }

    private void callMetadata(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener, final RegistrationBuilder registrationBuilder) {
        ProductMetaDataBuilder productMetaDataBuilder = new ProductMetaDataBuilder(registrationBuilder.getCtn(), registrationBuilder.getAccessToken());
        productMetaDataBuilder.setSector(registrationBuilder.getSector());
        productMetaDataBuilder.setmLocale(registrationBuilder.getLocale());
        productMetaDataBuilder.setCatalog(registrationBuilder.getCatalog());

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        mRequestManager.executeRequest(productMetaDataBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                makeRegistrationRequest(context, prxDataBuilder, listener);
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(context, "error in getting metadata", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeRegistrationRequest(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        Map<String, String> params = getProductRegParams((RegistrationBuilder) prxDataBuilder);
        Map<String, String> headers = getHeaders((RegistrationBuilder) prxDataBuilder);
        final ResponseListener listenerLocal = getLocalResponseListener(prxDataBuilder, listener);
        PrxRequest prxRequest = new PrxRequest(RequestType.POST, prxDataBuilder.getRequestUrl(), params, headers, listenerLocal, prxDataBuilder);
        RequestManager requestManager = new RequestManager();
        requestManager.init(context);
        requestManager.executeRequest(RequestType.POST, prxDataBuilder, prxRequest);
    }

    private Map<String, String> getHeaders(final RegistrationDataBuilder registrationDataBuilder) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_TAG, registrationDataBuilder.getAccessToken());
        return headers;
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
        params.put(ProdRegConstants.PRODUCT_SERIAL_NUMBER, registrationBuilder.getProductSerialNumber());
        params.put(ProdRegConstants.REGISTRATION_CHANNEL, registrationBuilder.getRegistrationChannel());
        return params;
    }

    private void validatePurchaseDate(final Map<String, String> params, final String purchaseDate) {
        if (purchaseDate != null && purchaseDate.length() > 0)
            params.put(ProdRegConstants.PURCHASE_DATE, purchaseDate);
    }

    private void handleError(final int statusCode, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_EXPIRED.getId()) {
            onAccessTokenExpire(prxDataBuilder, listener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            onAccessTokenExpire(prxDataBuilder, listener);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getId()) {
            listener.onResponseError(ErrorType.INVALID_VALIDATION.getDescription(), statusCode);
        } else if (statusCode == ErrorType.NO_INTERNET_CONNECTION.getId()) {
            listener.onResponseError(ErrorType.NO_INTERNET_CONNECTION.getDescription(), statusCode);
        }
    }

    private void onAccessTokenExpire(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        UserWithProduct userWithProduct = new UserWithProduct(mContext);
        userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {
                RegistrationDataBuilder registrationDataBuilder = (RegistrationDataBuilder) prxDataBuilder;
                registrationDataBuilder.setAccessToken(response);
                validateRequests(mContext, prxDataBuilder, listener, requestType);
            }
            @Override
            public void onRegisterFailedWithFailure(final int error) {
                return;
            }
        });
    }

    public void getRegisteredProduct(final Context context, PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        Map<String, String> headers = getHeaders((RegisteredBuilder) prxDataBuilder);
        final ResponseListener listenerLocal = getLocalResponseListener(prxDataBuilder, listener);
        PrxRequest prxRequest = new PrxRequest(RequestType.POST, prxDataBuilder.getRequestUrl(), null, headers, listenerLocal, prxDataBuilder);
        RequestManager requestManager = new RequestManager();
        requestManager.init(context);
        requestManager.executeRequest(RequestType.GET, prxDataBuilder, prxRequest);
    }

    private void validateRequests(final Context mContext, final PrxDataBuilder prxDataBuilder, final ResponseListener listener, String requestType) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                makeRegistrationRequest(mContext, prxDataBuilder, listener);
                break;
            case ProdRegConstants.FETCH_REGISTERED_PRODUCTS:
                getRegisteredProduct(mContext, prxDataBuilder, listener);
                break;
            default:
                break;
        }
    }
}
