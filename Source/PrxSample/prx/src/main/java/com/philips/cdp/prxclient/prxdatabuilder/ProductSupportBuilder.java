package com.philips.cdp.prxclient.prxdatabuilder;

import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.prxdatamodels.support.SupportModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Created by naveen@philips.com on 28-Mar-16.
 */
public class ProductSupportBuilder extends PrxDataBuilder {
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
        return String.format(PRX_SUPPORT_REQUEST_URL, getPRXBaseUrl(), getSectorCode(), getLocale(),
                getCatalogCode(), mCtn);
    }
}
