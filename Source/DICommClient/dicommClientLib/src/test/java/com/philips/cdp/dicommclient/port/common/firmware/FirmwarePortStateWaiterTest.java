/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

import com.google.gson.Gson;
import com.philips.cdp.dicommclient.helper.MockedHandler;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.GsonProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwarePortStateWaiterTest {
    @Mock
    private FirmwarePort portMock;

    @Mock
    private FirmwarePortProperties portPropertiesMock;

    @Mock
    private FirmwarePortStateWaiter.Listener listenerMock;

    @Captor
    private ArgumentCaptor<DICommPortListener<FirmwarePort>> updateListenerArgumentCaptor;

    private Gson gson = GsonProvider.get();

    private FirmwarePortStateWaiter firmwarePortStateWaiter;
    private MockedHandler mockedHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        mockedHandler = new MockedHandler();
        firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, mockedHandler.getMock());

        when(portMock.getPortProperties()).thenReturn(portPropertiesMock);
    }

    @Test
    public void canCreate() throws Exception {
        new FirmwarePortStateWaiter(portMock, mockedHandler.getMock());
    }

    @Test
    public void whenPortIsInRequestedStateThenListenerIsNotified() throws Exception {
        when(portPropertiesMock.getState()).thenReturn(FirmwarePortProperties.FirmwareState.IDLE);

        firmwarePortStateWaiter.waitUntilStateIsReached(FirmwarePortProperties.FirmwareState.IDLE, listenerMock);

        verify(listenerMock).onStateUpdated(FirmwarePortProperties.FirmwareState.IDLE, FirmwarePortStateWaiter.Result.Ok);
    }

    @Test
    public void whenPortIsInNotRequestedStateThenSubscribeIsCalled() throws Exception {
        when(portPropertiesMock.getState()).thenReturn(FirmwarePortProperties.FirmwareState.IDLE);

        firmwarePortStateWaiter.waitUntilStateIsReached(FirmwarePortProperties.FirmwareState.DOWNLOADING, listenerMock);

        verify(portMock).subscribe();
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        firmwarePortStateWaiter.onPortUpdate(portMock);

        verify(listenerMock, never()).onStateUpdated(any(FirmwarePortProperties.FirmwareState.class), any(FirmwarePortStateWaiter.Result.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithNoStateUpdateThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        String json = "{\"canupdate\": true}";

        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);

        verify(listenerMock, never()).onStateUpdated(any(FirmwarePortProperties.FirmwareState.class), any(FirmwarePortStateWaiter.Result.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithRequestedStateUpdateThenListenerIsNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        String json = "{\"state\": \"downloading\"}";
        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(FirmwarePortProperties.FirmwareState.DOWNLOADING, FirmwarePortStateWaiter.Result.Ok);
    }

    @Test
    public void whenPropertiesAreUpdatedWithUnexpectedStateUpdateThenListenerIsNotifiedWithAnError() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        String json = "{\"state\": \"error\"}";
        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(FirmwarePortProperties.FirmwareState.ERROR, FirmwarePortStateWaiter.Result.InvalidState);
    }

    @Test
    public void whenPropertiesAreUpdatedWithTransitionStateThenListenerIsNotNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        String json = "{\"state\": \"preparing\"}";
        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);

        verify(listenerMock, never()).onStateUpdated(any(FirmwarePortProperties.FirmwareState.class), any(FirmwarePortStateWaiter.Result.class));
    }

    @Test
    public void whenPropertiesAreUpdatedWithExpectedStateThenListenerIsNotified() throws Exception {
        whenPortIsInNotRequestedStateThenSubscribeIsCalled();

        String json = "{\"state\": \"preparing\"}";
        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);
        mockedHandler.executeFirstScheduledExecution();

        json = "{\"state\": \"downloading\"}";
        when(portMock.getPortProperties()).thenReturn(gson.fromJson(json, FirmwarePortProperties.class));
        firmwarePortStateWaiter.onPortUpdate(portMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(listenerMock).onStateUpdated(FirmwarePortProperties.FirmwareState.DOWNLOADING, FirmwarePortStateWaiter.Result.Ok);
    }

    @Test
    public void whenPropertiesAreUpdatedWithRequiredStateThenUnSubscribeIsCalled() throws Exception {
        whenPropertiesAreUpdatedWithRequestedStateUpdateThenListenerIsNotified();

        verify(portMock).unsubscribe();
    }

    @Test
    public void whenPropertiesAreUpdatedWithUnexpectedStateThenUnSubscribeIsCalled() throws Exception {
        whenPropertiesAreUpdatedWithUnexpectedStateUpdateThenListenerIsNotifiedWithAnError();

        verify(portMock).unsubscribe();
    }

    @Test
    public void whenCancelIsCalledThenUnsubcribedIsCalled() throws Exception {
        firmwarePortStateWaiter.cancel();

        verify(portMock).unsubscribe();
    }
}
