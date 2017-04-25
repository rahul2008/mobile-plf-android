/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TimeGuardedSharedPreferencesWrapperTest {

    private static final String S = "S";
    private static final String S_1 = "S1";
    public static final long NORMAL_EXECUTION_TIME = 40L;

    private TimeGuardedSharedPreferencesWrapper timeGuardedSharedPreferencesWrapper;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListenerMock;
    public static final long THREAD_ID = Thread.currentThread().getId();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        timeGuardedSharedPreferencesWrapper = new TimeGuardedSharedPreferencesWrapperForTest(sharedPreferencesMock, THREAD_ID, NORMAL_EXECUTION_TIME);
    }

    @Test
    public void testGetAll() throws Exception {
        timeGuardedSharedPreferencesWrapper.getAll();

        verify(sharedPreferencesMock).getAll();
    }

    @Test(expected = RuntimeException.class)
    public void whenThreadIdDoesNotMatchThenExceptionIsGenerated() throws Exception {
        timeGuardedSharedPreferencesWrapper = new TimeGuardedSharedPreferencesWrapper(sharedPreferencesMock, 0);

        timeGuardedSharedPreferencesWrapper.getAll();
    }

    @Test
    public void testGetString() throws Exception {
        timeGuardedSharedPreferencesWrapper.getString(S, S_1);

        verify(sharedPreferencesMock).getString(S, S_1);
    }

    @Test
    public void testGetStringSet() throws Exception {
        Set<String> empty = Collections.emptySet();
        timeGuardedSharedPreferencesWrapper.getStringSet(S, empty);

        verify(sharedPreferencesMock).getStringSet(S, empty);
    }

    @Test
    public void testGetInt() throws Exception {
        timeGuardedSharedPreferencesWrapper.getInt(S, 0);

        verify(sharedPreferencesMock).getInt(S, 0);
    }

    @Test
    public void testGetLong() throws Exception {
        timeGuardedSharedPreferencesWrapper.getLong(S, 0);

        verify(sharedPreferencesMock).getLong(S, 0);
    }

    @Test
    public void testGetFloat() throws Exception {
        timeGuardedSharedPreferencesWrapper.getFloat(S, 0);

        verify(sharedPreferencesMock).getFloat(S, 0);
    }

    @Test
    public void testGetBoolean() throws Exception {
        timeGuardedSharedPreferencesWrapper.getBoolean(S, false);

        verify(sharedPreferencesMock).getBoolean(S, false);
    }

    @Test
    public void testContains() throws Exception {
        timeGuardedSharedPreferencesWrapper.contains(S);

        verify(sharedPreferencesMock).contains(S);
    }

    @Test
    public void testEdit() throws Exception {
        timeGuardedSharedPreferencesWrapper.edit();

        verify(sharedPreferencesMock).edit();
    }

    @Test
    public void testRegisterOnSharedPreferenceChangeListener() throws Exception {
        timeGuardedSharedPreferencesWrapper.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);

        verify(sharedPreferencesMock).registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);
    }

    @Test
    public void testUnregisterOnSharedPreferenceChangeListener() throws Exception {
        timeGuardedSharedPreferencesWrapper.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);

        verify(sharedPreferencesMock).unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListenerMock);
    }

    class TimeGuardedSharedPreferencesWrapperForTest extends TimeGuardedSharedPreferencesWrapper {

        private long start = 100;
        private long increment;

        public TimeGuardedSharedPreferencesWrapperForTest(SharedPreferences sharedPreferences, long threadId, long increment) {
            super(sharedPreferences, threadId);
            this.increment = increment;
        }

        @Override
        protected long getCurrentTimeInMillis() {
            return start += increment;
        }
    }
}