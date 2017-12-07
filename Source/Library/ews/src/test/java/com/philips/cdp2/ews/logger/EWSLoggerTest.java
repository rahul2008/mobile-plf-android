/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.logger;

import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.ERROR;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.INFO;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.VERBOSE;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.WARNING;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class EWSLoggerTest {

    private static final String TAG = "EWSLogger";
    private static final String MSG = "sample message";

    @Mock
    private LoggingInterface mockLoggingInterface;

    private EWSLogger subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new EWSLogger(mockLoggingInterface);
    }

    @Test
    public void itShouldVerifyConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<EWSLogger> constructor = EWSLogger.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void itShouldLogMessageInVerboseLevel() throws Exception {
        subject.v(TAG, MSG);

        verify(mockLoggingInterface).log(VERBOSE, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInDebugLevel() throws Exception {
        subject.d(TAG, MSG);

        verify(mockLoggingInterface).log(DEBUG, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInWarningLevel() throws Exception {
        subject.w(TAG, MSG);

        verify(mockLoggingInterface).log(WARNING, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInInfoLevel() throws Exception {
        subject.i(TAG, MSG);

        verify(mockLoggingInterface).log(INFO, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInErrorLevel() throws Exception {
        subject.e(TAG, MSG);

        verify(mockLoggingInterface).log(ERROR, TAG, MSG);
    }
}