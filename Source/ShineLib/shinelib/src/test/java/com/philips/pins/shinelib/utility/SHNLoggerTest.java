package com.philips.pins.shinelib.utility;

import android.util.Log;

import com.philips.pins.shinelib.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNLoggerTest extends RobolectricTest {

    @Mock
    SHNLogger.LoggerImplementation mockedImplementation1;

    @Mock
    SHNLogger.LoggerImplementation mockedImplementation2;

    @Mock
    Throwable mockedThrowable;

    public static final String TEST_TAG = "This Is a Tag";
    public static final String TEST_MSG = "This is a message\n over two lines";
    public static final String LOGGER_HANDLER_PREFIX = "[TID: 0] ";
    public static final String TEST_MSG_ON_LOGGER_HANDLER = LOGGER_HANDLER_PREFIX + TEST_MSG;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        SHNLogger.registerLogger(mockedImplementation1);
        SHNLogger.registerLogger(mockedImplementation2);
    }

    private void verifyForwarded(int priority, String tag, String msg, Throwable tr) {
        verify(mockedImplementation1).logLine(priority, tag, msg, tr);
        verify(mockedImplementation2).logLine(priority, tag, msg, tr);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_V_FunctionIsCalled() throws Exception {
        SHNLogger.v(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.VERBOSE, TEST_TAG, TEST_MSG, null);

        SHNLogger.v(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.VERBOSE, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_D_LoggingFunctionIsCalled() throws Exception {
        SHNLogger.d(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.DEBUG, TEST_TAG, TEST_MSG, null);

        SHNLogger.d(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.DEBUG, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_I_LoggingFunctionIsCalled() throws Exception {
        SHNLogger.i(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.INFO, TEST_TAG, TEST_MSG, null);

        SHNLogger.i(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.INFO, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_W_LoggingFunctionIsCalled() throws Exception {
        SHNLogger.w(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.WARN, TEST_TAG, TEST_MSG, null);

        SHNLogger.w(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.WARN, TEST_TAG, TEST_MSG, mockedThrowable);

        SHNLogger.w(TEST_TAG, mockedThrowable);
        verifyForwarded(Log.WARN, TEST_TAG, "", mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_E_LoggingFunctionIsCalled() throws Exception {
        SHNLogger.e(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.ERROR, TEST_TAG, TEST_MSG, null);

        SHNLogger.e(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.ERROR, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_WTF_LoggingFunctionIsCalled() throws Exception {
        SHNLogger.wtf(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.ASSERT, TEST_TAG, TEST_MSG, null);

        SHNLogger.wtf(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.ASSERT, TEST_TAG, TEST_MSG, mockedThrowable);

        SHNLogger.wtf(TEST_TAG, mockedThrowable);
        verifyForwarded(Log.ASSERT, TEST_TAG, "", mockedThrowable);
    }

    @Test
    public void ShouldNotCallALogger_WhenItWasAlreadyRemoved_When() throws Exception {
        SHNLogger.unregisterLogger(mockedImplementation2);

        SHNLogger.e(TEST_TAG, TEST_MSG);

        verify(mockedImplementation1).logLine(Log.ERROR, TEST_TAG, TEST_MSG, null);
        verify(mockedImplementation2, never()).logLine(anyInt(), anyString(), anyString(), any(Throwable.class));
    }

    @Test
    public void ShouldBePossibleToCreateALogCatLogger() throws Exception {
        assertNotNull(new SHNLogger.LogCatLogger());
    }
}