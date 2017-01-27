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
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductMetadataRequest extends PrxRequest {
    private static final String TAG = ProductMetadataRequest.class.getSimpleName();
    private String mCtn = null;
    private String mServerInfo = "https://acc.philips.com/prx/registration/";
    private String mServiceId="";

    public ProductMetadataRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        this.mServiceId=serviceID;
        this.mCtn = ctn;
    }

//    public ProductMetadataRequest(String ctn, String serviceId) {
//        super(ctn, serviceId);
//        this.mServiceId=serviceId;
//        this.mCtn = ctn;
//    }

    //  public ProductMetadataRequest(String ctn) {
    //     this.mCtn = ctn;
    //}

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProductMetadataResponse().parseJsonResponseData(jsonObject);
    }

//    @Override
//    public String getServerInfo() {
//        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
//        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
//
//        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("prodreg.productmetadatarequest"
//                , new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//                    @Override
//                    public void onError(ERRORVALUES errorvalues, String s) {
//                        Log.d(TAG, " Response Error : " + s);
//
//                    }
//
//                    @Override
//                    public void onSuccess(URL url) {
//                        mServerInfo = url.toString();
//                    }
//                });
////        String mConfiguration = getRegistrationEnvironment();
////        if (mConfiguration.equalsIgnoreCase("Development")) {
////            mServerInfo = "https://10.128.41.113.philips.com/prx/registration/";
////        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
////            mServerInfo = "https://tst.philips.com/prx/registration/";
////        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
////            mServerInfo = "https://acc.philips.com/prx/registration/";
////        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
////            mServerInfo = "https://dev.philips.com/prx/registration/";
////        } else if (mConfiguration.equalsIgnoreCase("Production")) {
////            mServerInfo = "https://www.philips.com/prx/registration/";
////        }
//        return mServerInfo;
//    }

    protected String getRegistrationEnvironment() {
        return RegistrationConfiguration.getInstance().getRegistrationEnvironment();
    }



    public String getRequestUrl(String url) {
        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendPath(this.getSector().toString())
                .appendPath(Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry())
                .appendPath(this.getCatalog().toString())
                .appendPath("products")
                .appendPath(mCtn + ".metadata")
                .build();
        String retunUrl = builtUri.toString();
        try {
            retunUrl = java.net.URLDecoder.decode(retunUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        ProdRegLogger.d(getClass() + "URl :", retunUrl);
        return retunUrl;
    }

    public void getRequestUrlFromAppInfra(AppInfraInterface appInfra, final PrxRequest.OnUrlReceived listener) {
        HashMap replaceUrl = new HashMap();
        replaceUrl.put("ctn", this.mCtn);
        replaceUrl.put("sector", this.getSector().toString());
        replaceUrl.put("catalog", this.getCatalog().toString());
        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(this.mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            public void onSuccess(URL url) {
                PrxLogger.i("SUCCESS ***", "" + url);
                Log.d(TAG, " Request URL " + getRequestUrl(url.toString()));
                listener.onSuccess(getRequestUrl(url.toString()));
            }

            public void onError(ERRORVALUES error, String message) {
                PrxLogger.i("ERRORVALUES ***", "" + message);
                listener.onError(error, message);
            }
        });
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
