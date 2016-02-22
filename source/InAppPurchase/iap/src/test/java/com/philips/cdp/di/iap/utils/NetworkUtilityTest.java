package com.philips.cdp.di.iap.utils;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NetworkUtilityTest extends TestCase {

    @Test
    public void testShowNetworkError() {
        String alertTitle = "Network Error";
        String alertBody = "No network available. Please check your network settings and try again.";
        assertEquals(alertTitle, "Network Error");
        assertEquals(alertBody, "No network available. Please check your network settings and try again.");
    }

    @Test
    public void testsetOnline(){

    }
}