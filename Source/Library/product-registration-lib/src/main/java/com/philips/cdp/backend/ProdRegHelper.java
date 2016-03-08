package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.handler.Product;
import com.philips.cdp.handler.UserProduct;
import com.philips.cdp.model.ProductData;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
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

    private boolean processSerialNumber(final ProductData data, final ResponseListener listener, ProdRegRequestInfo prodRegRequestInfo) {
        if (prodRegRequestInfo.getSerialNumber() == null || prodRegRequestInfo.getSerialNumber().length() < 1) {
            listener.onResponseError(mContext.getString(R.string.serial_number_not_entered), -1);
            return true;
        } else if (!prodRegRequestInfo.getSerialNumber().matches(data.getSerialNumberFormat())) {
            listener.onResponseError(mContext.getString(R.string.serial_number_error), -1);
            return true;
        }
        return false;
    }

    private boolean validatePurchaseDateFromMetadata(final ProductData data, final ProdRegRequestInfo prodRegRequestInfo, ResponseListener listener) {
        final String purchaseDate = prodRegRequestInfo.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                listener.onResponseError(mContext.getString(R.string.date_format_error), -1);
                return false;
            }
        }
        return true;
    }

    private void makeRegistrationRequest(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        prodRegRequestInfo.setAccessToken(new User(context).getAccessToken());
        UserProduct userProduct = new UserProduct();
        userProduct.registerProduct(context, prodRegRequestInfo, getLocalResponseListener(prodRegRequestInfo, listener));
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
            }
        }, mContext);
    }

    private void retryRequests(final Context mContext, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                makeRegistrationRequest(mContext, prodRegRequestInfo, listener);
                break;
            case ProdRegConstants.FETCH_REGISTERED_PRODUCTS:
                getRegisteredProduct(mContext, prodRegRequestInfo, listener);
                break;
            default:
                break;
        }
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    /**
     * <b> API to get registered products</b>
     *
     * @param context    - Context of an activity
     * @param prodRegRequestInfo - prodRegRequestInfo object
     * @param listener   - Callback listener
     */
    public void getRegisteredProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        prodRegRequestInfo.setAccessToken(new User(context).getAccessToken());
        prodRegRequestInfo.setLocale(this.locale);
        UserProduct userProduct = new UserProduct();
        userProduct.getRegisteredProducts(context, prodRegRequestInfo, getLocalResponseListener(prodRegRequestInfo, listener));
    }

    /**
     * <b> API to register product</b>
     *
     * @param context            - Context of an activity
     * @param prodRegRequestInfo - PRX Request object
     * @param listener           - Callback listener
     */
    public void registerProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        processMetadata(context, prodRegRequestInfo, listener);
    }

    protected void processMetadata(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        Product product = new Product();
        prodRegRequestInfo.setLocale(this.locale);
        product.getProductMetadata(context, prodRegRequestInfo, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                ProductData productData = productMetaData.getData();
                if (validateSerialNumberFromMetadata(productData, prodRegRequestInfo, listener)
                        && validatePurchaseDateFromMetadata(productData, prodRegRequestInfo, listener))
                    makeRegistrationRequest(context, prodRegRequestInfo, listener);
            }

            @Override
            public void onResponseError(String error, int code) {
                handleError(code, prodRegRequestInfo, listener);
            }
        });
    }

    private boolean validateSerialNumberFromMetadata(final ProductData data, final ProdRegRequestInfo prodRegRequestInfo, ResponseListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, listener, prodRegRequestInfo)) return false;
        }
        return true;
    }


}
