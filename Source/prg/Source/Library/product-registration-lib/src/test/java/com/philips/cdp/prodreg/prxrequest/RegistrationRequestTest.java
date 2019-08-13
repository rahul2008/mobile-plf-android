package com.philips.cdp.prodreg.prxrequest;


import com.philips.cdp.prxclient.PrxConstants;

import junit.framework.TestCase;

import org.mockito.Mock;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegistrationRequestTest extends TestCase {

    RegistrationRequest registrationRequest;
    PrxConstants.Sector sector;
    PrxConstants.Catalog catalog;
    @Mock
    String mCtn = "HD8967/01", mSerialNumber = "1344", mAccessToken = "abq21238xbshs";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        sector = PrxConstants.Sector.B2C;
        catalog = PrxConstants.Catalog.CONSUMER;
        registrationRequest = new RegistrationRequest(mCtn, mSerialNumber,sector,catalog,true);
    }

    public void testGetServerInfo() throws Exception {

    }

    public void testGetProductSerialNumber() throws Exception {
        registrationRequest.setProductSerialNumber("1344");
        assertEquals("1344", registrationRequest.getProductSerialNumber());
    }

    public void testGetPurchaseDate() throws Exception {
        registrationRequest.setPurchaseDate("23-03-2016");
        assertEquals("23-03-2016", registrationRequest.getPurchaseDate());
    }

    public void testGetRegistrationChannel() throws Exception {
        registrationRequest.setRegistrationChannel("qed111");
        assertEquals("qed111", registrationRequest.getRegistrationChannel());
    }

    public void testGetSendEmail() throws Exception {
        registrationRequest.setSendEmail("test@test.com");
        assertEquals("test@test.com", registrationRequest.getSendEmail());
    }

    public void testGetAddress1() throws Exception {
        registrationRequest.setAddress1("test address");
        assertEquals("test address", registrationRequest.getAddress1());
    }

    public void testGetAddress2() throws Exception {
        registrationRequest.setAddress2("test address2");
        assertEquals("test address2", registrationRequest.getAddress2());
    }

    public void testGetAddress3() throws Exception {
        registrationRequest.setAddress3("test address3");
        assertEquals("test address3", registrationRequest.getAddress3());
    }

    public void testGetCity() throws Exception {
        registrationRequest.setCity("bangalore");
        assertEquals("bangalore", registrationRequest.getCity());
    }

    public void testGetZip() throws Exception {
        registrationRequest.setZip("12345");
        assertEquals("12345", registrationRequest.getZip());
    }

    public void testGetState() throws Exception {
        registrationRequest.setState("karanatak");
        assertEquals("karanatak", registrationRequest.getState());
    }

    public void testGetCountry() throws Exception {
        registrationRequest.setCountry("india");
        assertEquals("india", registrationRequest.getCountry());
    }

    public void testReceiveMarketingEmail() throws Exception {
        registrationRequest.setReceiveMarketEmail(true);
        assertEquals("true", registrationRequest.isReceiveMarketingEmail());
    }
}