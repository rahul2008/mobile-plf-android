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
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponse;
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

public class RegistrationRequest extends PrxRequest {

    private static final String TAG = RegistrationRequest.class.getSimpleName();

    private String ctn = null;
    private String accessToken;
    private String productSerialNumber;
    private String mServerInfo = "https://acc.philips.com/prx/registration/";
    private String purchaseDate;
    private String registrationChannel;
    private String sendEmail;
    private String address1;
    private String address2;
    private String address3;
    private String City;
    private String Zip;
    private String state;
    private String country;
    private String serviceID;

    private String shouldSendEmailAfterRegistration = "true";

    public RegistrationRequest(String ctn, String serviceID, Sector sector, Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        this.ctn = ctn;
        this.serviceID = serviceID;
    }

    //public RegistrationRequest(String ctn, String serviceId) {
      //  super(ctn, serviceId);
   // }

//    public RegistrationRequest(String ctn, final String serialNumber, String accessToken) {
//        this.ctn = ctn;
//        this.productSerialNumber = serialNumber;
//        this.accessToken = accessToken;
//    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

//    public String getServerInfo() {
////        String mConfiguration = RegistrationConfiguration.getInstance().getRegistrationEnvironment();
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
//        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
//        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
//
//        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("prodreg.productregistrationrequest"
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
//        return mServerInfo;
//    }

    public String getProductSerialNumber() {
        return productSerialNumber;
    }

    public void setProductSerialNumber(String productSerialNumber) {
        this.productSerialNumber = productSerialNumber;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getRegistrationChannel() {
        return registrationChannel;
    }

    public void setRegistrationChannel(String registrationChannel) {
        this.registrationChannel = registrationChannel;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCtn() {
        return ctn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new RegistrationResponse().parseJsonResponseData(jsonObject);
    }

    public String getShouldSendEmailAfterRegistration() {
        return shouldSendEmailAfterRegistration;
    }

    public void setShouldSendEmailAfterRegistration(final String shouldSendEmailAfterRegistration) {
        this.shouldSendEmailAfterRegistration = shouldSendEmailAfterRegistration;
    }


    public String getRequestUrl(String serviceURL) {
        Uri builtUri = Uri.parse(serviceURL)
                .buildUpon()
                .appendPath(getSector().name())
                .appendPath(PRUiHelper.getInstance().getLocale())
                .appendPath(getCatalog().name())
                .appendPath("products")
                .appendPath(ctn + ".register.type.product")
                .build();
        String url = builtUri.toString();
        try {
            url = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        ProdRegLogger.d(getClass() + "", url);
        return url;
    }

    public void getRequestUrlFromAppInfra(AppInfraInterface appInfra, final PrxRequest.OnUrlReceived listener) {
        HashMap replaceUrl = new HashMap();
        replaceUrl.put("ctn", ctn);
        replaceUrl.put("sector", this.getSector().toString());
        replaceUrl.put("catalog", this.getCatalog().toString());

        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            public void onSuccess(URL url) {
                PrxLogger.i("SUCCESS ***", "" + url);
                String chinaURL = url.toString();
                if(PRUiHelper.getInstance().getCountryCode().equalsIgnoreCase("CN")){
                    chinaURL = "https://acc.philips.com.cn/prx/registration/";
                }
               // String url1 = "https://acc.philips.com.cn/prx/registration/B2C/zh_CN/CONSUMER/products/XZ5810/70.register.type.product";
                listener.onSuccess(getRequestUrl(chinaURL));
            }

            public void onError(ERRORVALUES error, String message) {
                PrxLogger.i("Registration Request","Registration Request :error :"+error.toString() + ":  message : "+message );
                //PrxLogger.i("ERRORVALUES ***", "" + message);
                listener.onError(error, message);
            }
        });
    }


    @Override
    public int getRequestType() {
        return RequestType.POST.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        String ACCESS_TOKEN_TAG = "x-accessToken";
        final Map<String, String> headers = new HashMap<>();
        headers.put(ACCESS_TOKEN_TAG, getAccessToken());
        if(PRUiHelper.getInstance().getCountryCode().equalsIgnoreCase("CN")){
            headers.put("x-provider", "JANRAIN-CN");
        }
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        String REGISTRATION_CHANNEL = "registrationChannel";
        String SEND_EMAIL = "sendEmail";
        Map<String, String> params = new HashMap<>();
        validatePurchaseDate(params, getPurchaseDate());
        validateSerialNumber(params);
        params.put(REGISTRATION_CHANNEL, getRegistrationChannel());
        params.put(SEND_EMAIL, getShouldSendEmailAfterRegistration());
        return params;
    }

    private void validateSerialNumber(final Map<String, String> params) {
        String PRODUCT_SERIAL_NUMBER = "productSerialNumber";
        final String productSerialNumber = getProductSerialNumber();
        if (productSerialNumber != null && productSerialNumber.length() > 0)
            params.put(PRODUCT_SERIAL_NUMBER, productSerialNumber);
    }

    private void validatePurchaseDate(final Map<String, String> params, final String purchaseDate) {
        String PURCHASE_DATE = "purchaseDate";
        if (purchaseDate != null && purchaseDate.length() > 0)
            params.put(PURCHASE_DATE, purchaseDate);
    }

}
