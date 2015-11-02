package com.cdp.prx.databuilder;

import horizontal.cdp.prxcomponent.PrxDataBuilder;
import horizontal.cdp.prxcomponent.ResponseData;

public class ProductAssetBuilder extends PrxDataBuilder {

    private String mCtn = null;
    private String mRequestTag = null;

    public ProductAssetBuilder(String ctn, String requestTag)
    {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        return null;
    }
}
