package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.util.Log;

import com.philips.cdp.RequestConstants;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationBuilder extends RegistrationDataBuilder {

    private String mCtn = null;
    private int mRequestTag;

    public RegistrationBuilder(String ctn, String accessToken, final String serialNumber, final int mRequestTag) {
        this.mCtn = ctn;
        this.accessToken = accessToken;
        this.mRequestTag = mRequestTag;
        setProductSerialNumber(serialNumber);
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
        return generateUrl(getServerInfo());
    }

    private String generateUrl(String serverInfo) {
        Uri.Builder builtUri = Uri.parse(serverInfo)
                .buildUpon();
        buildUrl(builtUri);
        Log.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }

    private void buildUrl(final Uri.Builder builtUri) {
        switch (mRequestTag) {
            case RequestConstants.METADATA:
                break;
            case RequestConstants.REGISTER:
                builtUri.appendPath(getSectorCode())
                        .appendPath(getLocale())
                        .appendPath(getCatalogCode())
                        .appendPath("products")
                        .appendPath(mCtn + ".register.type.product")
                        .appendQueryParameter("productSerialNumber", getProductSerialNumber())
                        .appendQueryParameter("purchaseDate", getPurchaseDate())
                        .appendQueryParameter("registrationChannel", getRegistrationChannel())
                        .build();
                break;
            case RequestConstants.FETCH_PRODUCTS:
                break;
            case RequestConstants.EMAIL_SERVICES:
                break;
            default:
                break;
        }
    }
}
