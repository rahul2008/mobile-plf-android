/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;

import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ProductSummaryRequest extends PrxRequest {
    private static final String TAG = ProductSummaryRequest.class.getSimpleName();
    private String mServerInfo;
    private String mCtn;

    public ProductSummaryRequest(String ctn) {
        this.mCtn = ctn;
    }

    @Override
    public String getServerInfo() {
        String mConfiguration = getRegistrationEnvironment();
        if (mConfiguration.equalsIgnoreCase("Development")) {
            mServerInfo = "https://10.128.41.113.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
            mServerInfo = "https://tst.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
            mServerInfo = "https://acc.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://acc.philips.com/prx/product/";
        } else if (mConfiguration.equalsIgnoreCase("Production")) {
            mServerInfo = "https://www.philips.com/prx/product/";
        }
        return mServerInfo;
    }

    protected String getRegistrationEnvironment() {
        return RegistrationConfiguration.getInstance().getRegistrationEnvironment();
    }

    @Override
    public ResponseData getResponseData(final JSONObject jsonObject) {
        return new ProductSummaryResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSector().name())
                .appendPath(getLocaleMatchResult())
                .appendPath(getCatalog().name())
                .appendPath("products")
                .appendPath(mCtn + ".summary")
                .build();
        String url = builtUri.toString();
        try {
            url = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        ProdRegLogger.d(getClass() + "URl :", builtUri.toString());
        return url;
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

    public void setCtn(final String mCtn) {
        this.mCtn = mCtn;
    }

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
