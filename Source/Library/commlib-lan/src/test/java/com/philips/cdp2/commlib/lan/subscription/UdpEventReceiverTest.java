/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UdpEventReceiverTest extends RobolectricTest {

    private UdpEventReceiver receiver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        receiver = UdpEventReceiver.getInstance();
    }

    @Test
    public void givenSingletonAvailable_whenSingletonCalled_thenShouldNotBeNull() {
        // Boyscout rule FTW
        assertNotNull(receiver);
    }
}
