package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ThreadGuardedSharedPreferencesWrapperTest {

    private static final String S = "S";
    private static final String S_1 = "S1";

    private ThreadGuardedSharedPreferencesWrapper threadGuardedSharedPreferencesWrapper;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private Handler handlerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Mock
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListenerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        long threadId = Thread.currentThread().getId();
        threadGuardedSharedPreferencesWrapper = new ThreadGuardedSharedPreferencesWrapper(sharedPreferencesMock, handlerMock, threadId);
    }

    @Test
    public void testGetAll() throws Exception {
        threadGuardedSharedPreferencesWrapper.getAll();

        verify(sharedPreferencesMock).getAll();
    }

    @Test
    public void whenGetAllIsCalledThenPostDelayedIsCalledOnTheHandler() throws Exception {
        threadGuardedSharedPreferencesWrapper.getAll();

        verify(handlerMock).postDelayed(any(Runnable.class), anyLong());
    }

    @Test
    public void whenGetAllReturnsValueThenRemoveCallbacksIsCalledOnTheHandler() throws Exception {
        threadGuardedSharedPreferencesWrapper.getAll();

        verify(handlerMock).removeCallbacks(any(Runnable.class));
    }

    @Test(expected = AssertionError.class)
    public void whenTimeOutExpiresThenAssertErrorIsGiven() throws Exception {
        threadGuardedSharedPreferencesWrapper.getAll();
        verify(handlerMock).postDelayed(runnableCaptor.capture(), anyLong());

        runnableCaptor.getValue().run();
    }

    @Test(expected = RuntimeException.class)
    public void whenThreadIdDoesNotMatchThenExceptionIsGenerated() throws Exception {
        threadGuardedSharedPreferencesWrapper = new ThreadGuardedSharedPreferencesWrapper(sharedPreferencesMock, handlerMock, 0);

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
}