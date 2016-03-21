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

public class ThreadGuardedSharedPreferencesProvidedWrapperTest {

    ThreadGuardedSharedPreferencesProvidedWrapper threadGuardedSharedPreferencesProvidedWrapper;

    @Mock
    SharedPreferencesProvider sharedPreferencesProviderMock;

    @Mock
    Handler handlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        threadGuardedSharedPreferencesProvidedWrapper = new ThreadGuardedSharedPreferencesProvidedWrapper(sharedPreferencesProviderMock, handlerMock, 0);
    }

    @Test
    public void canCreate() throws Exception {
        assertNotNull(threadGuardedSharedPreferencesProvidedWrapper);
    }

    @Test
    public void whenGetPrefixIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        threadGuardedSharedPreferencesProvidedWrapper.getSharedPreferencesPrefix();

        verify(sharedPreferencesProviderMock).getSharedPreferencesPrefix();
    }

    @Test
    public void getPrefixIsCalledReturnsTheValue() throws Exception {
        String mockedPrefix = "prefix";
        when(sharedPreferencesProviderMock.getSharedPreferencesPrefix()).thenReturn(mockedPrefix);

        String prefix = threadGuardedSharedPreferencesProvidedWrapper.getSharedPreferencesPrefix();

        assertEquals(mockedPrefix, prefix);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        String name = "name";
        int mode = 0;
        threadGuardedSharedPreferencesProvidedWrapper.getSharedPreferences(name, mode);

        verify(sharedPreferencesProviderMock).getSharedPreferences(name, mode);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenReturnedTypeIsWrapper() throws Exception {
        String name = "name";
        int mode = 0;
        SharedPreferences sharedPreferences = threadGuardedSharedPreferencesProvidedWrapper.getSharedPreferences(name, mode);

        assertTrue(sharedPreferences instanceof ThreadGuardedSharedPreferencesWrapper);
    }
}