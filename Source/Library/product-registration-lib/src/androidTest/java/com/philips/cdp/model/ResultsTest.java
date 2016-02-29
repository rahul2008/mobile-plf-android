package com.philips.cdp.model;

import junit.framework.TestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ResultsTest extends TestCase {

    Results resultsTest;

    @Override
    public void setUp() throws Exception {
        resultsTest = new Results();
    }

    public void testGetLastModified() throws Exception {
        resultsTest.setLastModified("2013-12-03");
        assertEquals("2013-12-03", resultsTest.getLastModified());
    }

    public void testGetLastSolicitDate() throws Exception {
        resultsTest.setLastSolicitDate(null);
        assertEquals(null, resultsTest.getLastSolicitDate());
    }

    public void testGetWarrantyInMonths() throws Exception {
        resultsTest.setWarrantyInMonths(null);
        assertEquals(null, resultsTest.getWarrantyInMonths());
    }

    public void testGetLastUpdated() throws Exception {
        resultsTest.setLastUpdated("2014-02-25 21:31:36.161304 +0000");
        assertEquals("2014-02-25 21:31:36.161304 +0000", resultsTest.getLastUpdated());
    }

    public void testGetExtendedWarranty() throws Exception {
        resultsTest.setExtendedWarranty("false");
        assertEquals("false", resultsTest.getExtendedWarranty());
    }

    public void testGetProductCatalogLocaleId() throws Exception {
        resultsTest.setProductCatalogLocaleId("nl_NL_CONSUMER");
        assertEquals("nl_NL_CONSUMER", resultsTest.getProductCatalogLocaleId());
    }

    public void testGetProductModelNumber() throws Exception {
        resultsTest.setProductModelNumber("HX8002/05");
        assertEquals("HX8002/05", resultsTest.getProductModelNumber());
    }

    public void testGetProductId() throws Exception {
        resultsTest.setProductId("HX8002_05_NL_CONSUMER");
        assertEquals("HX8002_05_NL_CONSUMER", resultsTest.getProductId());
    }

    public void testGetPurchasePlace() throws Exception {
        resultsTest.setPurchasePlace(null);
        assertEquals(null, resultsTest.getPurchasePlace());
    }

    public void testGetId() throws Exception {
        resultsTest.setId("139136402");
        assertEquals("139136402", resultsTest.getId());
    }

    public void testGetRegistrationDate() throws Exception {
        resultsTest.setRegistrationDate("2013-12-03 00:00:00 +0000");
        assertEquals("2013-12-03 00:00:00 +0000", resultsTest.getRegistrationDate());
    }

    public void testGetDeviceName() throws Exception {
        resultsTest.setDeviceName("HX8002/05");
        assertEquals("HX8002/05", resultsTest.getDeviceName());
    }

    public void testGetCreated() throws Exception {
        resultsTest.setCreated("2014-02-25 21:31:36.161304 +0000");
        assertEquals("2014-02-25 21:31:36.161304 +0000", resultsTest.getCreated());
    }

    public void testGetProductRegistrationID() throws Exception {
        resultsTest.setProductRegistrationID("2512000064");
        assertEquals("2512000064", resultsTest.getProductRegistrationID());
    }

    public void testGetSlashWinCompetition() throws Exception {
        resultsTest.setSlashWinCompetition("false");
        assertEquals("false", resultsTest.getSlashWinCompetition());
    }

    public void testGetPurchaseDate() throws Exception {
        resultsTest.setPurchaseDate("2013-03-01");
        assertEquals("2013-03-01", resultsTest.getPurchaseDate());
    }

    public void testGetIsPrimaryUser() throws Exception {
        resultsTest.setIsPrimaryUser("true");
        assertEquals("true", resultsTest.getIsPrimaryUser());
    }

    public void testGetIsGenerations() throws Exception {

        resultsTest.setIsGenerations("false");
        assertEquals("false", resultsTest.getIsGenerations());
    }

    public void testGetUuid() throws Exception {
        resultsTest.setUuid("973bd103-6c38-4899-8716-aade4f632cb6");
        assertEquals("973bd103-6c38-4899-8716-aade4f632cb6", resultsTest.getUuid());
    }

    public void testGetRegistrationChannel() throws Exception {
        resultsTest.setRegistrationChannel("web");
        assertEquals("web", resultsTest.getRegistrationChannel());
    }

    public void testGetProductSerialNumber() throws Exception {

        resultsTest.setProductSerialNumber(null);
        assertEquals(null, resultsTest.getProductSerialNumber());
    }

    public void testGetContractNumber() throws Exception {
        resultsTest.setContractNumber(null);
        assertEquals(null, resultsTest.getContractNumber());
    }

    public void testGetDeviceId() throws Exception {
        resultsTest.setDeviceId(null);
        assertEquals(null, resultsTest.getDeviceId());
    }
}