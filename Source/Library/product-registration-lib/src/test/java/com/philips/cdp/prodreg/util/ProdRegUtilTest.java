package com.philips.cdp.prodreg.util;

import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtilTest extends TestCase {

    private ProdRegUtil prodRegUtil;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        prodRegUtil = new ProdRegUtil();
    }

    @Test
    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(prodRegUtil.isValidDate("2016-03-22"));
        assertFalse(prodRegUtil.isValidDate(""));
        assertFalse(prodRegUtil.isValidDate(null));
    }

    @Test
    public void testIsFutureDate() {
        String date = "2098-05-22";
        assertTrue(prodRegUtil.isFutureDate(date));
        String date1 = "2016-05-16";
        assertFalse(prodRegUtil.isFutureDate(date1));
    }

    @Test
    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(prodRegUtil.isValidDate("1998-03-22"));
        assertFalse(prodRegUtil.isValidDate("2098-05-22"));
    }

    @Test
    public void testGettingMinDate() {
        assertTrue(prodRegUtil.getMinDate() != 0);
    }

    @Test
    public void testStoreProdRegTaggingMeasuresCount() {
        ProdRegCache prodRegCacheMock = mock(ProdRegCache.class);
        SecureStorageInterface ssInterface = mock(SecureStorageInterface.class);
        SecureStorageInterface.SecureStorageError ssError = mock(SecureStorageInterface.SecureStorageError.class);
        String key = AnalyticsConstants.Product_REGISTRATION_START_COUNT;

        when(prodRegCacheMock.getAppInfraSecureStorageInterface()).thenReturn(ssInterface);
        when(prodRegCacheMock.getSecureStorageError()).thenReturn(ssError);
        when(prodRegCacheMock.getIntData(key)).thenReturn(1);

        int count = 2;
        prodRegUtil.storeProdRegTaggingMeasuresCount(prodRegCacheMock, key, count);
        verify(prodRegCacheMock).storeStringData(key, String.valueOf(3));
    }

    @Test
    public void testGettingValidatedString() {
        int value = 5;
        String data = prodRegUtil.getValidatedString(value);
        assertTrue(data.equals("05"));

        int value2 = 15;
        String data2 = prodRegUtil.getValidatedString(value2);
        assertTrue(data2.equals("15"));
    }

    @Test
    public void testIsValidSerialNumber() {
        String serialNumber = "124";
        assertTrue(prodRegUtil.isValidSerialNumber(true, "[0-9][0-9][0-9]", serialNumber));
        assertFalse(prodRegUtil.isValidSerialNumber(true, "[0-9][0-9][0-9]", ""));
        assertTrue(prodRegUtil.isValidSerialNumber(false, "[0-9][0-9][0-9]", ""));
    }
}