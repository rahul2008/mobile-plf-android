/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashMap;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SHNCapabilityDeviceDiagnosticsWrapperTest {

    private SHNCapabilityDeviceDiagnosticsWrapper diagnosticsWrapper;

    @Mock
    private SHNCapabilityDeviceDiagnostics capabilityMock;

    @Mock
    private SHNMapResultListener<String, String> listenerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<SHNMapResultListener<String, String>> mapResultListenerCaptor;

    private Handler internalHandlerMock;
    private Handler userHandlerMock;

    private static final HashMap<String, String> TEST_MAP = new HashMap<>();
    private static final SHNResult TEST_RESULT = SHNResult.SHNErrorConnectionLost;

    @Before
    public void setUp() {
        initMocks(this);

        internalHandlerMock = mock(Handler.class);
        userHandlerMock = mock(Handler.class);

        diagnosticsWrapper = new SHNCapabilityDeviceDiagnosticsWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldPostReadMethodOnCapabilityOnInternalHandler_WhenReadIsCalled() {
        Runnable runnable = captureInternalHandlerRunnable();

        runnable.run();

        verify(capabilityMock).readDeviceDiagnostics(isA(SHNMapResultListener.class));
    }

    @Test
    public void shouldPostReadMethodOnListenerOnUserHandler_WhenReadIsCalled() {
        Runnable internalRunnable = captureInternalHandlerRunnable();
        SHNMapResultListener<String, String> mapResultListener = captureMapResultListener(internalRunnable);
        Runnable userRunnable = captureUserRunnable(mapResultListener);

        userRunnable.run();

        verify(listenerMock).onActionCompleted(TEST_MAP, TEST_RESULT);
    }

    // -------------------------

    private Runnable captureUserRunnable(final SHNMapResultListener<String, String> mapResultListener) {
        mapResultListener.onActionCompleted(TEST_MAP, TEST_RESULT);
        verify(userHandlerMock).post(runnableCaptor.capture());
        return runnableCaptor.getValue();
    }

    private Runnable captureInternalHandlerRunnable() {
        diagnosticsWrapper.readDeviceDiagnostics(listenerMock);
        verify(internalHandlerMock).post(runnableCaptor.capture());

        return runnableCaptor.getValue();
    }

    private SHNMapResultListener<String, String> captureMapResultListener(final Runnable runnable) {
        runnable.run();

        verify(capabilityMock).readDeviceDiagnostics(mapResultListenerCaptor.capture());
        return mapResultListenerCaptor.getValue();
    }
}