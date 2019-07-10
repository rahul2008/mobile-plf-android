/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;


import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisteredProductsRequest extends PrxRequest {

    private String accessToken;
    private String mServiceId;

    public RegisteredProductsRequest(String ctn, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
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
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_KEY, getAccessToken());
        ArrayList<String> serviceIDList = new ArrayList<>();
        serviceIDList.add(mServiceId);
        PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().
                getServicesWithCountryPreference(serviceIDList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        String url = urlMap.get(mServiceId).getConfigUrls();
                        if (url.contains(ProdRegConstants.CHINA_DOMAIN)){
                            headers.put(ProdRegConstants.CHINA_PROVIDER_KEY, ProdRegConstants.CHINA_PROVIDER_VAL_CN);
                        }
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        ProdRegLogger.i("Registration Request","Registration Request :error :"+error.toString() + ":  message : "+message );
                    }
                },null);
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public PrxConstants.Sector getSector() {
        return PrxConstants.Sector.DEFAULT;
    }

    @Override
    public PrxConstants.Catalog getCatalog() {
        return PrxConstants.Catalog.DEFAULT;
    }

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
