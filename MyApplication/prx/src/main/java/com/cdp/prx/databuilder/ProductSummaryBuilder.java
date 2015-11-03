package com.cdp.prx.databuilder;

import com.cdp.prx.assets.AssetModel;
import com.cdp.prx.summary.SummaryModel;

import org.json.JSONObject;

import horizontal.cdp.prxcomponent.PrxDataBuilder;
import horizontal.cdp.prxcomponent.ResponseData;

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
        return String.format(PRX_REQUEST_URL, getServerInfo(), getSectorCode(),getLocale(),
                getCatalogCode(), getCtnCode());
    }
}
