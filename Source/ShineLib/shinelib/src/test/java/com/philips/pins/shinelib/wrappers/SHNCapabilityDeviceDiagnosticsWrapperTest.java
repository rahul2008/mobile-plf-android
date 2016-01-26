/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

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

public class SHNCapabilityDeviceDiagnosticsWrapperTest extends SHNCapabilityWrapperTestBase {

    private SHNCapabilityDeviceDiagnosticsWrapper diagnosticsWrapper;

    @Mock
    private SHNCapabilityDeviceDiagnostics capabilityMock;

    @Mock
    private SHNMapResultListener<String, String> listenerMock;

    @Captor
    private ArgumentCaptor<SHNMapResultListener<String, String>> mapResultListenerCaptor;

    private static final HashMap<String, String> TEST_MAP = new HashMap<>();
    private static final SHNResult TEST_RESULT = SHNResult.SHNErrorConnectionLost;

    @Before
    public void setUp() {
        initMocks(this);

        diagnosticsWrapper = new SHNCapabilityDeviceDiagnosticsWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldPostReadMethodOnCapabilityOnInternalHandler_WhenReadIsCalled() {
        diagnosticsWrapper.readDeviceDiagnostics(listenerMock);
        Runnable runnable = captureInternalHandlerRunnable();

        runnable.run();

        verify(capabilityMock).readDeviceDiagnostics(isA(SHNMapResultListener.class));
    }

    @Test
    public void shouldPostReadMethodOnListenerOnUserHandler_WhenReadIsCalled() {
        diagnosticsWrapper.readDeviceDiagnostics(listenerMock);
        Runnable internalRunnable = captureInternalHandlerRunnable();
        SHNMapResultListener<String, String> mapResultListener = captureMapResultListener(internalRunnable);
        mapResultListener.onActionCompleted(TEST_MAP, TEST_RESULT);
        Runnable userRunnable = captureUserHandlerRunnable();

        userRunnable.run();

        verify(listenerMock).onActionCompleted(TEST_MAP, TEST_RESULT);
    }

    // -------------------------

    private SHNMapResultListener<String, String> captureMapResultListener(final Runnable runnable) {
        runnable.run();

        verify(capabilityMock).readDeviceDiagnostics(mapResultListenerCaptor.capture());
        return mapResultListenerCaptor.getValue();
    }
}