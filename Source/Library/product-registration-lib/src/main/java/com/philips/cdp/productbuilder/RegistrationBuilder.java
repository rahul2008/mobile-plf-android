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


    private static final String PRX_REQUEST_URL = "https://acc.philips.co.uk/prx/registration";
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
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

        private String generateUrl () {
            Uri builtUri = Uri.parse(getServerInfo())
                    .buildUpon()
                    .appendPath(getSectorCode())
                    .appendPath(getLocale())
                    .appendPath(getCatalogCode())
                    .appendPath("products")
                    .appendPath(mCtn + ".register.type.product")
                    .appendQueryParameter("productSerialNumber", getProductSerialNumber())
                    .appendQueryParameter("purchaseDate", getPurchaseDate())
                    .appendQueryParameter("registrationChannel", getRegistrationChannel())
                    .build();
            Log.d(getClass() + "", builtUri.toString());
            return builtUri.toString();
        }

}
