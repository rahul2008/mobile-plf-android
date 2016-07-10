package com.philips.cdp.prodreg.util;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.localcache.ProdRegCache;

import java.text.ParseException;

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

    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(ProdRegUtil.isValidDate("1998-03-22"));
        assertFalse(ProdRegUtil.isValidDate("2098-05-22"));
    }

    public void testIsInValidSerialNumber() {
        assertTrue(ProdRegUtil.isInValidSerialNumber("[0-9]-[0-9]-[0-9]", "@3456"));
        assertTrue(ProdRegUtil.isInValidSerialNumber("", ""));
        assertTrue(ProdRegUtil.isInValidSerialNumber(null, ""));
    }

    public void testIsFutureDate() {
        String date = "2098-05-22";
        assertTrue(ProdRegUtil.isFutureDate(date));
        String date1 = "2016-05-16";
        assertFalse(ProdRegUtil.isFutureDate(date1));
        try {
            assertFalse(ProdRegUtil.isFutureDate("05/06/2016"));
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
        }
    }

    public void testGettingMinDate() {
        assertTrue(ProdRegUtil.getMinDate() != 0);
    }

    public void testStoreProdRegTaggingMeasuresCount() {
        ProdRegCache prodRegCacheMock = mock(ProdRegCache.class);
        String key = ProdRegConstants.Product_REGISTRATION_START_COUNT;
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


}