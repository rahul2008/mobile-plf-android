/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisteredProductsRequest extends PrxRequest {

    private String accessToken;
    private String mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new RegisteredResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getServerInfo() {
        String mConfiguration = RegistrationConfiguration.getInstance().getRegistrationEnvironment();
        if (mConfiguration.equalsIgnoreCase("Development")) {
            mServerInfo = "https://10.128.41.113.philips.com/prx/registration.registeredProducts";
        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
            mServerInfo = "https://tst.philips.com/prx/registration.registeredProducts";
        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
            mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";
        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";
        } else if (mConfiguration.equalsIgnoreCase("Production")) {
            mServerInfo = "https://www.philips.com/prx/registration.registeredProducts";
        }
        return mServerInfo;
    }

    @Override
    public String getRequestUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .build();
        ProdRegLogger.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }

    @Override
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        String ACCESS_TOKEN_TAG = "x-accessToken";
        final Map<String, String> headers = new HashMap<>();
        headers.put(ACCESS_TOKEN_TAG, getAccessToken());
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Sector getSector() {
        return Sector.DEFAULT;
    }

    @Override
    public Catalog getCatalog() {
        return Catalog.DEFAULT;
    }

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
