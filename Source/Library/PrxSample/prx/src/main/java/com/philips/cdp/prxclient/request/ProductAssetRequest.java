package com.philips.cdp.prxclient.request;

import android.util.Log;

import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
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
public class ProductAssetRequest extends PrxRequest {

    //private static final String PRX_REQUEST_URL = "https://%s/product/%s/%s/%s/products/%s.assets";
    private String mRequestTag = null;
    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    public ProductAssetRequest(String ctn, String requestTag) {
        super.initCtn(ctn, PRXAssetAssetServiceID);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

//    @Override
//    public String getRequestUrl() {
//        return String.format(PRX_REQUEST_URL, getServerInfo(), getSector(), getLocaleMatchResult(),
//                getCatalog(), mCtn);
//    }


//    public void getRequestUrlFromAppInfra(final AppInfraInterface appInfra, final OnUrlReceived listener) {
//        appInfra.getServiceDiscovery().getServiceLocaleWithLanguagePreference(PRXAssetAssetServiceID,
//                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//                    @Override
//                    public void onSuccess(String locale) {
//                        Map<String, String> replaceUrl = new HashMap<>();
//                        replaceUrl.put("ctn", mCtn);
//                        replaceUrl.put("sector", getSector().toString());
//                        replaceUrl.put("catalog", getCatalog().toString());
//                        replaceUrl.put("locale", locale);
//                        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(PRXAssetAssetServiceID,
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
//                                }, replaceUrl);
//                    }
//
//                    @Override
//                    public void onError(ERRORVALUES errorvalues, String message) {
//                        listener.onError(errorvalues, message);
//                    }
//                });
//    }

//    @Override
//    public int getRequestType() {
//        return RequestType.GET.getValue();
//    }
//
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
