package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityWrapperTestBase {

    @Mock
    protected Handler internalHandlerMock;

    @Mock
    protected Handler userHandlerMock;

    @Captor
    protected ArgumentCaptor<Runnable> runnableCaptor;

    protected Runnable captureInternalHandlerRunnable() {
        verify(internalHandlerMock).post(runnableCaptor.capture());

        return runnableCaptor.getValue();
    }

    protected Runnable captureUserHandlerRunnable() {
        verify(userHandlerMock).post(runnableCaptor.capture());

        return runnableCaptor.getValue();
    }

    @Before
    public void setUp() {
        initMocks(this);
    }
}
