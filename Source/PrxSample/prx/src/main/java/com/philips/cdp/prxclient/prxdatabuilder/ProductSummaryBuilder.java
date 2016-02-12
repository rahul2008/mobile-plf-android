package com.philips.cdp.prxclient.prxdatabuilder;


import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;



/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductSummaryBuilder extends PrxDataBuilder {

    private String mCtn = null;
    private String mRequestTag = null;
    private static final String PRX_REQUEST_URL = "http://%s/product/%s/%s/%s/products/%s.summary";

    public ProductSummaryBuilder(String ctn, String requestTag)
    {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {

        return new SummaryModel().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return String.format(PRX_REQUEST_URL, getPRXBaseUrl(), getSectorCode(),getLocale(),
                getCatalogCode(), mCtn);
    }
}
