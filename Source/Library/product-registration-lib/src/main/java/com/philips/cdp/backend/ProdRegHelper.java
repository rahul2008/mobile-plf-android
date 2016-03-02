package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.model.ProductData;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.productbuilder.ProductMetaDataBuilder;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.productbuilder.RegistrationDataBuilder;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

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
        processMetadata(context, prxDataBuilder, listener);
    }

    protected void processMetadata(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        RegistrationBuilder registrationBuilder = (RegistrationBuilder) prxDataBuilder;
        ProductMetaDataBuilder productMetaDataBuilder = getProductMetaDataBuilder(registrationBuilder);
        RequestManager mRequestManager = getRequestManager(context);
        executeMetadataRequest(context, prxDataBuilder, listener, productMetaDataBuilder, mRequestManager);
    }

    private void executeMetadataRequest(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener, final ProductMetaDataBuilder productMetaDataBuilder, final RequestManager mRequestManager) {
        mRequestManager.executeRequest(productMetaDataBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                ProductData productData = productMetaData.getData();
                if (validateSerialNumberFromMetadata(productData, prxDataBuilder, listener)
                        && validatePurchaseDateFromMetadata(productData, prxDataBuilder, listener))
                    makeRegistrationRequest(context, prxDataBuilder, listener);
            }

            @Override
            public void onResponseError(String error, int code) {
                Toast.makeText(context, R.string.metadata_error, Toast.LENGTH_SHORT).show();
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
    private ProductMetaDataBuilder getProductMetaDataBuilder(final RegistrationBuilder registrationBuilder) {
        ProductMetaDataBuilder productMetaDataBuilder = new ProductMetaDataBuilder(registrationBuilder.getCtn(), registrationBuilder.getAccessToken());
        productMetaDataBuilder.setSector(registrationBuilder.getSector());
        productMetaDataBuilder.setmLocale(registrationBuilder.getLocale());
        productMetaDataBuilder.setCatalog(registrationBuilder.getCatalog());
        return productMetaDataBuilder;
    }

    private boolean validateSerialNumberFromMetadata(final ProductData data, final PrxDataBuilder prxDataBuilder, ResponseListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            RegistrationDataBuilder registrationDataBuilder = (RegistrationDataBuilder) prxDataBuilder;
            registrationDataBuilder.setRequiresSerialNumber(true);
            if (!registrationDataBuilder.getProductSerialNumber().matches(data.getSerialNumberFormat())) {
                listener.onResponseError(mContext.getString(R.string.serial_number_invalid_format), -1);
                return false;
            }
        }
        return true;
    }

    private boolean validatePurchaseDateFromMetadata(final ProductData data, final PrxDataBuilder prxDataBuilder, ResponseListener listener) {
        RegistrationDataBuilder registrationDataBuilder = (RegistrationDataBuilder) prxDataBuilder;
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

    private void makeRegistrationRequest(final Context context, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        RequestManager requestManager = getRequestManager(context);
        requestManager.executeRequest(RequestType.POST, prxDataBuilder, getLocalResponseListener(prxDataBuilder, listener));
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
                    PrxLogger.e(TAG, mContext.getString(R.string.volley_error) + e.toString());
                }
            }
        };
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
        } else if (statusCode == ErrorType.INVALID_SERIAL_NUMBER.getId()) {
            listener.onResponseError(ErrorType.INVALID_SERIAL_NUMBER.getDescription(), statusCode);
        } else if (statusCode == ErrorType.NO_INTERNET_CONNECTION.getId()) {
            listener.onResponseError(ErrorType.NO_INTERNET_CONNECTION.getDescription(), statusCode);
        } else {
            listener.onResponseError(ErrorType.UNKNOWN.getDescription(), statusCode);
        }
    }

    private void onAccessTokenExpire(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        UserWithProduct userWithProduct = new UserWithProduct(mContext);
        userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {
                RegistrationDataBuilder registrationDataBuilder = (RegistrationDataBuilder) prxDataBuilder;
                registrationDataBuilder.setAccessToken(response);
                validateRequests(mContext, prxDataBuilder, listener);
            }

            @Override
            public void onRegisterFailedWithFailure(final int error) {
                return;
            }
        });
    }

    public void getRegisteredProduct(final Context context, PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        RequestManager requestManager = getRequestManager(context);
        requestManager.executeRequest(RequestType.GET, prxDataBuilder, getLocalResponseListener(prxDataBuilder, listener));
    }

    private void validateRequests(final Context mContext, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
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
