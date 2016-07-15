package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataRequest extends PrxRequest {
    private static final String TAG = ProductMetadataRequest.class.getSimpleName();
    private String mCtn = null;
    private String mServerInfo = "https://acc.philips.com/prx/registration/";

    public ProductMetadataRequest(String ctn) {
        this.mCtn = ctn;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductMetadataResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getServerInfo() {
        String mConfiguration = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        if (mConfiguration.equalsIgnoreCase("Development")) {
            mServerInfo = "https://10.128.41.113.philips.com/prx/registration/";
        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
            mServerInfo = "https://tst.philips.com/prx/registration/";
        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
            mServerInfo = "https://acc.philips.com/prx/registration/";
        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://dev.philips.com/prx/registration/";
        } else if (mConfiguration.equalsIgnoreCase("Production")) {
            mServerInfo = "https://www.philips.com/prx/registration/";
        }
        return mServerInfo;
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
                .appendPath(getLocaleMatchResult())
                .appendPath(getCatalog().name())
                .appendPath("products")
                .appendPath(mCtn + ".metadata")
                .build();
        ProdRegLogger.d(getClass() + "URl :", builtUri.toString());
        return getDecodedUrl(builtUri);
    }

    @NonNull
    private String getDecodedUrl(final Uri builtUri) {
        String url = builtUri.toString();
        try {
            url = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        ProdRegLogger.d(getClass() + "", url);
        return url;
    }

    public void setCtn(final String mCtn) {
        this.mCtn = mCtn;
    }

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
