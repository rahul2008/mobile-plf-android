package com.philips.cdp.prodreg.localcache;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.registration.BuildConfig;

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
    private Context contextMock;
    private SharedPreferences sharedPreferencesMock;
    private SharedPreferences.Editor editorMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        contextMock = mock(Context.class);
        editorMock = mock(SharedPreferences.Editor.class);
        sharedPreferencesMock = mock(SharedPreferences.class);
        prodRegCache = new ProdRegCache(contextMock);
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(contextMock.getSharedPreferences(ProdRegConstants.PRODUCT_REGISTRATION, Context.MODE_PRIVATE)).thenReturn(sharedPreferencesMock);
    }

    @Test
    public void testStoreStringData() {
        prodRegCache.storeStringData("", "data");
        verify(editorMock).putString("", "data");
        verify(editorMock).commit();
    }

    @Test
    public void testGetStringData() {
        final String testData = "test";
        when(sharedPreferencesMock.getString("", null)).thenReturn(testData);
        final String data = prodRegCache.getStringData("");
        assertEquals(data, testData);
    }

    @Test
    public void testGetIntData() {
        final int testData = 5;
        when(sharedPreferencesMock.getInt("", 0)).thenReturn(testData);
        final int data = prodRegCache.getIntData("");
        assertEquals(data, testData);
    }
}