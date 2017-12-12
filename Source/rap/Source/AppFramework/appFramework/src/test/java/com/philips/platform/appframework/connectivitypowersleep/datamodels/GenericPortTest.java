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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by philips on 8/31/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class GenericPortTest {

    private static final int PRODUCT_ID = 1;

    private static final long SESSION = 12345;

    private static final String NAME = "session";

    private static final String DUMMY_DATA = "{\"tst\":\"2332000000\",\"dst\":\"540000000\",\"nri\":\"5\"}";

    @Mock
    CommunicationStrategy communicationStrategy;

    private GenericPort genericPort;

    @Before
    public void setUp() {
        genericPort = new GenericPort(communicationStrategy, NAME, PRODUCT_ID, SessionDataPortProperties.class);
    }

    @Test
    public void getDICommPortNameTest() {
        assertEquals(genericPort.getDICommPortName(), NAME);
    }

    @Test
    public void getDICommProductIdTest() {
        assertEquals(genericPort.getDICommProductId(), PRODUCT_ID);
    }

    @Test
    public void supportsSubscriptionTest() {
        assertTrue(genericPort.supportsSubscription());
    }


    @After
    public void tearDown() {
        communicationStrategy = null;
        genericPort = null;
    }
}
