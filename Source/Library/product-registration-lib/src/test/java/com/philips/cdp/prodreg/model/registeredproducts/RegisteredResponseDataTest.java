package com.philips.cdp.prodreg.model.registeredproducts;

import junit.framework.TestCase;

import org.junit.Test;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegisteredResponseDataTest extends TestCase {

    RegisteredResponseData resultsTest;

    @Override
    public void setUp() throws Exception {
        resultsTest = new RegisteredResponseData();
    }

    public void testSetLastModified() throws Exception {
        resultsTest.setLastModified("2013-12-03");
    }

    public void testSetLastSolicitDate() throws Exception {
        resultsTest.setLastSolicitDate(null);
    }

    public void testSetWarrantyInMonths() throws Exception {
        resultsTest.setWarrantyInMonths(null);
    }

    public void testSetLastUpdated() throws Exception {
        resultsTest.setLastUpdated("2014-02-25 21:31:36.161304 +0000");
    }

    public void testSetExtendedWarranty() throws Exception {
        resultsTest.setExtendedWarranty("false");
    }

    public void testSetProductCatalogLocaleId() throws Exception {
        resultsTest.setProductCatalogLocaleId("nl_NL_CONSUMER");
    }

    public void testSetProductModelNumber() throws Exception {
        resultsTest.setProductModelNumber("HX8002/05");
    }

    public void testSetProductId() throws Exception {
        resultsTest.setProductId("HX8002_05_NL_CONSUMER");
    }

    public void testSetPurchasePlace() throws Exception {
        resultsTest.setPurchasePlace(null);
    }

    public void testSetId() throws Exception {
        resultsTest.setId("139136402");
    }

    public void testSetRegistrationDate() throws Exception {
        resultsTest.setRegistrationDate("2013-12-03 00:00:00 +0000");
    }

    public void testSetDeviceName() throws Exception {
        resultsTest.setDeviceName("HX8002/05");
    }

    public void testSetCreated() throws Exception {
        resultsTest.setCreated("2014-02-25 21:31:36.161304 +0000");
    }

    @Test
    public void testSetProductRegistrationID() throws Exception {
        resultsTest.setProductRegistrationID("2512000064");
    }

    public void testSetSlashWinCompetition() throws Exception {
        resultsTest.setSlashWinCompetition("false");
    }

    public void testSetPurchaseDate() throws Exception {
        resultsTest.setPurchaseDate("2013-03-01");
    }

    public void testSetIsPrimaryUser() throws Exception {
        resultsTest.setIsPrimaryUser("true");
    }

    public void testSetIsGenerations() throws Exception {

        resultsTest.setIsGenerations("false");
    }

    public void testSetUuid() throws Exception {
        resultsTest.setUuid("973bd103-6c38-4899-8716-aade4f632cb6");
    }

    public void testSetRegistrationChannel() throws Exception {
        resultsTest.setRegistrationChannel("web");
    }

    public void testSetProductSerialNumber() throws Exception {

        resultsTest.setProductSerialNumber(null);
    }

    public void testSetContractNumber() throws Exception {
        resultsTest.setContractNumber(null);
    }

    public void testSetDeviceId() throws Exception {
        resultsTest.setDeviceId(null);
    }

    @Test
    public void ShouldGetLastModified() throws Exception {
        resultsTest.setLastModified("2013-12-03");
        assertEquals("2013-12-03", resultsTest.getLastModified());
    }

    @Test
    public void ShouldGetLastSolicitDate() throws Exception {
        resultsTest.setLastSolicitDate(null);
        assertEquals(null, resultsTest.getLastSolicitDate());
    }

    @Test
    public void ShouldGetWarrantyInMonths() throws Exception {
        resultsTest.setWarrantyInMonths(null);
        assertEquals(null, resultsTest.getWarrantyInMonths());
    }

    @Test
    public void ShouldGetLastUpdated() throws Exception {
        resultsTest.setLastUpdated("2014-02-25 21:31:36.161304 +0000");
        assertEquals("2014-02-25 21:31:36.161304 +0000", resultsTest.getLastUpdated());
    }

    @Test
    public void ShouldGetExtendedWarranty() throws Exception {
        resultsTest.setExtendedWarranty("false");
        assertEquals("false", resultsTest.getExtendedWarranty());
    }

    @Test
    public void ShouldGetProductCatalogLocaleId() throws Exception {
        resultsTest.setProductCatalogLocaleId("nl_NL_CONSUMER");
        assertEquals("nl_NL_CONSUMER", resultsTest.getProductCatalogLocaleId());
    }

    @Test
    public void ShouldGetProductModelNumber() throws Exception {
        resultsTest.setProductModelNumber("HX8002/05");
        assertEquals("HX8002/05", resultsTest.getProductModelNumber());
    }

    @Test
    public void ShouldGetProductId() throws Exception {
        resultsTest.setProductId("HX8002_05_NL_CONSUMER");
        assertEquals("HX8002_05_NL_CONSUMER", resultsTest.getProductId());
    }

    @Test
    public void ShouldGetPurchasePlace() throws Exception {
        resultsTest.setPurchasePlace(null);
        assertEquals(null, resultsTest.getPurchasePlace());
    }

    @Test
    public void ShouldGetId() throws Exception {
        resultsTest.setId("139136402");
        assertEquals("139136402", resultsTest.getId());
    }

    @Test
    public void ShouldGetRegistrationDate() throws Exception {
        resultsTest.setRegistrationDate("2013-12-03 00:00:00 +0000");
        assertEquals("2013-12-03 00:00:00 +0000", resultsTest.getRegistrationDate());
    }

    @Test
    public void ShouldGetDeviceName() throws Exception {
        resultsTest.setDeviceName("HX8002/05");
        assertEquals("HX8002/05", resultsTest.getDeviceName());
    }

    @Test
    public void ShouldGetCreated() throws Exception {
        resultsTest.setCreated("2014-02-25 21:31:36.161304 +0000");
        assertEquals("2014-02-25 21:31:36.161304 +0000", resultsTest.getCreated());
    }

    @Test
    public void ShouldGetProductRegistrationID() throws Exception {
        resultsTest.setProductRegistrationID("2512000064");
        assertEquals("2512000064", resultsTest.getProductRegistrationID());
    }

    @Test
    public void ShouldGetSlashWinCompetition() throws Exception {
        resultsTest.setSlashWinCompetition("false");
        assertEquals("false", resultsTest.getSlashWinCompetition());
    }

    @Test
    public void ShouldGetPurchaseDate() throws Exception {
        resultsTest.setPurchaseDate("2013-03-01");
        assertEquals("2013-03-01", resultsTest.getPurchaseDate());
    }

    @Test
    public void ShouldGetIsPrimaryUser() throws Exception {
        resultsTest.setIsPrimaryUser("true");
        assertEquals("true", resultsTest.getIsPrimaryUser());
    }

    @Test
    public void ShouldGetIsGenerations() throws Exception {

        resultsTest.setIsGenerations("false");
        assertEquals("false", resultsTest.getIsGenerations());
    }

    @Test
    public void ShouldGetUuid() throws Exception {
        resultsTest.setUuid("973bd103-6c38-4899-8716-aade4f632cb6");
        assertEquals("973bd103-6c38-4899-8716-aade4f632cb6", resultsTest.getUuid());
    }

    @Test
    public void ShouldGetRegistrationChannel() throws Exception {
        resultsTest.setRegistrationChannel("web");
        assertEquals("web", resultsTest.getRegistrationChannel());
    }

    @Test
    public void ShouldGetProductSerialNumber() throws Exception {

        resultsTest.setProductSerialNumber(null);
        assertEquals(null, resultsTest.getProductSerialNumber());
    }

    @Test
    public void ShouldGetContractNumber() throws Exception {
        resultsTest.setContractNumber(null);
        assertEquals(null, resultsTest.getContractNumber());
    }

    @Test
    public void ShouldGetDeviceId() throws Exception {
        resultsTest.setDeviceId(null);
        assertEquals(null, resultsTest.getDeviceId());
    }
}