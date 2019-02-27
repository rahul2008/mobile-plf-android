package com.philips.cdp.prxclient.request;


import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.List;


/**
 * The type Product summary request.
 */
public class ProductSummaryListRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summarylist";
    private String mRequestTag = null;
    private List<String> ctns;


    /**
     * Instantiates a new Product summary request.
     * @since 1.0.0
     * @param ctn        product ctn
     * @param requestTag requestTag
     */
    public ProductSummaryListRequest(String ctn, String requestTag) {
        super(ctn, PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    /**
     * Instantiates a new Product summary request.
     * @since 1.0.0
     * @param ctns        product ctns
     * @param sector      sector
     * @param catalog     catalog
     * @param requestTag  request tag
     */
    public ProductSummaryListRequest(List<String> ctns, PrxConstants.Sector sector,
                                     PrxConstants.Catalog catalog, String requestTag) {
        super(ctns, PRXSummaryDataServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SummaryModel().parseJsonResponseData(jsonObject);
    }
}
