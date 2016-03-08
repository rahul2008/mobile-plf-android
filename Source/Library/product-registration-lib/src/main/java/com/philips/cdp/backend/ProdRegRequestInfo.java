package com.philips.cdp.backend;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRequestInfo {

    private String ctn, serialNumber;
    private Sector sector;
    private Catalog catalog;
    private String accessToken;
    private String purchaseDate;
    private String locale;
    private String registrationChannel;

    public ProdRegRequestInfo(String ctn, String serialNumber, Sector sector, Catalog catalog) {
        this.ctn = ctn;
        this.serialNumber = serialNumber;
        this.sector = sector;
        this.catalog = catalog;
    }

    public Sector getSector() {
        return sector;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getCtn() {
        return ctn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(final String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getRegistrationChannel() {
        return registrationChannel;
    }

    public void setRegistrationChannel(final String registrationChannel) {
        this.registrationChannel = registrationChannel;
    }

}
