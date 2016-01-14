package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilitySedentary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SHNCapabilitySedentaryWrapperTest {

    private static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    private static final short EXPECTED_SEDENTARY_MINUTES = 60;
    private static final boolean EXPECTED_BOOLEAN = true;

    private SHNCapabilitySedentaryWrapper sedentaryWrapper;

    @Mock
    private SHNCapabilitySedentary capabilityMock;

    @Mock
    private ResultListener listenerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<ResultListener> resultListenerArgumentCaptor;

    private Handler internalHandlerMock;
    private Handler userHandlerMock;

    @Before
    public void setUp() {
        initMocks(this);

        internalHandlerMock = mock(Handler.class);
        userHandlerMock = mock(Handler.class);

        sedentaryWrapper = new SHNCapabilitySedentaryWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    private Runnable captureInternalHandlerRunnable() {
        verify(internalHandlerMock).post(runnableCaptor.capture());

        return runnableCaptor.getValue();
    }

    private Runnable captureUserHandlerRunnable() {
        verify(userHandlerMock).post(runnableCaptor.capture());

        return runnableCaptor.getValue();
    }

    @Test
    public void shouldPostSetSedentaryPeriodInMinutesMethodOnCapabilityOnInternalHandler_WhenSetSedentaryNotificationEnabledIsCalled() {
        sedentaryWrapper.setSedentaryPeriodInMinutes(EXPECTED_SEDENTARY_MINUTES, listenerMock);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setSedentaryPeriodInMinutes(eq(EXPECTED_SEDENTARY_MINUTES), isA(ResultListener.class));
    }

    @Test
    public void shouldNotifyTheSetSedentaryPeriodInMinutesListenerOnUserThread_WhenResponseIsReceived() {
        sedentaryWrapper.setSedentaryPeriodInMinutes(EXPECTED_SEDENTARY_MINUTES, listenerMock);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setSedentaryPeriodInMinutes(eq(EXPECTED_SEDENTARY_MINUTES), resultListenerArgumentCaptor.capture());
        resultListenerArgumentCaptor.getValue().onActionCompleted(EXPECTED_SEDENTARY_MINUTES, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(listenerMock).onActionCompleted(EXPECTED_SEDENTARY_MINUTES, EXPECTED_RESULT);
    }

    @Test
    public void shouldPostGetSedentaryPeriodInMinutesMethodOnCapabilityOnInternalHandler_WhenSetSedentaryNotificationEnabledIsCalled() {
        sedentaryWrapper.getSedentaryPeriodInMinutes(listenerMock);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSedentaryPeriodInMinutes(isA(ResultListener.class));
    }

    @Test
    public void shouldNotifyTheGetSedentaryPeriodInMinutesListenerOnUserThread_WhenResponseIsReceived() {
        sedentaryWrapper.getSedentaryPeriodInMinutes(listenerMock);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSedentaryPeriodInMinutes(resultListenerArgumentCaptor.capture());
        resultListenerArgumentCaptor.getValue().onActionCompleted(EXPECTED_SEDENTARY_MINUTES, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(listenerMock).onActionCompleted(EXPECTED_SEDENTARY_MINUTES, EXPECTED_RESULT);
    }

    @Test
    public void shouldPostSetSedentaryNotificationEnabledMethodOnCapabilityOnInternalHandler_WhenSetSedentaryNotificationEnabledIsCalled() {
        sedentaryWrapper.setSedentaryNotificationEnabled(EXPECTED_BOOLEAN, listenerMock);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setSedentaryNotificationEnabled(eq(EXPECTED_BOOLEAN), isA(ResultListener.class));
    }

    @Test
    public void shouldNotifyTheSetSedentaryNotificationEnabledListenerOnUserThread_WhenResponseIsReceived() {
        sedentaryWrapper.setSedentaryNotificationEnabled(EXPECTED_BOOLEAN, listenerMock);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setSedentaryNotificationEnabled(eq(true), resultListenerArgumentCaptor.capture());
        resultListenerArgumentCaptor.getValue().onActionCompleted(EXPECTED_BOOLEAN, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(listenerMock).onActionCompleted(EXPECTED_BOOLEAN, EXPECTED_RESULT);
    }

    @Test
    public void shouldPostGetSedentaryNotificationEnabledMethodOnCapabilityOnInternalHandler_WhenSetSedentaryNotificationEnabledIsCalled() {
        sedentaryWrapper.getSedentaryNotificationEnabled(listenerMock);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSedentaryNotificationEnabled(isA(ResultListener.class));
    }

    @Test
    public void shouldNotifyTheGetSedentaryNotificationEnabledListenerOnUserThread_WhenResponseIsReceived() {
        sedentaryWrapper.getSedentaryNotificationEnabled(listenerMock);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSedentaryNotificationEnabled(resultListenerArgumentCaptor.capture());
        resultListenerArgumentCaptor.getValue().onActionCompleted(EXPECTED_BOOLEAN, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(listenerMock).onActionCompleted(EXPECTED_BOOLEAN, EXPECTED_RESULT);
    }
}