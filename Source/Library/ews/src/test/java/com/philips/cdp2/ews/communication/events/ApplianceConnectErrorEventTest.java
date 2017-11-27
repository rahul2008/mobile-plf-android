/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.communication.events;

import com.philips.cdp2.ews.annotations.ConnectionErrorType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplianceConnectErrorEventTest {

    private ApplianceConnectErrorEvent subject;

    @ConnectionErrorType  private int type = ConnectionErrorType.WRONG_HOME_WIFI;

    @Before
    public void setUp() throws Exception {
        subject = new ApplianceConnectErrorEvent(type);
    }

    @Test
    public void itShouldVerifyCorrectErrorType() throws Exception {
        assertEquals(subject.getErrorType(), type);
    }

}