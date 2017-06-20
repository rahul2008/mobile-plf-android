/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import org.junit.Test;

import static com.philips.cdp.dicommclient.port.common.SecurityPortProperties.DIFFIE;
import static com.philips.cdp.dicommclient.port.common.SecurityPortProperties.HELLMAN;
import static com.philips.cdp.dicommclient.port.common.SecurityPortProperties.KEY;
import static junit.framework.Assert.fail;

public class SecurityPortPropertiesTest {

    @Test
    public void mustHaveFieldsWithNamesAccordingToSpec() {
        try {
            SecurityPortProperties.class.getDeclaredField(KEY);
            SecurityPortProperties.class.getDeclaredField(DIFFIE);
            SecurityPortProperties.class.getDeclaredField(HELLMAN);
        } catch (NoSuchFieldException e) {
            fail();
        }
    }
}
