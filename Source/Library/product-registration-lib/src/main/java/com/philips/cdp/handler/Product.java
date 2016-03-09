package com.philips.cdp.handler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.backend.PRXDataBuilderFactory;
import com.philips.cdp.backend.PRXRequestType;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.model.ProductData;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.prxclient.ErrorType;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Product {

    private Context mContext;

    public void getProductSummary() {

    }

    public void getProductMetadata(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener helperListener, final ResponseListener listener) {
        mContext = context;
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.METADATA, prodRegRequestInfo, new User(context).getAccessToken());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener localListener = new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                ProductData productData = productMetaData.getData();
                if (validateSerialNumberFromMetadata(productData, prodRegRequestInfo, listener)
                        && validatePurchaseDateFromMetadata(productData, prodRegRequestInfo, listener))
                    helperListener.onResponseSuccess(null);
            }

            @Override
            public void onResponseError(String error, int code) {
                handleError(code, listener);
            }
        };
        mRequestManager.executeRequest(prxRequest, localListener);
    }

    @NonNull
    private RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    private void handleError(final int statusCode, final ResponseListener listener) {
        if (statusCode == ErrorType.INVALID_PRODUCT.getId()) {
            listener.onResponseError(ErrorType.INVALID_PRODUCT.getDescription(), statusCode);
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

    private boolean validateSerialNumberFromMetadata(final ProductData data, final ProdRegRequestInfo prodRegRequestInfo, ResponseListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, listener, prodRegRequestInfo)) return false;
        }
        return true;
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
}
