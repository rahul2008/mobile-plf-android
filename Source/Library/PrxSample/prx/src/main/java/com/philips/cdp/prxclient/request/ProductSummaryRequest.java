package com.philips.cdp.prxclient.request;


import android.util.Log;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductSummaryRequest extends PrxRequest {

    //    private static final String PRX_REQUEST_URL = "https://ave.bolyartech.com:44401/https_test.html";
    // private static final String PRX_REQUEST_URL = "https://%s/product/%s/%s/%s/products/%s.summary";
    private static final String PRXSummaryDataServiceID = "prxclient.summary";
 //   private String mCtn = null;
    private String mRequestTag = null;


    public ProductSummaryRequest(String ctn, String requestTag) {
        super.initCtn(ctn,PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {

        return new SummaryModel().parseJsonResponseData(jsonObject);
    }

//    @Override
//    public void getRequestUrlFromAppInfra(final AppInfraInterface appInfra, final OnUrlReceived listener) {
//
//        appInfra.getServiceDiscovery().getServiceLocaleWithCountryPreference(PRXSummaryDataServiceID,
//                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//                    @Override
//                    public void onSuccess(final String locale) {
//                        Map<String, String> parameters = new HashMap<>();
//                        parameters.put("ctn", mCtn);
//                        parameters.put("sector", getSector().toString());
//                        parameters.put("catalog", getCatalog().toString());
//                        parameters.put("locale", locale);
//
//                        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(PRXSummaryDataServiceID,
//                                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//                                    @Override
//                                    public void onSuccess(URL url) {
//                                        Log.i("SUCCESS ***", "" + url);
//                                        listener.onSuccess(url.toString());
//                                    }
//
//                                    @Override
//                                    public void onError(ERRORVALUES error, String message) {
//                                        Log.i("ERRORVALUES ***", "" + message);
//                                        listener.onError(error, message);
//                                    }
//                                }, parameters);
//                    }
//
//                    @Override
//                    public void onError(ERRORVALUES errorvalues, String s) {
//                        listener.onError(errorvalues,s);
//                    }
//                });
//    }

//    @Override
//    public String getRequestUrl() {
//        return String.format(PRX_REQUEST_URL, getServerInfo(), getSector(), getLocaleMatchResult(),
//                getCatalog(), mCtn);
//    }

//    @Override
//    public int getRequestType() {
//        return RequestType.GET.getValue();
//    }

//    @Override
//    public Map<String, String> getHeaders() {
//        return null;
//    }
//
//    @Override
//    public Map<String, String> getParams() {
//        return null;
//    }
}
