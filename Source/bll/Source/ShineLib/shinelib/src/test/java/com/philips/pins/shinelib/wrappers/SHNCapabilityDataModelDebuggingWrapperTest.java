package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataModelDebugging;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityDataModelDebuggingWrapperTest {

    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    @Mock
    private SHNCapabilityDataModelDebugging capability;

    @Mock
    private Handler internalHandler;

    @Mock
    private Handler userHandler;

    @Mock
    private ResultListener<String> outputListenerMock;

    @Captor
    private ArgumentCaptor<ResultListener<String>> resultListenerCaptor;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private SHNCapabilityDataModelDebuggingWrapper wrapper;

    @Before
    public void setUp() {
        initMocks(this);

        wrapper = new SHNCapabilityDataModelDebuggingWrapper(capability, internalHandler, userHandler);
    }

    @Test
    public void ShouldCallWriteInputOnInternalThread_WhenWriteOnWrapperIsCalled() {
        wrapper.writeInput(TEST_MESSAGE);

        verify(internalHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(capability).writeInput(TEST_MESSAGE);
    }

    @Test
    public void ShouldCallSetEnabledOnInternalThread_WhenSetEnabledOnWrapperIsCalled() {
        wrapper.setEnabled(true);

        verify(internalHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(capability).setEnabled(true);
    }

    @Test
    public void ShouldInformOutputListenerOnUserThread_WhenResultIsReceived() {
        wrapper.setOutputListener(outputListenerMock);

        verify(internalHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(capability).setOutputListener(resultListenerCaptor.capture());
        ResultListener<String> resultListener = resultListenerCaptor.getValue();

        resultListener.onActionCompleted(TEST_MESSAGE, SHNResult.SHNOk);

        verify(userHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(outputListenerMock).onActionCompleted(TEST_MESSAGE, SHNResult.SHNOk);
    }
}