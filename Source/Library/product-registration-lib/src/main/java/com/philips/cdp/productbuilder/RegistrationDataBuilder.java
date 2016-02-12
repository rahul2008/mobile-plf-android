package com.philips.cdp.productbuilder;

import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class RegistrationDataBuilder extends PrxDataBuilder {

    private String mServerInfo = "www.philips.com/prx";
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;
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

    public String getServerInfo() {
        return mServerInfo;
    }

    public String getSectorCode() {
        return mSectorCode;
    }

    public void setmSectorCode(String mSectorCode) {
        this.mSectorCode = mSectorCode;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setmLocale(String mLocale) {
        this.mLocale = mLocale;
    }

    public String getCatalogCode() {
        return mCatalogCode;
    }

    public void setmCatalogCode(String mCatalogCode) {
        this.mCatalogCode = mCatalogCode;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract String getRequestUrl();

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
}
