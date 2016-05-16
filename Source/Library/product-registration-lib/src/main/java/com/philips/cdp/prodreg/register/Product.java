package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Product {

    private String productModelNumber = "";
    private String productSerialNumber = "";
    private String purchaseDate;
    private Sector sector;
    private Catalog catalog;
    private String locale;
    private boolean shouldSendEmailAfterRegistration;

    public Product(String productModelNumber, Sector sector, Catalog catalog) {
        this.productModelNumber = productModelNumber;
        this.sector = sector;
        this.catalog = catalog;
    }

    protected void getProductMetadata(final Context context, final MetadataListener metadataListener) {
        ProductMetadataRequest productMetadataRequest = getProductMetadataRequest(getCtn());
        productMetadataRequest.setSector(getSector());
        productMetadataRequest.setCatalog(getCatalog());
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
            public void onResponseError(PrxError prxError) {
                metadataListener.onErrorResponse(prxError.getDescription(), prxError.getStatusCode());
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

    /**
     * API return Model Number
     *
     * @return return model number as string
     */
    public String getCtn() {
        return productModelNumber;
    }

    /**
     * API return Serial Number
     * @return return serial number as string
     */
    public String getSerialNumber() {
        return productSerialNumber;
    }

    /**
     * API set the serial number
     * @param serialNumber serial Number
     */

    public void setSerialNumber(final String serialNumber) {
        this.productSerialNumber = serialNumber;
    }

    /**
     * API return Sector
     * @return return sector as Enum's
     */
    public Sector getSector() {
        return sector;
    }

    /**
     * API return Catalog
     * @return return catalog as Enum's
     */
    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * API return Locale
     * @return return local as string
     */
    public String getLocale() {
        return locale;
    }

    /**
     * API set Local
     * @param locale local
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * API return date
     * @return Purchase date
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * API set Date
     * @param purchaseDate purchase date
     */

    public void setPurchaseDate(final String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * API return email
     * @return return Email
     */
    public boolean getEmail() {
        return shouldSendEmailAfterRegistration;
    }

    /**
     * API need to set email confirmations
     * @param shouldSendEmailAfterRegistration shouldSendEmailAfterRegistration
     */
    public void sendEmail(final boolean shouldSendEmailAfterRegistration) {
        this.shouldSendEmailAfterRegistration = shouldSendEmailAfterRegistration;
    }

    /**
     * API need to pass CTN to get Metadata
     * @param ctn ctn
     * @return ctn as string
     */
    public ProductMetadataRequest getProductMetadataRequest(String ctn) {
        return new ProductMetadataRequest(ctn);
    }

}
