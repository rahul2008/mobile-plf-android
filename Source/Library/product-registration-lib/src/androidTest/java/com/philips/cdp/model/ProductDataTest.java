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

    public void testGetMessage() throws Exception {
        productDataTest.setMessage("If, after the purchase date");
        assertEquals("If, after the purchase date", productDataTest.getMessage());
    }

    public void testGetCtn() throws Exception {
        productDataTest.setCtn("HC5410/83");
        assertEquals("HC5410/83", productDataTest.getCtn());
    }

    public void testGetIsLicensekeyRequired() throws Exception {

    }

    public void testGetHasGiftPack() throws Exception {
        productDataTest.setHasGiftPack("true");
        assertEquals("true", productDataTest.getHasGiftPack());
    }

    public void testGetSerialNumberFormat() throws Exception {
        productDataTest.setSerialNumberFormat("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        assertEquals("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$", productDataTest.getSerialNumberFormat());
    }

    public void testGetHasExtendedWarranty() throws Exception {
        productDataTest.setHasExtendedWarranty("true");
        assertEquals("true", productDataTest.getHasExtendedWarranty());
    }

    public void testGetRequiresSerialNumber() throws Exception {
        productDataTest.setRequiresSerialNumber("true");
        assertEquals("true", productDataTest.getRequiresSerialNumber());
    }

    public void testGetIsConnectedDevice() throws Exception {
        productDataTest.setIsConnectedDevice("true");
        assertEquals("true", productDataTest.getIsConnectedDevice());
    }

    public void testGetExtendedWarrantyMonths() throws Exception {
        productDataTest.setExtendedWarrantyMonths("true");
        assertEquals("true", productDataTest.getExtendedWarrantyMonths());
    }

    public void testGetRequiresDateOfPurchase() throws Exception {
        productDataTest.setRequiresDateOfPurchase("true");
        assertEquals("true", productDataTest.getRequiresDateOfPurchase());
    }
}