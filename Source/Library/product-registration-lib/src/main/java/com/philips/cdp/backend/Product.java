package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.handler.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.prxrequest.ProductMetadataRequest;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Product {

    private String ctn;
    private String serialNumber;
    private String purchaseDate;
    private Sector sector;
    private Catalog catalog;
    private String locale;

    public Product(String ctn, String serialNumber, String purchaseDate, Sector sector, Catalog catalog) {
        this.ctn = ctn;
        this.serialNumber = serialNumber;
        this.purchaseDate = purchaseDate;
        this.sector = sector;
        this.catalog = catalog;
    }

    public void getProductSummary() {

    }

    public void getProductMetadata(final Context context, final ProdRegListener metadataListener) {
        ProductMetadataRequest productMetadataRequest = getProductMetadataRequest(getCtn());
        productMetadataRequest.setSector(getSector());
        productMetadataRequest.setCatalog(getCatalog());
        productMetadataRequest.setmLocale(getLocale());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener metadataResponseListener = getMetadataResponseListener(metadataListener);
        mRequestManager.executeRequest(productMetadataRequest, metadataResponseListener);
    }

    @NonNull
    ResponseListener getMetadataResponseListener(final ProdRegListener metadataListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                metadataListener.onProdRegSuccess(responseData);
            }

            @Override
            public void onResponseError(String error, int code) {
                handleError(code, metadataListener);
            }
        };
    }

    @NonNull
    RequestManager getRequestManager(final Context context) {
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

    public String getCtn() {
        return ctn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Sector getSector() {
        return sector;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(final String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ProductMetadataRequest getProductMetadataRequest(String ctn) {
        return new ProductMetadataRequest(ctn);
    }
}
