package com.philips.cdp.prodreg.util;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtilTest extends MockitoTestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(ProdRegUtil.isValidDate("2016-03-22"));
        assertFalse(ProdRegUtil.isValidDate(""));
        assertFalse(ProdRegUtil.isValidDate(null));
    }

    public void testGettingMinDate() {
        assertTrue(ProdRegUtil.getMinDate() != 0);
    }

    public void testStoreProdRegTaggingMeasuresCount() {
        ProdRegCache prodRegCacheMock = mock(ProdRegCache.class);
        String key = AnalyticsConstants.Product_REGISTRATION_START_COUNT;
        when(prodRegCacheMock.getIntData(key)).thenReturn(1);
        int count = 2;
        ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCacheMock, key, count);
        verify(prodRegCacheMock).storeIntData(key, 3);
    }

    public void testGettingValidatedString() {
        int value = 5;
        String data = ProdRegUtil.getValidatedString(value);
        assertTrue(data.equals("05"));

        int value2 = 15;
        String data2 = ProdRegUtil.getValidatedString(value2);
        assertTrue(data2.equals("15"));
    }

    public void testIsValidSerialNumber() {
        String serialNumber = "124";
        assertTrue(ProdRegUtil.isValidSerialNumber(true, "[0-9][0-9][0-9]", serialNumber));
        assertTrue(ProdRegUtil.isValidSerialNumber(true, null, serialNumber));
        assertTrue(ProdRegUtil.isValidSerialNumber(true, "", serialNumber));
        assertFalse(ProdRegUtil.isValidSerialNumber(true, "[0-9][0-9][0-9]", null));
        assertFalse(ProdRegUtil.isValidSerialNumber(true, "[0-9][0-9][0-9]", ""));
        assertTrue(ProdRegUtil.isValidSerialNumber(false, "[0-9][0-9][0-9]", ""));
    }

}