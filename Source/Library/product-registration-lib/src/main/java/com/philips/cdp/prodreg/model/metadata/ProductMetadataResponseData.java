package com.philips.cdp.prodreg.model.metadata;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataResponseData {
    private String message;

    private String ctn;

    private String isLicensekeyRequired;

    private String hasGiftPack;

    private String serialNumberFormat;

    private String hasExtendedWarranty;

    private String requiresSerialNumber;

    private MetadataSerNumbSampleContent serialNumberSampleContent;

    private String isConnectedDevice;

    private String extendedWarrantyMonths;

    private String requiresDateOfPurchase;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public String getIsLicensekeyRequired() {
        return isLicensekeyRequired;
    }

    public void setIsLicensekeyRequired(String isLicensekeyRequired) {
        this.isLicensekeyRequired = isLicensekeyRequired;
    }

    public String getHasGiftPack() {
        return hasGiftPack;
    }

    public void setHasGiftPack(String hasGiftPack) {
        this.hasGiftPack = hasGiftPack;
    }

    public String getSerialNumberFormat() {
        return serialNumberFormat;
    }

    public void setSerialNumberFormat(String serialNumberFormat) {
        this.serialNumberFormat = serialNumberFormat;
    }

    public String getHasExtendedWarranty() {
        return hasExtendedWarranty;
    }

    public void setHasExtendedWarranty(String hasExtendedWarranty) {
        this.hasExtendedWarranty = hasExtendedWarranty;
    }

    public String getRequiresSerialNumber() {
        return requiresSerialNumber;
    }

    public void setRequiresSerialNumber(String requiresSerialNumber) {
        this.requiresSerialNumber = requiresSerialNumber;
    }

    public MetadataSerNumbSampleContent getSerialNumberSampleContent() {
        return serialNumberSampleContent;
    }

    public void setSerialNumberSampleContent(MetadataSerNumbSampleContent serialNumberSampleContent) {
        this.serialNumberSampleContent = serialNumberSampleContent;
    }

    public String getIsConnectedDevice() {
        return isConnectedDevice;
    }

    public void setIsConnectedDevice(String isConnectedDevice) {
        this.isConnectedDevice = isConnectedDevice;
    }

    public String getExtendedWarrantyMonths() {
        return extendedWarrantyMonths;
    }

    public void setExtendedWarrantyMonths(String extendedWarrantyMonths) {
        this.extendedWarrantyMonths = extendedWarrantyMonths;
    }

    public String getRequiresDateOfPurchase() {
        return requiresDateOfPurchase;
    }

    public void setRequiresDateOfPurchase(String requiresDateOfPurchase) {
        this.requiresDateOfPurchase = requiresDateOfPurchase;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", ctn = " + ctn + ", isLicensekeyRequired = " + isLicensekeyRequired + ", hasGiftPack = " + hasGiftPack + ", serialNumberFormat = " + serialNumberFormat + ", hasExtendedWarranty = " + hasExtendedWarranty + ", requiresSerialNumber = " + requiresSerialNumber + ", serialNumberSampleContent = " + serialNumberSampleContent + ", isConnectedDevice = " + isConnectedDevice + ", extendedWarrantyMonths = " + extendedWarrantyMonths + ", requiresDateOfPurchase = " + requiresDateOfPurchase + "]";
    }
}
