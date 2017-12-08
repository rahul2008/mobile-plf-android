/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prodreg.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prodreg.prxrequest.ProductSummaryRequest;
import com.philips.cdp.prxclient.PRXDependencies;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.platform.appinfra.AppInfraInterface;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 4081810711321162900L;
    protected String productModelNumber;
    protected String productSerialNumber;
    protected String purchaseDate;
    private PrxConstants.Sector sector;
    private PrxConstants.Catalog catalog;
    private String locale;
    private boolean shouldSendEmailAfterRegistration = true;
    private String friendlyName;

    public Product(String productModelNumber, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        this.productModelNumber = productModelNumber;
        this.sector = sector;
        this.catalog = catalog;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(final String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void getProductMetadata(final Context context, final MetadataListener metadataListener) {
        ProductMetadataRequest productMetadataRequest = getProductMetadataRequest(getCtn());
        productMetadataRequest.setSector(getSector());
        productMetadataRequest.setCatalog(getCatalog());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener metadataResponseListener = getPrxResponseListener(metadataListener);
        mRequestManager.executeRequest(productMetadataRequest, metadataResponseListener);
    }

    public void getProductSummary(final Context context, final Product product, final SummaryListener summaryListener) {
        ProductSummaryRequest productSummaryRequest = getProductSummaryRequest(product);
        productSummaryRequest.setSector(product.getSector());
        productSummaryRequest.setCatalog(product.getCatalog());
        RequestManager mRequestManager = getRequestManager(context);
        final ResponseListener summaryResponseListener = getPrxResponseListenerForSummary(summaryListener);
        mRequestManager.executeRequest(productSummaryRequest, summaryResponseListener);
    }

    @NonNull
    protected ProductSummaryRequest getProductSummaryRequest(final Product product) {
        return new ProductSummaryRequest(product.getCtn(),ProdRegConstants.PRODUCTSUMMERYREQUEST_SERVICE_ID,getSector(),getCatalog());
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

    @NonNull
    ResponseListener getPrxResponseListenerForSummary(final SummaryListener summaryListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductSummaryResponse productSummaryResponse = (ProductSummaryResponse) responseData;
                summaryListener.onSummaryResponse(productSummaryResponse);
            }

            @Override
            public void onResponseError(PrxError prxError) {
                summaryListener.onErrorResponse(prxError.getDescription(), prxError.getStatusCode());
            }
        };
    }

    protected Product getProduct() {
        return this;
    }

    @NonNull
    RequestManager getRequestManager(final Context context) {
        AppInfraInterface appInfra = PRUiHelper.getInstance().getAppInfraInstance();
        PRXDependencies   prxDependencies = new PRXDependencies(context , appInfra,ProdRegConstants.PRG_SUFFIX); // use existing appinfra instance
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(prxDependencies); // pass prxdependency

        return mRequestManager;
    }

    /**
     * API return Model Number
     *
     * @return return model number as string
     */
    public String getCtn() {
        productModelNumber = productModelNumber != null ? productModelNumber : "";
        return productModelNumber;
    }

    /**
     * API return Serial Number
     * @return return serial number as string
     */
    public String getSerialNumber() {
        productSerialNumber = productSerialNumber != null ? productSerialNumber : "";
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
    public PrxConstants.Sector getSector() {
        return sector;
    }

    /**
     * API return Catalog
     * @return return catalog as Enum's
     */
    public PrxConstants.Catalog getCatalog() {
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
        return new ProductMetadataRequest(ctn, ProdRegConstants.PRODUCTMETADATAREQUEST_SERVICE_ID,getSector(),getCatalog());
    }

}