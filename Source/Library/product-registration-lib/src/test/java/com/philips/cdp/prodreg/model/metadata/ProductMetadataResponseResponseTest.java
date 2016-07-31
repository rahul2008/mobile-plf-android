package com.philips.cdp.prodreg.model.metadata;

import junit.framework.TestCase;

import org.junit.Test;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataResponseResponseTest extends TestCase {

    ProductMetadataResponseData productDataTest;

    @Override
    public void setUp() throws Exception {
        productDataTest = new ProductMetadataResponseData();
    }

    public void testSetMessage() throws Exception {
        productDataTest.setMessage("If, after the purchase date");
    }

    public void testSetGetCtn() throws Exception {
        productDataTest.setCtn("HC5410/83");
    }

    public void testSetHasGiftPack() throws Exception {
        productDataTest.setHasGiftPack("true");
    }

    public void testSetSerialNumberFormat() throws Exception {
        productDataTest.setSerialNumberFormat("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
    }

    public void testSetHasExtendedWarranty() throws Exception {
        productDataTest.setHasExtendedWarranty("true");
    }

    public void testSetRequiresSerialNumber() throws Exception {
        productDataTest.setRequiresSerialNumber("true");
    }

    public void testSetIsConnectedDevice() throws Exception {
        productDataTest.setIsConnectedDevice("true");
    }

    public void testSetExtendedWarrantyMonths() throws Exception {
        productDataTest.setExtendedWarrantyMonths("true");
    }

    public void testSetRequiresDateOfPurchase() throws Exception {
        productDataTest.setRequiresDateOfPurchase("true");
    }

    @Test
    public void ShouldGetMessage() throws Exception {
        productDataTest.setMessage("If, after the purchase date");
        assertEquals("If, after the purchase date", productDataTest.getMessage());
    }

    @Test
    public void ShouldGetCtn() throws Exception {
        productDataTest.setCtn("HC5410/83");
        assertEquals("HC5410/83", productDataTest.getCtn());
    }

    @Test
    public void ShouldGetHasGiftPack() throws Exception {
        productDataTest.setHasGiftPack("true");
        assertEquals("true", productDataTest.getHasGiftPack());
    }

    @Test
    public void ShouldGetSerialNumberFormat() throws Exception {
        productDataTest.setSerialNumberFormat("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        assertEquals("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$", productDataTest.getSerialNumberFormat());
    }

    @Test
    public void ShouldGetHasExtendedWarranty() throws Exception {
        productDataTest.setHasExtendedWarranty("true");
        assertEquals("true", productDataTest.getHasExtendedWarranty());
    }

    @Test
    public void ShouldGetRequiresSerialNumber() throws Exception {
        productDataTest.setRequiresSerialNumber("true");
        assertEquals("true", productDataTest.getRequiresSerialNumber());
    }

    @Test
    public void ShouldGetIsConnectedDevice() throws Exception {
        productDataTest.setIsConnectedDevice("true");
        assertEquals("true", productDataTest.getIsConnectedDevice());
    }

    @Test
    public void ShouldGetExtendedWarrantyMonths() throws Exception {
        productDataTest.setExtendedWarrantyMonths("true");
        assertEquals("true", productDataTest.getExtendedWarrantyMonths());
    }

    @Test
    public void ShouldGetRequiresDateOfPurchase() throws Exception {
        productDataTest.setRequiresDateOfPurchase("true");
        assertEquals("true", productDataTest.getRequiresDateOfPurchase());
    }
}