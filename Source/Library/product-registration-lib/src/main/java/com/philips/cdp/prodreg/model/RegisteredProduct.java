package com.philips.cdp.prodreg.model;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.backend.Product;
import com.philips.cdp.prodreg.handler.ProdRegError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProduct extends Product {

    private RegistrationState registrationState;
    private String endWarrantyDate;
    private String userUUid;
    private String shouldSendEmailAfterRegistration;
    private ProdRegError prodRegError;

    public RegisteredProduct(final String ctn, final String serialNumber, final String purchaseDate, final Sector sector, final Catalog catalog) {
        super(ctn, serialNumber, purchaseDate, sector, catalog);
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(final RegistrationState registrationState) {
        this.registrationState = registrationState;
    }

    public String getEndWarrantyDate() {
        return endWarrantyDate;
    }

    public void setEndWarrantyDate(final String endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    public String getUserUUid() {
        return userUUid;
    }

    public void setUserUUid(final String userUUid) {
        this.userUUid = userUUid;
    }

    public String getShouldSendEmailAfterRegistration() {
        return shouldSendEmailAfterRegistration;
    }

    public void setShouldSendEmailAfterRegistration(final String shouldSendEmailAfterRegistration) {
        this.shouldSendEmailAfterRegistration = shouldSendEmailAfterRegistration;
    }

    public ProdRegError getProdRegError() {
        return prodRegError;
    }

    public void setProdRegError(final ProdRegError prodRegError) {
        this.prodRegError = prodRegError;
    }
}
