package com.philips.cdp.prxclient.request;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Created by naveen@philips.com on 28-Mar-16.
 */
public class ProductSupportRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXFAQServiceID = "prxclient.support";

    public ProductSupportRequest(String ctn, String requestTag) {
        super(ctn, PRXFAQServiceID);
        this.mRequestTag = requestTag;
    }

    public ProductSupportRequest(String ctn, Sector sector, Catalog catalog, String requestTag) {
        super(ctn, PRXFAQServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SupportModel().parseJsonResponseData(jsonObject);
    }
}
