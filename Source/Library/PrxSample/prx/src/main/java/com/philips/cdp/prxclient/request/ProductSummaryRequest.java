package com.philips.cdp.prxclient.request;


import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductSummaryRequest extends PrxRequest {

    //    private static final String PRX_REQUEST_URL = "https://ave.bolyartech.com:44401/https_test.html";
    private static final String PRX_REQUEST_URL = "https://%s/product/%s/%s/%s/products/%s.summary";
    private String mCtn = null;
    private String mRequestTag = null;

    public ProductSummaryRequest(String ctn, String requestTag) {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {

        return new SummaryModel().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return String.format(PRX_REQUEST_URL, getServerInfo(), getSector(), getLocaleMatchResult(),
                getCatalog(), mCtn);
    }

    @Override
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
