package com.philips.cdp.productbuilder;

import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class RegistrationDataBuilder extends PrxDataBuilder {

    protected String accessToken;
    private String mServerInfo = "https://acc.philips.co.uk/prx/registration/";
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

    public abstract String getAccessToken();

    public void setAccessToken(String accessToken) {
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

    public abstract int getMethod();

    public abstract Map<String, String> getParams();

    public abstract Map<String, String> getHeaders();
}
