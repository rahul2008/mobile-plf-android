/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import org.junit.Test;

public class SecurityPortPropertiesTest {

    @Test
    public void mustHaveFieldsWithNamesAccordingToSpec() throws Exception {
        Class<SecurityPortProperties> securityPortPropertiesClass = SecurityPortProperties.class;
        securityPortPropertiesClass.getDeclaredField("key");
        securityPortPropertiesClass.getDeclaredField("diffie");
        securityPortPropertiesClass.getDeclaredField("hellman");
    }
}