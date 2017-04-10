package com.philips.cdp.registration.app.tagging;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EncryptionTest extends TestCase {

    private Encryption encryption;
    @Before
    public void setUp() throws Exception {
        encryption = new Encryption();
    }

    @After
    public void tearDown() throws Exception {
        encryption = null;
    }

    @Test
    public void testEncrypt_NullInput() throws Exception {
        String toBeEncrypted = null;
        String encrpytedValue = encryption.encrypt(toBeEncrypted);
        assertEquals(null, encrpytedValue);
    }
}