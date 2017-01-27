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
    private String mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";
    private String mServiceId ="";

    public RegisteredProductsRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        mServiceId = serviceID;
    }

//    public RegisteredProductsRequest(String ctn, String serviceId) {
//        super(ctn, serviceId);
//    }


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

//    @Override
//    public String getServerInfo() {
////        String mConfiguration = getRegistrationEnvironment();
////        if (mConfiguration.equalsIgnoreCase("Development")) {
////            mServerInfo = "https://10.128.41.113.philips.com/prx/registration.registeredProducts";
////        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
////            mServerInfo = "https://tst.philips.com/prx/registration.registeredProducts";
////        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
////            mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";
////        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
////            mServerInfo = "https://dev.philips.com/prx/registration.registeredProducts";
////        } else if (mConfiguration.equalsIgnoreCase("Production")) {
////            mServerInfo = "https://www.philips.com/prx/registration.registeredProducts";
////        }
//        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
//        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
//
//        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("prodreg.registeredProductsRequest",
//                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
//        return mServerInfo;
//    }

    protected String getRegistrationEnvironment() {
        return RegistrationConfiguration.getInstance().getRegistrationEnvironment();
    }

//    @Override
    public String getRequestUrl(String url) {
        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .build();
        ProdRegLogger.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }
public void getRequestUrlFromAppInfra(AppInfraInterface appInfra, final PrxRequest.OnUrlReceived listener) {

    appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
