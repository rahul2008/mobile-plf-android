package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.core.ProdRegConstants;
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
        this.productSerialNumber = serialNumber;
    }

    public String getCtn() {
        return mCtn;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_TAG, getAccessToken());
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        validatePurchaseDate(params, getPurchaseDate());
        validateSerialNumber(params);
        params.put(ProdRegConstants.REGISTRATION_CHANNEL, getRegistrationChannel());
        return params;
    }

    private void validateSerialNumber(final Map<String, String> params) {
        final String productSerialNumber = getProductSerialNumber();
        if (isRequiresSerialNumber() && productSerialNumber != null && productSerialNumber.length() > 0)
            params.put(ProdRegConstants.PRODUCT_SERIAL_NUMBER, productSerialNumber);
    }

    private void validatePurchaseDate(final Map<String, String> params, final String purchaseDate) {
        if (isRequiresPurchaseDate() && purchaseDate != null && purchaseDate.length() > 0)
            params.put(ProdRegConstants.PURCHASE_DATE, purchaseDate);
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSector().name())
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
