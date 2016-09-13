package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TimeGuardedSharedPreferencesProviderWrapperTest {

    private static final long EXCEEDED_EXECUTION_TIME = 60L;
    private TimeGuardedSharedPreferencesProviderWrapper timeGuardedSharedPreferencesProviderWrapper;

    @Mock
    SharedPreferencesProvider sharedPreferencesProviderMock;
    public static final String NAME = "name";
    public static final int MODE = 0;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        timeGuardedSharedPreferencesProviderWrapper = new TimeGuardedSharedPreferencesProviderWrapper(sharedPreferencesProviderMock, 0);
    }

    @Test
    public void canCreate() throws Exception {
        assertNotNull(timeGuardedSharedPreferencesProviderWrapper);
    }

    @Test
    public void whenGetPrefixIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        timeGuardedSharedPreferencesProviderWrapper.getSharedPreferencesPrefix();

        verify(sharedPreferencesProviderMock).getSharedPreferencesPrefix();
    }

    @Test
    public void getPrefixIsCalledReturnsTheValue() throws Exception {
        String mockedPrefix = "prefix";
        when(sharedPreferencesProviderMock.getSharedPreferencesPrefix()).thenReturn(mockedPrefix);

        String prefix = timeGuardedSharedPreferencesProviderWrapper.getSharedPreferencesPrefix();

        assertEquals(mockedPrefix, prefix);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenCallIsPassedToWrappedProvider() throws Exception {
        timeGuardedSharedPreferencesProviderWrapper.getSharedPreferences(NAME, MODE);

        verify(sharedPreferencesProviderMock).getSharedPreferences(NAME, MODE);
    }

    @Test
    public void whenGetSharedPreferencesIsCalledThenReturnedTypeIsWrapper() throws Exception {
        SharedPreferences sharedPreferences = timeGuardedSharedPreferencesProviderWrapper.getSharedPreferences(NAME, MODE);

        assertTrue(sharedPreferences instanceof TimeGuardedSharedPreferencesWrapper);
    }

    @Test(expected = TimeoutException.class)
    public void whenTimeOutExpiresThenAssertErrorIsGiven() throws Exception {
        timeGuardedSharedPreferencesProviderWrapper = new TimeGuardedSharedPreferencesProviderWrapperForTest(sharedPreferencesProviderMock, 0, EXCEEDED_EXECUTION_TIME);

        timeGuardedSharedPreferencesProviderWrapper.getSharedPreferences(NAME, MODE);
    }

    class TimeGuardedSharedPreferencesProviderWrapperForTest extends TimeGuardedSharedPreferencesProviderWrapper {

        private long start = 100;
        private long increment;

        public TimeGuardedSharedPreferencesProviderWrapperForTest(SharedPreferencesProvider sharedPreferences, long threadId, long increment) {
            super(sharedPreferences, threadId);
            this.increment = increment;
        }

        @Override
        protected long getCurrentTimeInMillis() {
            return start += increment;
        }
    }
}