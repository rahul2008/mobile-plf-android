package com.philips.cdp.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.backend.PRXDataBuilderFactory;
import com.philips.cdp.backend.PRXRequestType;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProduct {

    private final String TAG = getClass() + "";
    private Context mContext;
    private String requestType;

    public void getRegisteredProducts(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        this.mContext = context;
        this.requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.FETCH_PRODUCTS, prodRegRequestInfo, new User(context).getAccessToken());
        RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(prxRequest, getLocalResponseListener(prodRegRequestInfo, listener));
    }

    @NonNull
    private RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    public void registerProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        this.mContext = context;
        this.requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.REGISTRATION, prodRegRequestInfo, new User(context).getAccessToken());
        RegistrationRequest registrationRequest = (RegistrationRequest) prxRequest;
        registrationRequest.setRegistrationChannel(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setmLocale(prodRegRequestInfo.getLocale());
        registrationRequest.setPurchaseDate(prodRegRequestInfo.getPurchaseDate());
        registrationRequest.setProductSerialNumber(prodRegRequestInfo.getSerialNumber());
        RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(registrationRequest, getLocalResponseListener(prodRegRequestInfo, listener));
    }

    private void handleError(final int statusCode, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_EXPIRED.getId()) {
            onAccessTokenExpire(prodRegRequestInfo, listener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            onAccessTokenExpire(prodRegRequestInfo, listener);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getId()) {
            listener.onResponseError(ErrorType.INVALID_VALIDATION.getDescription(), statusCode);
        } else if (statusCode == ErrorType.INVALID_SERIAL_NUMBER.getId()) {
            listener.onResponseError(ErrorType.INVALID_SERIAL_NUMBER.getDescription(), statusCode);
        } else if (statusCode == ErrorType.NO_INTERNET_CONNECTION.getId()) {
            listener.onResponseError(ErrorType.NO_INTERNET_CONNECTION.getDescription(), statusCode);
        } else if (statusCode == ErrorType.REQUEST_TIME_OUT.getId()) {
            listener.onResponseError(ErrorType.REQUEST_TIME_OUT.getDescription(), statusCode);
        } else {
            listener.onResponseError(ErrorType.UNKNOWN.getDescription(), statusCode);
        }
    }

    private void onAccessTokenExpire(final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        final User user = new User(mContext);
        user.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                retryRequests(mContext, prodRegRequestInfo, listener);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int error) {
                Log.d(TAG, "error in refreshing session");
                listener.onResponseError("error in refreshing session", -1);
            }
        }, mContext);
    }

    private void retryRequests(final Context mContext, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                registerProduct(mContext, prodRegRequestInfo, listener);
                break;
            case ProdRegConstants.FETCH_REGISTERED_PRODUCTS:
                getRegisteredProducts(mContext, prodRegRequestInfo, listener);
                break;
            default:
                break;
        }
    }

    @NonNull
    private ResponseListener getLocalResponseListener(final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                listener.onResponseSuccess(responseData);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    handleError(responseCode, prodRegRequestInfo, listener);
                } catch (Exception e) {
                    PrxLogger.e(TAG, mContext.getString(R.string.volley_error) + e.toString());
                }
            }
        };
    }
}
