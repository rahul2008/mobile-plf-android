package com.philips.cdp.dicommclient.util;

import android.util.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class DICommLogTest {

    private final static String TAG = "DICommLogTest";
    private final static String TEST_MESSAGE = "test message";

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);

        DICommLog.enableLogging();
    }

    @Test
    public void whenLoggingIsEnabled_thenLogMessagesShouldBePrinted() {
        DICommLog.disableLogging();
        DICommLog.enableLogging();

        DICommLog.d(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.d(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLoggingIsDisabled_thenLogMessagesShouldNotBePrinted() {
        DICommLog.disableLogging();

        DICommLog.d(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, never());
        Log.d(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenDebugLogPerformed_thenLoglevelIsDebug() {
        DICommLog.d(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.d(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenErrorLogPerformed_thenLoglevelIsError() {
        DICommLog.e(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.e(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenInfoLogPerformed_thenLoglevelIsInfo() {
        DICommLog.i(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.i(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenVerboseLogPerformed_thenLoglevelIsVerbose() {
        DICommLog.v(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.v(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenWarningLogPerformed_thenLoglevelIsWarning() {
        DICommLog.w(TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.w(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLogIsCalledWithVerbosityDebug_thenLogLevelIsDebug() {
        DICommLog.log(DICommLog.Verbosity.DEBUG, TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.d(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLogIsCalledWithVerbosityError_thenLogLevelIsError() {
        DICommLog.log(DICommLog.Verbosity.ERROR, TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.e(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLogIsCalledWithVerbosityInfo_thenLogLevelIsInfo() {
        DICommLog.log(DICommLog.Verbosity.INFO, TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.i(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLogIsCalledWithVerbosityVerbose_thenLogLevelIsVerbose() {
        DICommLog.log(DICommLog.Verbosity.VERBOSE, TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.v(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLogIsCalledWithVerbosityWarning_thenLogLevelIsWarning() {
        DICommLog.log(DICommLog.Verbosity.WARN, TAG, TEST_MESSAGE);

        verifyStatic(Log.class, times(1));
        Log.w(TAG, TEST_MESSAGE);
    }

    @Test
    public void whenLoggingIsEnabled_thenIsLoggingEnabledReturnsTrue() {
        assertTrue(DICommLog.isLoggingEnabled());
    }

    @Test
    public void whenLoggingIsDisabled_thenIsLoggingEnabledReturnsFalse() {
        DICommLog.disableLogging();

        assertFalse(DICommLog.isLoggingEnabled());
    }
}