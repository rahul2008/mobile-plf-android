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

    }

    public void testSetLocale() throws Exception {

    }

    public void testGetDateOfPurchase() throws Exception {

    }

    public void testSetDateOfPurchase() throws Exception {

    }

    public void testGetProductRegistrationUuid() throws Exception {

    }

    public void testSetProductRegistrationUuid() throws Exception {

    }

    public void testGetWarrantyEndDate() throws Exception {

    }

    public void testSetWarrantyEndDate() throws Exception {

    }

    public void testGetContractNumber() throws Exception {

    }

    public void testSetContractNumber() throws Exception {

    }

    public void testGetModelNumber() throws Exception {

    }

    public void testSetModelNumber() throws Exception {

    }

    public void testGetEmailStatus() throws Exception {

    }

    public void testSetEmailStatus() throws Exception {

    }

    public void testToString() throws Exception {

    }
}