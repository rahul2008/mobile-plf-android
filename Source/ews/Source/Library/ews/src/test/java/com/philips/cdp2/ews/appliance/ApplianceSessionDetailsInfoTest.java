/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.appliance;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class ApplianceSessionDetailsInfoTest {

    private ApplianceSessionDetailsInfo subject;

    private static final String CPPID = "be12345678";
    private static final String APPLIANCEPIN  = "pin12345678";

    @Before
    public void setup() {
        subject = new ApplianceSessionDetailsInfo();
        subject.setCppId(CPPID);
        subject.setAppliancePin(APPLIANCEPIN);
    }

    @Test
    public void itShouldVerifyCppId() {
        assertEquals(CPPID, subject.getCppId());
    }

    @Test
    public void itShouldVerifyAppliancePin() {
        assertEquals(APPLIANCEPIN, subject.getAppliancePin());
    }

    @Test
    public void itShouldClearAppliancePinWhenClearIsCalled() {
        subject.clear();
        assertNull(subject.getAppliancePin());
    }

}