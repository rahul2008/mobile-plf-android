package com.philips.cdp.model;

import junit.framework.TestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductDataTest extends TestCase {

    ProductData productDataTest;

    @Override
    public void setUp() throws Exception {
        productDataTest = new ProductData();
    }

    public void ShouldGetMessage() throws Exception {
        productDataTest.setMessage("If, after the purchase date");
        assertEquals("If, after the purchase date", productDataTest.getMessage());
    }

    public void ShouldGetCtn() throws Exception {
        productDataTest.setCtn("HC5410/83");
        assertEquals("HC5410/83", productDataTest.getCtn());
    }

    public void ShouldGetHasGiftPack() throws Exception {
        productDataTest.setHasGiftPack("true");
        assertEquals("true", productDataTest.getHasGiftPack());
    }

    public void ShouldGetSerialNumberFormat() throws Exception {
        productDataTest.setSerialNumberFormat("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        assertEquals("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$", productDataTest.getSerialNumberFormat());
    }

    public void ShouldGetHasExtendedWarranty() throws Exception {
        productDataTest.setHasExtendedWarranty("true");
        assertEquals("true", productDataTest.getHasExtendedWarranty());
    }

    public void ShouldGetRequiresSerialNumber() throws Exception {
        productDataTest.setRequiresSerialNumber("true");
        assertEquals("true", productDataTest.getRequiresSerialNumber());
    }

    public void ShouldGetIsConnectedDevice() throws Exception {
        productDataTest.setIsConnectedDevice("true");
        assertEquals("true", productDataTest.getIsConnectedDevice());
    }

    public void ShouldGetExtendedWarrantyMonths() throws Exception {
        productDataTest.setExtendedWarrantyMonths("true");
        assertEquals("true", productDataTest.getExtendedWarrantyMonths());
    }

    public void ShouldGetRequiresDateOfPurchase() throws Exception {
        productDataTest.setRequiresDateOfPurchase("true");
        assertEquals("true", productDataTest.getRequiresDateOfPurchase());
    }
}