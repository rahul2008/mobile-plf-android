package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SharedPreferencesProviderWrapperTest {

    SharedPreferencesProviderWrapper sharedPreferencesProviderWrapper;

    @Mock
    SharedPreferencesProvider sharedPreferencesProviderMock;

    @Mock
    Handler handlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        sharedPreferencesProviderWrapper = new SharedPreferencesProviderWrapper(sharedPreferencesProviderMock, handlerMock, 0);
    }

    @Test
    public void canCreate() throws Exception {
        assertNotNull(sharedPreferencesProviderWrapper);
    }

    @Test
    public void whenGetPrefixIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        sharedPreferencesProviderWrapper.getSharedPreferencesPrefix();

        verify(sharedPreferencesProviderMock).getSharedPreferencesPrefix();
    }

    @Test
    public void getPrefixIsCalledReturnsTheValue() throws Exception {
        String mockedPrefix = "prefix";
        when(sharedPreferencesProviderMock.getSharedPreferencesPrefix()).thenReturn(mockedPrefix);

        String prefix = sharedPreferencesProviderWrapper.getSharedPreferencesPrefix();

        assertEquals(mockedPrefix, prefix);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        String name = "name";
        int mode = 0;
        sharedPreferencesProviderWrapper.getSharedPreferences(name, mode);

        verify(sharedPreferencesProviderMock).getSharedPreferences(name, mode);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenReturnedTypeIsWrapper() throws Exception {
        String name = "name";
        int mode = 0;
        SharedPreferences sharedPreferences = sharedPreferencesProviderWrapper.getSharedPreferences(name, mode);

        assertTrue(sharedPreferences instanceof SharedPreferencesWrapper);
    }
}