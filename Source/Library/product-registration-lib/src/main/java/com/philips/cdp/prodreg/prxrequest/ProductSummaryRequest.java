package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductSummaryRequest extends PrxRequest {

    private String mServerInfo;
    private String mCtn;

    public ProductSummaryRequest(String ctn) {
        this.mCtn = ctn;
    }

    @Override
    public String getServerInfo() {
        String mConfiguration = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        if (mConfiguration.equalsIgnoreCase("Development")) {
            mServerInfo = "https://10.128.41.113.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
            mServerInfo = "http://www.philips.co.uk/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
            mServerInfo = "https://acc.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://dev.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Production")) {
            mServerInfo = "https://www.philips.com/prx/product/";
        }
        return mServerInfo;
    }

    @Override
    public ResponseData getResponseData(final JSONObject jsonObject) {
        return new ProductSummaryResponse().parseJsonResponseData(jsonObject);
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
                .appendPath(mCtn + ".summary")
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

    public void setCtn(final String mCtn) {
        this.mCtn = mCtn;
    }
}
