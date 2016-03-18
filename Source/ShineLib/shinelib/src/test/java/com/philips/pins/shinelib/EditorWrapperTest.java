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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EditorWrapperTest {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    private EditorWrapper wrapper;

    @Mock
    private SharedPreferences.Editor editorMock;

    @Mock
    private Handler handlerMock;
    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        wrapper = new EditorWrapper(editorMock, handlerMock);
    }

    @Test
    public void testPutString() throws Exception {
        wrapper.putString(KEY, VALUE);

        verify(editorMock).putString(KEY, VALUE);
    }

    @Test
    public void whenPutStringIsCalledThenPostDelayedIsCalledOnTheHandler() throws Exception {
        wrapper.putString(KEY, VALUE);

        verify(handlerMock).postDelayed(any(Runnable.class), anyLong());
    }

    @Test
    public void whenPutStringReturnsValueThenRemoveCallbacksIsCalledOnTheHandler() throws Exception {
        wrapper.putString(KEY, VALUE);

        verify(handlerMock).removeCallbacks(any(Runnable.class));
    }

    @Test(expected = AssertionError.class)
    public void whenTimeOutExpiresThenAssertErrorIsGiven() throws Exception {
        wrapper.putString(KEY, VALUE);

        verify(handlerMock).postDelayed(runnableCaptor.capture(), anyLong());

        runnableCaptor.getValue().run();
    }

    @Test
    public void whenPutStringIsCalledThenReturnTypeIsWrapper() throws Exception {
        SharedPreferences.Editor editor = wrapper.putString(KEY, VALUE);

        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testPutStringSet() throws Exception {
        Set<String> empty = Collections.emptySet();
        SharedPreferences.Editor editor = wrapper.putStringSet(KEY, empty);

        verify(editorMock).putStringSet(KEY, empty);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testPutInt() throws Exception {
        SharedPreferences.Editor editor = wrapper.putInt(KEY, 0);

        verify(editorMock).putInt(KEY, 0);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testPutLong() throws Exception {
        SharedPreferences.Editor editor = wrapper.putLong(KEY, 0);

        verify(editorMock).putLong(KEY, 0);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testPutFloat() throws Exception {
        SharedPreferences.Editor editor = wrapper.putFloat(KEY, 0);

        verify(editorMock).putFloat(KEY, 0);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testPutBoolean() throws Exception {
        SharedPreferences.Editor editor = wrapper.putBoolean(KEY, false);

        verify(editorMock).putBoolean(KEY, false);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testRemove() throws Exception {
        SharedPreferences.Editor editor = wrapper.remove(KEY);

        verify(editorMock).remove(KEY);
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testClear() throws Exception {
        SharedPreferences.Editor editor = wrapper.clear();

        verify(editorMock).clear();
        assertTrue(editor instanceof EditorWrapper);
    }

    @Test
    public void testCommit() throws Exception {
        wrapper.commit();

        verify(editorMock).commit();
    }

    @Test
    public void testApply() throws Exception {
        wrapper.apply();

        verify(editorMock).apply();
    }
}