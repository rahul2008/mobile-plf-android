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
    public void ShouldGetRegistrationDateWhenAsked() throws Exception {
        dataTest.setRegistrationDate("28-02-2016");
        assertEquals("28-02-2016", dataTest.getRegistrationDate());
    }

    public void ShouldGetLocale() throws Exception {
        dataTest.setLocale("en_GB");
        assertEquals("en_GB", dataTest.getLocale());
    }

    public void ShouldGetDateOfPurchase() throws Exception {
        dataTest.setDateOfPurchase("2015-02-25");
        assertEquals("2015-02-25", dataTest.getDateOfPurchase());
    }

    public void ShouldGetProductRegistrationUuid() throws Exception {
        dataTest.setProductRegistrationUuid("eb26c6d8-693f-4ec0-be60-2c603eaad8a3");
        assertEquals("eb26c6d8-693f-4ec0-be60-2c603eaad8a3", dataTest.getProductRegistrationUuid());
    }

    public void ShouldGetWarrantyEndDate() throws Exception {
        dataTest.setWarrantyEndDate("2019-06-12");
        assertEquals("2019-06-12", dataTest.getWarrantyEndDate());
    }

    public void ShouldGetContractNumber() throws Exception {
        dataTest.setContractNumber("CQ5A000ef");
        assertEquals("CQ5A000ef", dataTest.getContractNumber());
    }

    public void ShouldGetModelNumber() throws Exception {
        dataTest.setModelNumber("HC5450/83");
        assertEquals("HC5450/83", dataTest.getModelNumber());
    }

    public void ShouldGetEmailStatus() throws Exception {
        dataTest.setEmailStatus("succes s");
        assertEquals("success", dataTest.getEmailStatus());
    }
}