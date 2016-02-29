package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.util.Log;

import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductMetaDataBuilder extends RegistrationDataBuilder {

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
        return "https://acc.philips.co.uk/prx/registration/B2C/en_GB/CARE/products/HD8967/01.metadata";
    }
}
