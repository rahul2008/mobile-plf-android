package com.philips.cdp.prodreg.model.registerproduct;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegistrationResponseData {
    private String registrationDate;

    private String locale;

    private String dateOfPurchase;

    private String productRegistrationUuid;

    private String warrantyEndDate;

    private String contractNumber;

    private String modelNumber;

    private String emailStatus;

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getProductRegistrationUuid() {
        return productRegistrationUuid;
    }

    public void setProductRegistrationUuid(String productRegistrationUuid) {
        this.productRegistrationUuid = productRegistrationUuid;
    }

    public String getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(String warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    @Override
    public String toString() {
        return "ClassPojo [registrationDate = " + registrationDate + ", locale = " + locale + ", dateOfPurchase = " + dateOfPurchase + ", productRegistrationUuid = " + productRegistrationUuid + ", warrantyEndDate = " + warrantyEndDate + ", contractNumber = " + contractNumber + ", modelNumber = " + modelNumber + ", emailStatus = " + emailStatus + "]";
    }
}
