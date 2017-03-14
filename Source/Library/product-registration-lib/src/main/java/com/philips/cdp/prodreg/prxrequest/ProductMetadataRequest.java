/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProductMetadataRequest extends PrxRequest {
    private static final String TAG = ProductMetadataRequest.class.getSimpleName();
    private String mServiceId;
    private String mCtn;

    public ProductMetadataRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        this.mServiceId = serviceID;
        this.mCtn = ctn;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductMetadataResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public void getRequestUrlFromAppInfra(AppInfraInterface appInfra, final PrxRequest.OnUrlReceived listener) {
        HashMap replaceUrl = new HashMap();
        replaceUrl.put("ctn", this.mCtn);
        replaceUrl.put("sector", this.getSector().toString());
        replaceUrl.put("catalog", this.getCatalog().toString());
        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(this.mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            public void onSuccess(URL url) {
                PrxLogger.i("SUCCESS ***", "" + url);
                String updateUrl = url.toString().replace(ProdRegConstants.PATH_PARAM_PRODUCT,ProdRegConstants.PATH_PARAM_REGISTRATION);
                listener.onSuccess(updateUrl);
            }

            public void onError(ERRORVALUES error, String message) {
                PrxLogger.i("ERRORVALUES ***", "" + message);
                listener.onError(error, message);
            }
        }, replaceUrl);
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

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
