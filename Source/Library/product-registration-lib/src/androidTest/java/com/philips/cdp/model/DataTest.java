package com.philips.cdp.model;

import android.test.InstrumentationTestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DataTest extends InstrumentationTestCase {

    Data dataTest;

    @Override
    public void setUp() throws Exception {
        dataTest = new Data();
    }

    @Test
    public void testGetRegistrationDate() throws Exception {
        dataTest.setRegistrationDate("28-02-2016");
        assertEquals("28-02-2016", dataTest.getRegistrationDate());
    }

    public void testSetRegistrationDate() throws Exception {

    }

    public void testGetLocale() throws Exception {
        dataTest.setLocale("en_GB");
        assertEquals("en_GB", dataTest.getLocale());

    }

    public void testSetLocale() throws Exception {

    }

    public void testGetDateOfPurchase() throws Exception {
        dataTest.setDateOfPurchase("2015-02-25");
        assertEquals("2015-02-25", dataTest.getDateOfPurchase());

    }

    public void testSetDateOfPurchase() throws Exception {

    }

    public void testGetProductRegistrationUuid() throws Exception {
        dataTest.setProductRegistrationUuid("eb26c6d8-693f-4ec0-be60-2c603eaad8a3");
        assertEquals("eb26c6d8-693f-4ec0-be60-2c603eaad8a3", dataTest.getProductRegistrationUuid());

    }

    public void testSetProductRegistrationUuid() throws Exception {

    }

    public void testGetWarrantyEndDate() throws Exception {
        dataTest.setWarrantyEndDate("2019-06-12");
        assertEquals("2019-06-12", dataTest.getWarrantyEndDate());

    }

    public void testSetWarrantyEndDate() throws Exception {

    }

    public void testGetContractNumber() throws Exception {
        dataTest.setContractNumber("CQ5A000ef");
        assertEquals("CQ5A000ef", dataTest.getContractNumber());

    }

    public void testSetContractNumber() throws Exception {

    }

    public void testGetModelNumber() throws Exception {
        dataTest.setModelNumber("HC5450/83");
        assertEquals("HC5450/83", dataTest.getModelNumber());

    }

    public void testSetModelNumber() throws Exception {

    }

    public void testGetEmailStatus() throws Exception {
        dataTest.setEmailStatus("succes s");
        assertEquals("success", dataTest.getEmailStatus());

    }

    public void testSetEmailStatus() throws Exception {

    }

    public void testToString() throws Exception {

    }
}