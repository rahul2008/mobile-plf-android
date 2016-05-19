/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class IAPHandleListenerTest {
//Verify there are no api-break
    @Test
    public void validateInterfaceApis() throws NoSuchMethodException {
        assertNotNull(IAPHandlerListener.class.getMethod("onSuccess", Integer.TYPE));
        assertNotNull(IAPHandlerListener.class.getMethod("onFailure", Integer.TYPE));
    }
}
