/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

public class EncryptionUtilTest {

    @Test
    public void testGetEvenNumberSecretKey255bitKey() {
        String key255bit = "9cd15f5d121ec8c9adbd0682fb9e8d079cba90e7683230985a895f6d90b7d87884e4a3a4cc80ac58889de8f174d0df7dd4fd1c3e7d1f766fdeed89154ea6714ee8f70e551299e41ff8a6f51d60f2f763d8b58af70119fc0734ee80ddbccf0f84d22b5add6103be35dfff1a521075d973fc3262a98a5378364851bbd6a7b1cab";

        String newKey = EncryptionUtil.getEvenNumberSecretKey(key255bit);
        assertEquals(256, newKey.length());
    }

    @Test
    public void testGetEvenNumberSecretKey256bitKey() {
        String key256bit = "2253FD28E69FAC2F38620ED0B6F84565C7634E586CFF83C6300AC296F6DFE1C668D04627F6F929569BC2F783DAB16EAC33D6231959EEC9EBB1BAD522B7B919EA4C51C660A148DCA3B3B2AD558B07DF959337E64423BF4EC8EBD2333032AF11FC430746965E30862680EB9CF075AFD87B60F597699F2EE4354796C7ADAB581747";

        String newKey = EncryptionUtil.getEvenNumberSecretKey(key256bit);
        assertEquals(256, newKey.length());
    }

    @Test
    public void testDiffieGeneration() {
        String diffie1 = EncryptionUtil.generateDiffieKey("111");
        String diffie2 = EncryptionUtil.generateDiffieKey("222");

        assertNotNull(diffie1);
        assertNotNull(diffie2);
        assertNotSame(diffie1, diffie2);
    }
}
