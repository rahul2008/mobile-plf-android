/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;
import android.util.Log;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment.TAG;

public class RegisteredProductsRequest extends PrxRequest {

    private String accessToken;
    private String mServiceId;

    public RegisteredProductsRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        mServiceId = serviceID;
    }

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
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        String ACCESS_TOKEN_TAG = "x-accessToken";
        final Map<String, String> headers = new HashMap<>();
        headers.put(ACCESS_TOKEN_TAG, getAccessToken());
        PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                PrxLogger.i("Registration Request","Registration Request :error :"+errorvalues.toString() + ":  message : "+s );
            }
            @Override
            public void onSuccess(URL url) {
                if (url.toString().contains("philips.co.cn")){
                    headers.put("x-provider", "JANRAIN-CN");
                }
            }
        });
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
