package com.philips.cdp.model;

import junit.framework.TestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SerialNumberSampleContentTest extends TestCase {

    SerialNumberSampleContent serialNumberSampleContentTest;

    @Override
    public void setUp() throws Exception {
        serialNumberSampleContentTest = new SerialNumberSampleContent();
    }

    public void testGetTitle() throws Exception {
        serialNumberSampleContentTest.setTitle("Find the serial number");
        assertEquals("Find the serial number", serialNumberSampleContentTest.getTitle());
    }

    public void testSetTitle() throws Exception {

    }

    public void testGetAsset() throws Exception {

        serialNumberSampleContentTest.setAsset("/consumerfiles/assets/img/registerproducts/HC.jpg");
        assertEquals("/consumerfiles/assets/img/registerproducts/HC.jpg", serialNumberSampleContentTest.getAsset());
    }

    public void testSetAsset() throws Exception {

    }

    public void testGetSnExample() throws Exception {
        serialNumberSampleContentTest.setSnExample("Example: 1344");
        assertEquals("Example: 1344", serialNumberSampleContentTest.getSnExample());
    }

    public void testSetSnExample() throws Exception {

    }

    public void testGetSnFormat() throws Exception {
        serialNumberSampleContentTest.setSnFormat("cc");
        assertEquals("cc", serialNumberSampleContentTest.getSnFormat());
    }

    public void testSetSnFormat() throws Exception {

    }

    public void testToString() throws Exception {

    }
}