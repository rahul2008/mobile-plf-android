package com.philips.cdp.productrequest;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.model.ProdRegResponse;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationRequest extends PrxRequest {

    private String mCtn = null;
    private String accessToken;
    private String productSerialNumber;
    private String mServerInfo = "https://dev.philips.com/prx/registration/";
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

    public RegistrationRequest(String ctn, final String serialNumber, String accessToken) {
        this.mCtn = ctn;
        this.productSerialNumber = serialNumber;
        this.accessToken = accessToken;
    }

    public String getServerInfo() {
        return mServerInfo;
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
        return mCtn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ProdRegResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    @Override
    public int getRequestType() {
        return RequestType.POST.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_TAG, getAccessToken());
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        validatePurchaseDate(params, getPurchaseDate());
        validateSerialNumber(params);
        params.put(ProdRegConstants.REGISTRATION_CHANNEL, getRegistrationChannel());
        return params;
    }

    private void validateSerialNumber(final Map<String, String> params) {
        final String productSerialNumber = getProductSerialNumber();
        if (productSerialNumber != null && productSerialNumber.length() > 0)
            params.put(ProdRegConstants.PRODUCT_SERIAL_NUMBER, productSerialNumber);
    }

    private void validatePurchaseDate(final Map<String, String> params, final String purchaseDate) {
        if (purchaseDate != null && purchaseDate.length() > 0)
            params.put(ProdRegConstants.PURCHASE_DATE, purchaseDate);
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .appendPath(getSector().name())
                .appendPath(getLocaleMatchResult())
                .appendPath(getCatalog().name())
                .appendPath("products")
                .appendPath(mCtn + ".register.type.product")
                .build();
        return getDecodedUrl(builtUri);
    }

    @NonNull
    private String getDecodedUrl(final Uri builtUri) {
        String url = builtUri.toString();
        try {
            url = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(getClass() + "", url);
        return url;
    }
}
