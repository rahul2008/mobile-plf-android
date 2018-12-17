package com.philips.cdp.prodreg.model.registerproduct;

import junit.framework.TestCase;

import org.junit.Test;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegistrationResponseDataTest extends TestCase {

    RegistrationResponseData dataTest;

    @Override
    public void setUp() throws Exception {
        dataTest = new RegistrationResponseData();
    }

    public void testSetRegistrationDate() throws Exception {
        dataTest.setRegistrationDate("28-02-2016");
    }

    public void testSetLocale() throws Exception {
        dataTest.setLocale("en_GB");
    }

    public void testSetDateOfPurchase() throws Exception {
        dataTest.setDateOfPurchase("2015-02-25");
    }

    public void testSetProductRegistrationUuid() throws Exception {
        dataTest.setProductRegistrationUuid("eb26c6d8-693f-4ec0-be60-2c603eaad8a3");
    }

    public void testSetWarrantyEndDate() throws Exception {
        dataTest.setWarrantyEndDate("2019-06-12");
    }

    public void testSetContractNumber() throws Exception {
        dataTest.setContractNumber("CQ5A000ef");
    }

    public void testSetModelNumber() throws Exception {
        dataTest.setModelNumber("HC5450/83");
    }

    public void testSetEmailStatus() throws Exception {
        dataTest.setEmailStatus("success");
    }

    @Test
    public void ShouldGetRegistrationDateWhenAsked() throws Exception {
        dataTest.setRegistrationDate("28-02-2016");
        assertEquals("28-02-2016", dataTest.getRegistrationDate());
    }

    @Test
    public void ShouldGetLocale() throws Exception {
        dataTest.setLocale("en_GB");
        assertEquals("en_GB", dataTest.getLocale());
    }

    @Test
    public void ShouldGetDateOfPurchase() throws Exception {
        dataTest.setDateOfPurchase("2015-02-25");
        assertEquals("2015-02-25", dataTest.getDateOfPurchase());
    }

    @Test
    public void ShouldGetProductRegistrationUuid() throws Exception {
        dataTest.setProductRegistrationUuid("eb26c6d8-693f-4ec0-be60-2c603eaad8a3");
        assertEquals("eb26c6d8-693f-4ec0-be60-2c603eaad8a3", dataTest.getProductRegistrationUuid());
    }

    @Test
    public void ShouldGetWarrantyEndDate() throws Exception {
        dataTest.setWarrantyEndDate("2019-06-12");
        assertEquals("2019-06-12", dataTest.getWarrantyEndDate());
    }

    @Test
    public void ShouldGetContractNumber() throws Exception {
        dataTest.setContractNumber("CQ5A000ef");
        assertEquals("CQ5A000ef", dataTest.getContractNumber());
    }

    @Test
    public void ShouldGetModelNumber() throws Exception {
        dataTest.setModelNumber("HC5450/83");
        assertEquals("HC5450/83", dataTest.getModelNumber());
    }

    @Test
    public void ShouldGetEmailStatus() throws Exception {
        dataTest.setEmailStatus("success");
        assertEquals("success", dataTest.getEmailStatus());
    }
}