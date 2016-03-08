/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class PairingHandlerTest   {

    @Test
    public void testGenerateRandomSecretKeyNotNull() {
        PairingHandler manager = new PairingHandler(null, null);
        String randomKey = manager.generateRandomSecretKey();
        assertNotNull(randomKey);
    }

    @Test
    public void testGenerateRandomSecretKeyNotEqual() {
        PairingHandler manager = new PairingHandler(null, null);
        String randomKey = manager.generateRandomSecretKey();
        String randomKey1 = manager.generateRandomSecretKey();
        assertFalse(randomKey.equals(randomKey1));
    }

    @Test
    public void testGetDiffInDays() {
        long rl = PairingHandler.getDiffInDays(0L);
        assertTrue(rl != 0);
    }
}
