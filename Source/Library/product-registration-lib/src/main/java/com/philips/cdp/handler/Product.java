package com.philips.cdp.handler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.backend.PRXDataBuilderFactory;
import com.philips.cdp.backend.PRXRequestType;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.error.ErrorType;
import com.philips.cdp.model.ProdRegMetaData;
import com.philips.cdp.model.ProdRegMetaDataResponse;
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

    public void getProductSummary() {

    }

    public void getProductMetadata(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.METADATA, prodRegRequestInfo, new User(context).getAccessToken());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener localListener = getMetadataResponseListener(prodRegRequestInfo, listener);
        mRequestManager.executeRequest(prxRequest, localListener);
    }

    @NonNull
    private ResponseListener getMetadataResponseListener(final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProdRegMetaData productMetaData = (ProdRegMetaData) responseData;
                ProdRegMetaDataResponse productData = productMetaData.getData();
                if (validateSerialNumberFromMetadata(productData, prodRegRequestInfo, listener)
                        && validatePurchaseDateFromMetadata(productData, prodRegRequestInfo, listener))
                    listener.onProdRegSuccess(null);
            }

            @Override
            public void onResponseError(String error, int code) {
                handleError(code, listener);
            }
        };
    }

    @NonNull
    private RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    protected void handleError(final int statusCode, final ProdRegListener listener) {
        if (statusCode == ErrorType.INVALID_CTN.getCode()) {
            listener.onProdRegFailed(ErrorType.INVALID_CTN);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getCode()) {
            listener.onProdRegFailed(ErrorType.INVALID_VALIDATION);
        } else if (statusCode == ErrorType.INVALID_SERIALNUMBER.getCode()) {
            listener.onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
        } else if (statusCode == ErrorType.NO_INTERNET_AVAILABLE.getCode()) {
            listener.onProdRegFailed(ErrorType.NO_INTERNET_AVAILABLE);
        } else if (statusCode == ErrorType.INTERNAL_SERVER_ERROR.getCode()) {
            listener.onProdRegFailed(ErrorType.INTERNAL_SERVER_ERROR);
        } else {
            listener.onProdRegFailed(ErrorType.UNKNOWN);
        }
    }

    protected boolean validateSerialNumberFromMetadata(final ProdRegMetaDataResponse data, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, listener, prodRegRequestInfo)) return false;
        } else {
            prodRegRequestInfo.setSerialNumber(null);
        }
        return true;
    }

    private boolean processSerialNumber(final ProdRegMetaDataResponse data, final ProdRegListener listener, ProdRegRequestInfo prodRegRequestInfo) {
        if (prodRegRequestInfo.getSerialNumber() == null || prodRegRequestInfo.getSerialNumber().length() < 1) {
            listener.onProdRegFailed(ErrorType.MISSING_SERIALNUMBER);
            return true;
        } else if (!prodRegRequestInfo.getSerialNumber().matches(data.getSerialNumberFormat())) {
            listener.onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
            return true;
        }
        return false;
    }

    protected boolean validatePurchaseDateFromMetadata(final ProdRegMetaDataResponse data, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        final String purchaseDate = prodRegRequestInfo.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                listener.onProdRegFailed(ErrorType.MISSING_DATE);
                return false;
            }
        } else
            prodRegRequestInfo.setPurchaseDate(null);
        return true;
    }
}
