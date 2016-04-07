package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.handler.ErrorType;
import com.philips.cdp.prodreg.handler.MetadataListener;
import com.philips.cdp.prodreg.model.ProductMetadataResponse;
import com.philips.cdp.prodreg.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Product implements Serializable {

    private String ctn;
    private String serialNumber;
    private String purchaseDate;
    private Sector sector;
    private Catalog catalog;
    private String locale;
    private String path;

    public Product(String ctn, String serialNumber, String purchaseDate, Sector sector, Catalog catalog) {
        this.ctn = ctn;
        this.serialNumber = serialNumber;
        this.purchaseDate = purchaseDate;
        this.sector = sector;
        this.catalog = catalog;
    }

    public void getProductMetadata(final Context context, final MetadataListener metadataListener) {
        ProductMetadataRequest productMetadataRequest = getProductMetadataRequest(getCtn());
        productMetadataRequest.setSector(getSector());
        productMetadataRequest.setCatalog(getCatalog());
        productMetadataRequest.setmLocale(getLocale());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener metadataResponseListener = getPrxResponseListener(metadataListener);
        mRequestManager.executeRequest(productMetadataRequest, metadataResponseListener);
    }

    @NonNull
    ResponseListener getPrxResponseListener(final MetadataListener metadataListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetadataResponse productMetaData = (ProductMetadataResponse) responseData;
                metadataListener.onMetadataResponse(productMetaData);
            }

            @Override
            public void onResponseError(String error, int code) {
                metadataListener.onErrorResponse(ErrorType.METADATA_FAILED.getDescription(), ErrorType.METADATA_FAILED.getCode());
            }
        };
    }

    protected Product getProduct() {
        return this;
    }

    @NonNull
    RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
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

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
