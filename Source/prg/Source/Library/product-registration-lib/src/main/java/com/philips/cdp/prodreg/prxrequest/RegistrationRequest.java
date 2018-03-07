/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponse;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegistrationRequest extends PrxRequest {

    private static final String TAG = RegistrationRequest.class.getSimpleName();

    private String ctn = null;
    private String accessToken;
    private String productSerialNumber;
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

    public RegistrationRequest(String ctn, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
        this.ctn = ctn;
        this.serviceID = serviceID;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

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

    @Override
    public int getRequestType() {
        return RequestType.POST.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_KEY, getAccessToken());
        PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().getServiceUrlWithCountryPreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                ProdRegLogger.i("Registration Request","Registration Request :error :"+errorvalues.toString() + ":  message : "+s );
            }
            @Override
            public void onSuccess(URL url) {
                if (url.toString().contains(ProdRegConstants.CHINA_DOMAIN)){
                    headers.put(ProdRegConstants.CHINA_PROVIDER_KEY, ProdRegConstants.CHINA_PROVIDER_VAL);
                }
            }
        });
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

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
