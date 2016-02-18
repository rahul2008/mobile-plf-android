package com.philips.cdp.model;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductData {
    private String isLicensekeyRequired;

    private String hasGiftPack;

    private String hasExtendedWarranty;

    private String requiresSerialNumber;

    private String isConnectedDevice;

    private String extendedWarrantyMonths;

    private String requiresDateOfPurchase;

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
        return "ClassPojo [isLicensekeyRequired = " + isLicensekeyRequired + ", hasGiftPack = " + hasGiftPack + ", hasExtendedWarranty = " + hasExtendedWarranty + ", requiresSerialNumber = " + requiresSerialNumber + ", isConnectedDevice = " + isConnectedDevice + ", extendedWarrantyMonths = " + extendedWarrantyMonths + ", requiresDateOfPurchase = " + requiresDateOfPurchase + "]";
    }
}
