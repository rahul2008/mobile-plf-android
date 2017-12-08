package com.philips.cdp.prodreg.localcache;

import android.support.annotation.NonNull;

import com.philips.cdp.registration.BuildConfig;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ProdRegCacheTest extends TestCase {

    private ProdRegCache prodRegCache;
    private SecureStorageInterface ssInterface;
    private SecureStorageInterface.SecureStorageError ssError;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ssInterface = mock(SecureStorageInterface.class);
        ssError = mock(SecureStorageInterface.SecureStorageError.class);
        prodRegCache = new ProdRegCache() {
            @Override
            public SecureStorageInterface getAppInfraSecureStorageInterface() {
                return ssInterface;
            }

            @NonNull
            @Override
            public SecureStorageInterface.SecureStorageError getSecureStorageError() {
                return ssError;
            }
        };
    }

    @Test
    public void testStoreStringData() {
        prodRegCache.storeStringData("", "data");
        verify(ssInterface).storeValueForKey("", "data", ssError);
    }

    @Test
    public void testGetStringData() {
        final String testData = "test";
        when(ssInterface.fetchValueForKey("", ssError)).thenReturn(testData);
        final String data = prodRegCache.getStringData("");
        assertEquals(data, testData);
    }

    @Test
    public void testGetIntData() {
        final String testData = "5";
        when(ssInterface.fetchValueForKey("", ssError)).thenReturn(testData);
        final int data = prodRegCache.getIntData("");
        assertEquals(String.valueOf(data), testData);
    }
}