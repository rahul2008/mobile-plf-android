/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.logger;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.ERROR;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.INFO;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.VERBOSE;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.WARNING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSDependencyProvider.class)
public class EWSLoggerTest {

    private static final String TAG = "EWSLogger";
    private static final String MSG = "sample message";

    @Mock
    private LoggingInterface loggingInterfaceMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSDependencyProvider.class);
        final EWSDependencyProvider ewsDependencyProviderMock = mock(EWSDependencyProvider.class);
        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(EWSDependencyProvider.getInstance().getLoggerInterface()).thenReturn(loggingInterfaceMock);
    }

    @Test
    public void itShouldLogMessageInVerboseLevel() throws Exception {
        EWSLogger.v(TAG, MSG);

        verify(loggingInterfaceMock).log(VERBOSE, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInDebugLevel() throws Exception {
        EWSLogger.d(TAG, MSG);

        verify(loggingInterfaceMock).log(DEBUG, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInWarningLevel() throws Exception {
        EWSLogger.w(TAG, MSG);

        verify(loggingInterfaceMock).log(WARNING, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInInfoLevel() throws Exception {
        EWSLogger.i(TAG, MSG);

        verify(loggingInterfaceMock).log(INFO, TAG, MSG);
    }

    @Test
    public void itShouldLogMessageInErrorLevel() throws Exception {
        EWSLogger.e(TAG, MSG);

        verify(loggingInterfaceMock).log(ERROR, TAG, MSG);
    }
}