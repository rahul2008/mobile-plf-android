package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by naveen@philips.com on 28-Mar-16.
 */
public class ProductSupportBuilder extends PrxRequest {
    private String mCtn = null;
    private String mRequestTag = null;
    private static final String PRX_SUPPORT_REQUEST_URL = "http://%s/product/%s/%s/%s/products/%s.support";

    public ProductSupportBuilder(String ctn, String requestTag) {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SupportModel().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return String.format(PRX_SUPPORT_REQUEST_URL, getServerInfo(),  getSector(), getLocale(),
                getCatalog(), mCtn);
    }

    @Override
    public int getRequestType() {
        return 0;
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
