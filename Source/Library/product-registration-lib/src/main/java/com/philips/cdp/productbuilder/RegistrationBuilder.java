package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationBuilder extends RegistrationDataBuilder {

    private String mCtn = null;

    public RegistrationBuilder(String ctn, String accessToken, final String serialNumber) {
        this.mCtn = ctn;
        this.accessToken = accessToken;
        setProductSerialNumber(serialNumber);
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("purchaseDate", getPurchaseDate());
        params.put("productSerialNumber", getProductSerialNumber());
        params.put("registrationChannel", getRegistrationChannel());
        return params;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-accessToken", accessToken);
        return headers;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSectorCode())
                .appendPath(getLocale())
                .appendPath(getCatalogCode())
                .appendPath("products")
                .appendPath(mCtn + ".register.type.product")
                .build();
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
