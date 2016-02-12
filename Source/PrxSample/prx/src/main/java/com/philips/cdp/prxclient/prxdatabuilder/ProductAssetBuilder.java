package com.philips.cdp.prxclient.prxdatabuilder;

import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;

import org.json.JSONObject;

import com.philips.cdp.prxclient.response.ResponseData;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductAssetBuilder extends PrxDataBuilder {

    private String mCtn = null;
    private String mRequestTag = null;
    private static final String PRX_REQUEST_URL = "http://%s/product/%s/%s/%s/products/%s.assets";

    public ProductAssetBuilder(String ctn, String requestTag) {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return String.format(PRX_REQUEST_URL, getPRXBaseUrl(), getSectorCode(), getLocale(),
                getCatalogCode(), mCtn);
    }
}
