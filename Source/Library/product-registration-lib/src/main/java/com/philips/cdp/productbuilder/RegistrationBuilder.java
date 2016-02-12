package com.philips.cdp.productbuilder;


import android.net.Uri;
import android.util.Log;

import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationBuilder extends RegistrationDataBuilder {

    private static final String PRX_REQUEST_URL = "https://acc.philips.co.uk/prx/registration/";
    private String mCtn = null;
    private String mRequestTag = null;

    public RegistrationBuilder(String ctn, String requestTag) {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
//        generateUrl();
        return new ProductResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return String.format(PRX_REQUEST_URL, getServerInfo(), getSectorCode(), getLocale(),
                getCatalogCode(), mCtn);
    }

    private void generateUrl() {
        Uri.Builder builder = new Uri.Builder();
//        Uri.Builder builder = Uri.parse(getServerInfo());
        builder.scheme("http")
                .authority("www.lapi.transitchicago.com")
                .appendPath("api")
                .appendPath("1.0")
                .appendPath("ttarrivals.aspx")
                .appendQueryParameter("key", "[redacted]")
                .appendQueryParameter("mapid", "yogesh");

        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendQueryParameter("key", "[redacted]")
                .appendQueryParameter("mapid", "yogesh")
                .build();
        Log.d(getClass() + "", builtUri.toString());
    }
}
