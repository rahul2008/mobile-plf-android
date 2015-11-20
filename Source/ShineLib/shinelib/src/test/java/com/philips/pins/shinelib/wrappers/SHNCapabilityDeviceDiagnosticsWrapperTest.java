/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

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

    private Handler internalHandlerMock;
    private Handler userHandlerMock;

    @Before
    public void setUp() {
        initMocks(this);

        internalHandlerMock = mock(Handler.class);
        userHandlerMock = mock(Handler.class);

        diagnosticsWrapper = new SHNCapabilityDeviceDiagnosticsWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldPostReadMethodOnInternalHandler_WhenReadIsCalled() {
        diagnosticsWrapper.readDeviceDiagnostics(listenerMock);
        verify(internalHandlerMock).post(runnableCaptor.capture());

        Runnable runnable = runnableCaptor.getValue();
        runnable.run();

        verify(capabilityMock).readDeviceDiagnostics(isA(SHNMapResultListener.class));
    }

}