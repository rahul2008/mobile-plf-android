package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductMetaDataBuilder extends MetaDataBuilder {

    private static final String PRX_REQUEST_URL = "https://acc.philips.co.uk/prx/registration";
    private String mCtn = null;

    public ProductMetaDataBuilder(String ctn, String accessToken) {
        this.mCtn = ctn;
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductMetaData().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSectorCode())
                .appendPath(getLocale())
                .appendPath(getCatalogCode())
                .appendPath("products")
                .appendPath(mCtn + ".metadata")
                .build();
        Log.d(getClass() + "URl :", builtUri.toString());
        return "https://acc.philips.co.uk/prx/registration/B2C/en_GB/CARE/products/HD8967/01.metadata";
    }
}
