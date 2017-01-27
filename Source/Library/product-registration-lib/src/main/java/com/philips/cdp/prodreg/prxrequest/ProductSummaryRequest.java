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
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductSummaryRequest extends PrxRequest {
    private static final String TAG = ProductSummaryRequest.class.getSimpleName();
    private String mServerInfo;
    private String mCtn;
    private String mServiceId="";

    public ProductSummaryRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        mServiceId = serviceID;
    }

//    public ProductSummaryRequest(String ctn, String serviceId) {
//        super(ctn, serviceId);
//        mCtn = ctn;
//    }

//    public ProductSummaryRequest(String ctn) {
//        this.mCtn = ctn;
//    }

//    @Override
//    public String getServerInfo() {
//        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
//        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
//
//        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("prodreg.productSummaryRequest"
//                , new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//                    @Override
//                    public void onError(ERRORVALUES errorvalues, String s) {
//                        Log.d(TAG, " Response Error : " + s);
//                    }
//
//                    @Override
//                    public void onSuccess(URL url) {
//                        mServerInfo = url.toString();
//                    }
//                });
//
////        String mConfiguration = getRegistrationEnvironment();
////        if (mConfiguration.equalsIgnoreCase("Development")) {
////            mServerInfo = "https://10.128.41.113.philips.com/prx/product/";
////        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
////            mServerInfo = "https://tst.philips.com/prx/product/";
////        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
////            mServerInfo = "https://acc.philips.com/prx/product/";
////        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
////            mServerInfo = "https://dev.philips.com/prx/product/";
////        } else if (mConfiguration.equalsIgnoreCase("Production")) {
////            mServerInfo = "https://www.philips.com/prx/product/";
////        }
//        return mServerInfo;
//    }

    protected String getRegistrationEnvironment() {
        return RegistrationConfiguration.getInstance().getRegistrationEnvironment();
    }

    @Override
    public ResponseData getResponseData(final JSONObject jsonObject) {
        return new ProductSummaryResponse().parseJsonResponseData(jsonObject);
    }

    public String getRequestUrl(String url) {
        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendPath(this.getSector().toString())
                .appendPath(Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry())
                .appendPath(this.getCatalog().toString())
                .appendPath("products")
                .appendPath(mCtn + ".summary")
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
