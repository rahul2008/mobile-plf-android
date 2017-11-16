/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StartConnectionModelTest {

    public StartConnectionModel subject;

    @Before
    public void setUp() throws Exception {
        subject = new StartConnectionModel("HomeSSIDTest", "HomePasswordTest", "DeviceNameTest", "DeviceFriendlyNameTest");
    }

    @Test
    public void itShouldVerifyGetHomeWiFiSSID() throws Exception {
        assertEquals(subject.getHomeWiFiSSID(), "HomeSSIDTest");
    }

    @Test
    public void itShouldVerifyGetHomeWiFiPassword() throws Exception {
        assertEquals(subject.getHomeWiFiPassword(), "HomePasswordTest");
    }

    @Test
    public void itShouldVerifyGetDeviceName() throws Exception {
        assertEquals(subject.getDeviceName(), "DeviceNameTest");
    }

    @Test
    public void itShouldVerifyGetDeviceFriendlyName() throws Exception {
        assertEquals(subject.getDeviceFriendlyName(), "DeviceFriendlyNameTest");
    }

    @Test
    public void itShouldGiveTrueOnVerifyEquals()throws Exception {
        StartConnectionModel startConnectionModel = new StartConnectionModel("HomeSSIDTest", "HomePasswordTest", "DeviceNameTest", "DeviceFriendlyNameTest");
        assertEquals(true, subject.equals(startConnectionModel));
    }

    @Test
    public void itShouldGiveFalseOnVerifyEquals()throws Exception {
        StartConnectionModel startConnectionModel = new StartConnectionModel("HomeSSIDTest1", "HomePasswordTest", "DeviceNameTest", "DeviceFriendlyNameTest");
        assertEquals(false, subject.equals(startConnectionModel));
    }

    @Test
    public void itShouldGiveFalseOnVerifyEqualsWithNull()throws Exception {
        assertEquals(false, subject.equals(null));
    }

    @Test
    public void itShouldVerifyHashCode()throws Exception {
        int hash = "HomeSSIDTest".hashCode();
        hash = 31 * hash + "HomePasswordTest".hashCode();
        hash = 31 * hash + "DeviceNameTest".hashCode();
        hash = 31 * hash + "DeviceFriendlyNameTest".hashCode();
        assertEquals(subject.hashCode(),hash);
    }
}