package com.philips.cdp.prxclient.request;


import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;


/**
 * The type Product summary request.
 */
public class ProductSpecificationRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.specification";
    private String mRequestTag = null;


    /**
     * Instantiates a new Product summary request.
     * @since 1.0.0
     * @param ctn        product ctn
     * @param requestTag requestTag
     */
    public ProductSpecificationRequest(String ctn, String requestTag) {
        super(ctn, PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    /**
     * Instantiates a new Product summary request.
     * @since 1.0.0
     * @param ctn         product ctn
     * @param sector      sector
     * @param catalog     catalog
     * @param requestTag  request tag
     */
    public ProductSpecificationRequest(String ctn, PrxConstants.Sector sector,
                                       PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXSummaryDataServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SpecificationModel().parseJsonResponseData(jsonObject);
    }
}
