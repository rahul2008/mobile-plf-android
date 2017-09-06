package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Product Support Request class
 */
public class ProductSupportRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXFAQServiceID = "prxclient.support";

    public ProductSupportRequest(String ctn, String requestTag) {
        super(ctn, PRXFAQServiceID);
        this.mRequestTag = requestTag;
    }

    public ProductSupportRequest(String ctn, PrxConstants.Sector sector,
                                 PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXFAQServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SupportModel().parseJsonResponseData(jsonObject);
    }
}
