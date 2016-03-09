package com.philips.cdp.productrequest;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductMetaRequest extends PrxRequest {

    private String mCtn = null;

    public ProductMetaRequest(String ctn) {
        this.mCtn = ctn;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductMetaData().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getServerInfo() {
        return "https://acc.philips.co.uk/prx/registration/";
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
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

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSector().name())
                .appendPath(getLocale())
                .appendPath(getCatalog().name())
                .appendPath("products")
                .appendPath(mCtn + ".metadata")
                .build();
        Log.d(getClass() + "URl :", builtUri.toString());
        return getDecodedUrl(builtUri);
    }

    @NonNull
    private String getDecodedUrl(final Uri builtUri) {
        String url = builtUri.toString();
        try {
            url = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(getClass() + "", url);
        return url;
    }
}
