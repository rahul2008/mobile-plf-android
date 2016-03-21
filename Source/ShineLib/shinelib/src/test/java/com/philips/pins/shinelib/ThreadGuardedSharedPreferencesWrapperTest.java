package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ThreadGuardedSharedPreferencesWrapperTest {

    private static final String S = "S";
    private static final String S_1 = "S1";
    public static final long NORMAL_EXECUTION_TIME = 40L;
    public static final long EXCEEDED_EXECUTION_TIME = 60L;

    private ThreadGuardedSharedPreferencesWrapper threadGuardedSharedPreferencesWrapper;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListenerMock;
    public static final long THREAD_ID = Thread.currentThread().getId();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        threadGuardedSharedPreferencesWrapper = new ThreadGuardedSharedPreferencesWrapperForTest(sharedPreferencesMock, THREAD_ID, NORMAL_EXECUTION_TIME);
    }

    @Test
    public void testGetAll() throws Exception {
        threadGuardedSharedPreferencesWrapper.getAll();

        verify(sharedPreferencesMock).getAll();
    }

    @Test(expected = AssertionError.class)
    public void whenTimeOutExpiresThenAssertErrorIsGiven() throws Exception {
        threadGuardedSharedPreferencesWrapper = new ThreadGuardedSharedPreferencesWrapperForTest(sharedPreferencesMock, THREAD_ID, EXCEEDED_EXECUTION_TIME);

        threadGuardedSharedPreferencesWrapper.getAll();
    }

    @Test(expected = RuntimeException.class)
    public void whenThreadIdDoesNotMatchThenExceptionIsGenerated() throws Exception {
        threadGuardedSharedPreferencesWrapper = new ThreadGuardedSharedPreferencesWrapper(sharedPreferencesMock, 0);

        threadGuardedSharedPreferencesWrapper.getAll();
    }

    @Test
    public void testGetString() throws Exception {
        threadGuardedSharedPreferencesWrapper.getString(S, S_1);

        verify(sharedPreferencesMock).getString(S, S_1);
    }

    @Test
    public void testGetStringSet() throws Exception {
        Set<String> empty = Collections.emptySet();
        threadGuardedSharedPreferencesWrapper.getStringSet(S, empty);

        verify(sharedPreferencesMock).getStringSet(S, empty);
    }

    @Test
    public void testGetInt() throws Exception {
        threadGuardedSharedPreferencesWrapper.getInt(S, 0);

        verify(sharedPreferencesMock).getInt(S, 0);
    }

    @Test
    public void testGetLong() throws Exception {
        threadGuardedSharedPreferencesWrapper.getLong(S, 0);

        verify(sharedPreferencesMock).getLong(S, 0);
    }

    @Test
    public void testGetFloat() throws Exception {
        threadGuardedSharedPreferencesWrapper.getFloat(S, 0);

        verify(sharedPreferencesMock).getFloat(S, 0);
    }

    @Test
    public void testGetBoolean() throws Exception {
        threadGuardedSharedPreferencesWrapper.getBoolean(S, false);

        verify(sharedPreferencesMock).getBoolean(S, false);
    }

    @Test
    public void testContains() throws Exception {
        threadGuardedSharedPreferencesWrapper.contains(S);

        verify(sharedPreferencesMock).contains(S);
    }

    @Test
    public void testEdit() throws Exception {
        threadGuardedSharedPreferencesWrapper.edit();

        verify(sharedPreferencesMock).edit();
    }

    @Test
    public void testRegisterOnSharedPreferenceChangeListener() throws Exception {
        threadGuardedSharedPreferencesWrapper.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);

        verify(sharedPreferencesMock).registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);
    }

    @Test
    public void testUnregisterOnSharedPreferenceChangeListener() throws Exception {
        threadGuardedSharedPreferencesWrapper.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);

        verify(sharedPreferencesMock).unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);
    }

    class ThreadGuardedSharedPreferencesWrapperForTest extends ThreadGuardedSharedPreferencesWrapper {

        private long start = 100;
        private long increment;

        public ThreadGuardedSharedPreferencesWrapperForTest(SharedPreferences sharedPreferences, long threadId, long increment) {
            super(sharedPreferences, threadId);
            this.increment = increment;
        }

        @Override
        protected long getCurrentTimeInMillis() {
            return start += increment;
        }
    }
}