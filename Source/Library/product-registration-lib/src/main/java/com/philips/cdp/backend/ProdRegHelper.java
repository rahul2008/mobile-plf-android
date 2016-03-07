package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.model.ProductData;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.productrequest.InjectAccessToken;
import com.philips.cdp.productrequest.ProductMetaRequest;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegHelper {

    private Context mContext = null;
    private String TAG = getClass() + "";
    private String requestType;
    private String locale;

    /**
     * <b> Helper class used to process product registration backend calls</b>
     *
     * @param context - Context of an Activity
     */
    public ProdRegHelper(Context context) {
        this.mContext = context;
    }

    public void cancelRequest(String requestTag) {
    }

    protected void processMetadata(final Context context, final PrxRequest prxRequest, final ResponseListener listener) {
        RegistrationRequest registrationRequest = (RegistrationRequest) prxRequest;
        ProductMetaRequest productMetaDataBuilder = getProductMetaDataBuilder(registrationRequest);
        RequestManager mRequestManager = getRequestManager(context);
        executeMetadataRequest(context, registrationRequest, listener, productMetaDataBuilder, mRequestManager);
    }

    private void executeMetadataRequest(final Context context, final PrxRequest prxRequest, final ResponseListener listener, final ProductMetaRequest productMetaDataBuilder, final RequestManager mRequestManager) {
        mRequestManager.executeRequest(productMetaDataBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                ProductData productData = productMetaData.getData();
                if (validateSerialNumberFromMetadata(productData, prxRequest, listener)
                        && validatePurchaseDateFromMetadata(productData, prxRequest, listener))
                    makeRegistrationRequest(context, prxRequest, listener);
            }

            @Override
            public void onResponseError(String error, int code) {
                handleError(code, prxRequest, listener);
            }
        });
    }

    @NonNull
    private RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    @NonNull
    private ProductMetaRequest getProductMetaDataBuilder(final RegistrationRequest registrationRequest) {
        ProductMetaRequest productMetaDataBuilder = new ProductMetaRequest(registrationRequest.getCtn(), registrationRequest.getAccessToken());
        productMetaDataBuilder.setSector(registrationRequest.getSector());
        productMetaDataBuilder.setmLocale(registrationRequest.getLocale());
        productMetaDataBuilder.setCatalog(registrationRequest.getCatalog());
        return productMetaDataBuilder;
    }

    private boolean validateSerialNumberFromMetadata(final ProductData data, final PrxRequest prxRequest, ResponseListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            RegistrationRequest registrationDataBuilder = (RegistrationRequest) prxRequest;
            registrationDataBuilder.setRequiresSerialNumber(true);
            if (registrationDataBuilder.getProductSerialNumber() == null || registrationDataBuilder.getProductSerialNumber().length() < 0) {
                listener.onResponseError(mContext.getString(R.string.serial_number_not_entered), -1);
                return false;
            }
            if (!registrationDataBuilder.getProductSerialNumber().matches(data.getSerialNumberFormat())) {
                listener.onResponseError(mContext.getString(R.string.serial_number_error), -1);
                return false;
            }
        }
        return true;
    }

    private boolean processSerialNumber(final ProductData data, final ResponseListener listener, final ProdRegRequestInfo prodRegRequestInfo) {
        if (prodRegRequestInfo.getSerialNumber() == null || prodRegRequestInfo.getSerialNumber().length() < 1) {
            listener.onResponseError(mContext.getString(R.string.serial_number_not_entered), -1);
            return true;
        } else if (!prodRegRequestInfo.getSerialNumber().matches(data.getSerialNumberFormat())) {
            listener.onResponseError(mContext.getString(R.string.serial_number_error), -1);
            return true;
        }
        return false;
    }

    private boolean validatePurchaseDateFromMetadata(final ProductData data, final PrxRequest prxRequest, ResponseListener listener) {
        RegistrationRequest registrationDataBuilder = (RegistrationRequest) prxRequest;
        final String purchaseDate = registrationDataBuilder.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                registrationDataBuilder.setRequiresPurchaseDate(true);
                return true;
            } else {
                listener.onResponseError(mContext.getString(R.string.date_format_error), -1);
                return false;
            }
        }
        return true;
    }

    private void makeRegistrationRequest(final Context context, final PrxRequest prxRequest, final ResponseListener listener) {
        RequestManager requestManager = getRequestManager(context);
        requestManager.executeRequest(prxRequest, getLocalResponseListener(prxRequest, listener));
    }

    @NonNull
    private ResponseListener getLocalResponseListener(final PrxRequest prxRequest, final ResponseListener listener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                listener.onResponseSuccess(responseData);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    handleError(responseCode, prxRequest, listener);
                } catch (Exception e) {
                    PrxLogger.e(TAG, mContext.getString(R.string.volley_error) + e.toString());
                }
            }
        };
    }

    private void handleError(final int statusCode, final PrxRequest prxRequest, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_EXPIRED.getId()) {
            onAccessTokenExpire(prxRequest, listener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getId()) {
            onAccessTokenExpire(prxRequest, listener);
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

    private void onAccessTokenExpire(final PrxRequest prxRequest, final ResponseListener listener) {
        final User user = new User(mContext);
        user.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                String response = user.getAccessToken();
                InjectAccessToken injectAccessToken = (InjectAccessToken) prxRequest;
                injectAccessToken.setAccessToken(response);
                validateRequests(mContext, prxRequest, listener);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int error) {
                Log.d(TAG, "error in refreshing session");
            }
        }, mContext);
    }

    private void validateRequests(final Context mContext, final PrxRequest prxRequest, final ResponseListener listener) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                makeRegistrationRequest(mContext, prxRequest, listener);
                break;
            case ProdRegConstants.FETCH_REGISTERED_PRODUCTS:
                getRegisteredProduct(mContext, prxRequest, listener);
                break;
            default:
                break;
        }
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    /**
     * <b> API to register product</b>
     *
     * @param context    - Context of an activity
     * @param prxRequest - PRX Request object
     * @param listener   - Callback listener
     */
    public void registerProduct(final Context context, final PrxRequest prxRequest, final ResponseListener listener) {
        requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        processMetadata(context, prxRequest, listener);
    }

    /**
     * <b> API to get registered products</b>
     *
     * @param context    - Context of an activity
     * @param prxRequest - PRX Request object
     * @param listener   - Callback listener
     */
    public void getRegisteredProduct(final Context context, PrxRequest prxRequest, final ResponseListener listener) {
        requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        RequestManager requestManager = getRequestManager(context);
        requestManager.executeRequest(prxRequest, getLocalResponseListener(prxRequest, listener));
    }


}
