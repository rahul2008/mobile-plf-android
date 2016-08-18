package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommFirmwarePortStateWaiterTest{

    @Mock
    private DiCommFirmwarePort portMock;

    @Mock
    private DiCommFirmwarePortStateWaiter.Listener listenerMock;

    @Captor
    private ArgumentCaptor<DiCommPort.UpdateListener> updateListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter;
    private MockedHandler mockedHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedHandler = new MockedHandler();
        diCommFirmwarePortStateWaiter = new DiCommFirmwarePortStateWaiter(portMock, mockedHandler.getMock());
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommFirmwarePortStateWaiter(portMock, mockedHandler.getMock());
    }

    @Test
    public void whenPortIsInRequestedStateThenListenerIsNotified() throws Exception {
        when(portMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);

        diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Idle, listenerMock);

        verify(listenerMock).onStateUpdated(DiCommFirmwarePort.State.Idle, SHNResult.SHNOk);
    }

    @Test
    public void whenPortIsInNotRequestedStateThenSubscribeIsCalled() throws Exception {
        when(portMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);

        diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Downloading, listenerMock);

        verify(portMock).subscribe(updateListenerArgumentCaptor.capture(), resultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenSubscriptionIsNotSuccessfulThenListenerIsNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNErrorConnectionLost);

        verify(listenerMock).onStateUpdated(null, SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        verify(listenerMock, never()).onStateUpdated(any(DiCommFirmwarePort.State.class), any(SHNResult.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithNoStateUpdateThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();
        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        Map<String, Object> properties = new HashMap<>();
        properties.put("canupdate", "true");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);

        verify(listenerMock, never()).onStateUpdated(any(DiCommFirmwarePort.State.class), any(SHNResult.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithRequestedStateUpdateThenListenerIsNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();
        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        Map<String, Object> properties = new HashMap<>();
        properties.put("state", "downloading");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(DiCommFirmwarePort.State.Downloading, SHNResult.SHNOk);
    }

    @Test
    public void whenPropertiesAreUpdatedWithUnexpectedStateUpdateThenListenerIsNotifiedWithAnError() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();
        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        Map<String, Object> properties = new HashMap<>();
        properties.put("state", "error");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(DiCommFirmwarePort.State.Error, SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenPropertiesAreUpdatedWithTransitionStateThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();
        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        Map<String, Object> properties = new HashMap<>();
        properties.put("state", "preparing");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);

        verify(listenerMock, never()).onStateUpdated(any(DiCommFirmwarePort.State.class), any(SHNResult.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithExpectedStateThenListenerIsNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();
        resultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        Map<String, Object> properties = new HashMap<>();
        properties.put("state", "preparing");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);
        mockedHandler.executeFirstScheduledExecution();

        properties.put("state", "downloading");
        updateListenerArgumentCaptor.getValue().onPropertiesChanged(properties);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(DiCommFirmwarePort.State.Downloading, SHNResult.SHNOk);
    }

    @Test
    public void whenPropertiesAreUpdatedWithRequiredStateThenUnSubscribeIsCalled() throws Exception {
        whenPropertiesAreUpdatedWithRequestedStateUpdateThenListenerIsNotified();

        verify(portMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithUnexpectedStateThenUnSubscribeIsCalled() throws Exception {
        whenPropertiesAreUpdatedWithUnexpectedStateUpdateThenListenerIsNotifiedWithAnError();

        verify(portMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void whenCancelIsCalledThenUnsubcribedIsCalled() throws Exception {
        diCommFirmwarePortStateWaiter.cancel();

        verify(portMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }
}