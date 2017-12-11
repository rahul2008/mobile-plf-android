/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by philips on 8/31/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SessionDataPortTest {

    private static final int PRODUCT_ID = 1;

    private static final long SESSION = 12345;

    private static final String NAME = "session/%s";

    private static final String DUMMY_DATA = "{\"tst\":\"2332000000\",\"dst\":\"540000000\",\"nri\":\"5\"}";

    @Mock
    CommunicationStrategy communicationStrategy;

    private SessionDataPort sessionDataPort;

    @Before
    public void setUp() {
        sessionDataPort = new SessionDataPort(communicationStrategy, NAME, PRODUCT_ID, SessionDataPortProperties.class);
        sessionDataPort.setSpecificSession(SESSION);
    }

    @Test
    public void getDICommPortNameTest() {
        assertEquals(sessionDataPort.getDICommPortName(), "session/12345");
    }

    @Test
    public void getDICommProductIdTest() {
        assertEquals(sessionDataPort.getDICommProductId(), PRODUCT_ID);
    }

    @Test
    public void supportsSubscriptionTest() {
        assertTrue(sessionDataPort.supportsSubscription());
    }

    @After
    public void tearDown() {
        communicationStrategy = null;
        sessionDataPort = null;
    }
}
