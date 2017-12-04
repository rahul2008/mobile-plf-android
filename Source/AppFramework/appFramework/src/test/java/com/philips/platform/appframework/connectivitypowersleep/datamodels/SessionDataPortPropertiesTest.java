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

import static junit.framework.Assert.assertTrue;

/**
 * Created by philips on 8/31/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SessionDataPortPropertiesTest {

    private static final int PRODUCT_ID = 1;

    private static final String NAME = "session";

    private static final String DUMMY_DATA = "{\"tst\":\"233200000\",\"dst\":\"54000000\",\"nri\":\"5\"}";

    @Mock
    CommunicationStrategy communicationStrategy;

    private SessionDataPort sessionDataPort;

    private SessionDataPortProperties sessionDataPortProperties;

    @Before
    public void setUp() {
        sessionDataPort = new SessionDataPort(communicationStrategy, NAME, PRODUCT_ID, SessionDataPortProperties.class);
        sessionDataPort.processResponse(DUMMY_DATA);
        sessionDataPortProperties = (SessionDataPortProperties) sessionDataPort.getPortProperties();
    }

    @Test
    public void getTotalSleepTimeTest() {
        assertTrue(sessionDataPortProperties.getTotalSleepTime() == 233200000);
    }
    @Test
    public void getNumberOfInterruptionsTest() {
        assertTrue(sessionDataPortProperties.getNumberOfInterruptions() == 5);
    }
    @Test
    public void getDeepSleepTimeTest() {
        assertTrue(sessionDataPortProperties.getDeepSleepTime() == 54000000);
    }

    @After
    public void tearDown() {
        communicationStrategy = null;
        sessionDataPort = null;
    }
}
