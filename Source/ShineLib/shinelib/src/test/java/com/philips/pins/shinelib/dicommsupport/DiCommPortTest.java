package com.philips.pins.shinelib.dicommsupport;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommPortTest {

    public static final String[] KEYS = {"data", "data1", "data2"};
    public static final int[] DATA = {5, 6, 7};

    @Mock
    private DiCommChannel diCommChannelMock;

    @Mock
    private DiCommPort.Listener diCommPortListenerMock;

    @Mock
    private DiCommPort.UpdateListener diCommUpdateListenerMock;

    @Mock
    private SHNMapResultListener<String, Object> mapResultListenerMock;

    @Mock
    private SHNResultListener resultListenerMock;

    @Captor
    private ArgumentCaptor<SHNMapResultListener<String, Object>> mapResultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;

    public static final String PORT_NAME = "Port";
    private DiCommPort diCommPort;
    private Map<String, Object> properties = new HashMap<>();
    private MockedHandler mockedHandler;
    private Map<String, Object> newProperties;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedHandler = new MockedHandler();
        Timer.setHandler(mockedHandler.getMock());

        diCommPort = new DiCommPort(PORT_NAME, mockedHandler.getMock());
        diCommPort.setListener(diCommPortListenerMock);
        diCommPort.setDiCommChannel(diCommChannelMock);

        properties.put(KEYS[0], DATA[0]);
        properties.put(KEYS[1], DATA[1]);
        properties.put(KEYS[2], DATA[2]);

        newProperties = new HashMap<>(properties);
        newProperties.put(KEYS[0], DATA[0] * 2);
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommPort(PORT_NAME, mockedHandler.getMock());
    }

    @Test
    public void whenCreatedThenNotAvailable() throws Exception {
        assertFalse(diCommPort.isAvailable());
    }

    @Test
    public void whenInitializedThenPropertiesAreEmpty() throws Exception {
        assertTrue(diCommPort.getProperties().isEmpty());
    }

    @Test
    public void whenChannelBecomesAvailableThenPortIsNotAvailable() throws Exception {
        diCommPort.onChannelAvailabilityChanged(true);

        assertFalse(diCommPort.isAvailable());
    }

    @Test
    public void whenChannelBecomesAvailableThenPortListenerIsNotNotified() throws Exception {
        diCommPort.onChannelAvailabilityChanged(true);

        verify(diCommPortListenerMock, never()).onPortAvailable(diCommPort);
    }

    @Test
    public void whenChannelBecomesAvailableAgainThenPropertiesAreRequested() throws Exception {
        diCommPort.setDiCommChannel(diCommChannelMock);
        diCommPort.onChannelAvailabilityChanged(true);

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPropertiesAreReportedWithResultOkThenChannelIsAvailable() throws Exception {
        whenChannelBecomesAvailableAgainThenPropertiesAreRequested();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        verify(diCommPortListenerMock).onPortAvailable(diCommPort);
        assertTrue(diCommPort.isAvailable());
    }

    @Test
    public void whenPropertiesAreReportedWithPropertiesAreStored() throws Exception {
        whenChannelBecomesAvailableAgainThenPropertiesAreRequested();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        assertEquals(properties, diCommPort.getProperties());
    }

    @Test
    public void whenPropertiesAreReportedWithResultNotOkThenChannelIsUnavailable() throws Exception {
        whenChannelBecomesAvailableAgainThenPropertiesAreRequested();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);

        verify(diCommPortListenerMock, never()).onPortAvailable(diCommPort);
        assertFalse(diCommPort.isAvailable());
    }

    @Test
    public void whenReloadPropertiesIsCalledWhileUnavailableThenSHNErrorInvalidStateIsReported() throws Exception {
        diCommPort.reloadProperties(mapResultListenerMock);

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPutPropertiesIsCalledWhileUnavailableThenSHNErrorInvalidStateIsReported() throws Exception {
        diCommPort.putProperties(properties, mapResultListenerMock);

        verify(diCommChannelMock).sendProperties(eq(properties), eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    private void goToAvailableState() {
        diCommPort.onChannelAvailabilityChanged(true);

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);
        reset(diCommChannelMock);
    }

    @Test
    public void whenReloadPropertiesWithNoChannelThenSHNErrorInvalidStateIsReported() throws Exception {
        goToAvailableState();
        diCommPort.setDiCommChannel(null);

        diCommPort.reloadProperties(mapResultListenerMock);

        verify(mapResultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenPutPropertiesWithNoChannelThenSHNErrorInvalidStateIsReported() throws Exception {
        goToAvailableState();
        diCommPort.setDiCommChannel(null);

        diCommPort.putProperties(properties, mapResultListenerMock);

        verify(mapResultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenReloadPropertiesIsCalledThenSendPropertiesIsCalledOnChannel() throws Exception {
        goToAvailableState();

        diCommPort.reloadProperties(mapResultListenerMock);

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenReloadPropertiesResultIsReceivedThenListenerIsNotified() throws Exception {
        whenReloadPropertiesIsCalledThenSendPropertiesIsCalledOnChannel();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        verify(mapResultListenerMock).onActionCompleted(properties, SHNResult.SHNOk);
    }

    @Test
    public void whenReloadPropertiesResultIsReceivedThenPropertiesAreUpdated() throws Exception {
        whenReloadPropertiesIsCalledThenSendPropertiesIsCalledOnChannel();

        Map<String, Object> properties = new HashMap<>();
        properties.put(KEYS[0], 1);
        properties.put(KEYS[1], 2);
        properties.put(KEYS[2], 3);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        assertEquals(properties, diCommPort.getProperties());
    }

    @Test
    public void whenPutPropertiesWhileUnavailableThenSendPropertiesISCalled() throws Exception {
        diCommPort.putProperties(properties, mapResultListenerMock);

        verify(diCommChannelMock).sendProperties(eq(properties), eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPutPropertiesIsCalledThenSendPropertiesIsCalledOnChannel() throws Exception {
        goToAvailableState();

        Map<String, Object> properties = new HashMap<>();
        diCommPort.putProperties(properties, mapResultListenerMock);

        verify(diCommChannelMock).sendProperties(eq(properties), eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPutPropertiesResultIsReceivedThenListenerIsNotified() throws Exception {
        whenPutPropertiesIsCalledThenSendPropertiesIsCalledOnChannel();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        verify(mapResultListenerMock).onActionCompleted(properties, SHNResult.SHNOk);
    }

    @Test
    public void whenPutPropertiesResultIsReceivedThenPropertiesAreUpdated() throws Exception {
        whenPutPropertiesIsCalledThenSendPropertiesIsCalledOnChannel();

        Map<String, Object> properties = new HashMap<>();
        properties.put(KEYS[0], 1);
        properties.put(KEYS[1], 2);
        properties.put(KEYS[2], 3);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        assertEquals(properties, diCommPort.getProperties());
    }

    @Test
    public void canSubscribeWhileUnavailable() throws Exception {
        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(resultListenerMock).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void canSubscribeWhenStateIsAvailable() throws Exception {
        goToAvailableState();

        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(resultListenerMock).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void canUnsubscribedWhileUnavailable() throws Exception {
        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        reset(resultListenerMock);
        diCommPort.unsubscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(resultListenerMock).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenUnsubscribedWhileNotSubscribedThenSHNErrorInvalidStateIsReported() throws Exception {
        goToAvailableState();

        diCommPort.unsubscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(resultListenerMock).onActionCompleted(SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenSubscribedWhenReloadPropertiesIsCalled() throws Exception {
        goToAvailableState();

        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenSubscribedThenContinuesReloadingProperties() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        reset(diCommChannelMock);
        assertEquals(1, mockedHandler.getScheduledExecutionCount());
        mockedHandler.executeFirstScheduledExecution();

        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenChannelBecomesUnavailableWhileSubscribedThenPollingIsStopped() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        diCommPort.setDiCommChannel(null);
        diCommPort.onChannelAvailabilityChanged(false);
        reset(diCommChannelMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        assertEquals(1, mockedHandler.getScheduledExecutionCount());
        reset(diCommChannelMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(diCommChannelMock, never()).reloadProperties(anyString(), any(SHNMapResultListener.class));
    }

    @Test
    public void whenChannelBecomesAvailableAgainWhileSubscribedThenPollingIsRestarted() throws Exception {
        whenChannelBecomesUnavailableWhileSubscribedThenPollingIsStopped();
        reset(diCommChannelMock);

        diCommPort.setDiCommChannel(diCommChannelMock);
        goToAvailableState();

        assertEquals(1, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenSubscribedTwiceThenNotifiedOnce() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(anyMap());
    }

    @Test
    public void whenSubscribedTwiceThenReloadPropertiesIsCalledOnce() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        reset(diCommChannelMock);

        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(diCommChannelMock, never()).reloadProperties(anyString(), any(SHNMapResultListener.class));
    }

    @Test
    public void whenSubscribedTwiceThenSubscriptionResultIsReported() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        reset(diCommChannelMock, resultListenerMock);

        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(resultListenerMock).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenUnsubscribedThenPollingIsStopped() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        diCommPort.unsubscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        assertEquals(0, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenThereIsASubscriptionThenPollingIsNotStopped() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        DiCommPort.UpdateListener diCommUpdateListenerMock2 = mock(DiCommPort.UpdateListener.class);
        diCommPort.subscribe(diCommUpdateListenerMock2, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        diCommPort.unsubscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        assertEquals(1, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenUnsubscribedThenPollingIsStopped2() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        diCommPort.unsubscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        assertEquals(0, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenSubscribedAgainThenPollingIsNotRestarted() throws Exception {
        whenSubscribedWhenReloadPropertiesIsCalled();
        reset(diCommChannelMock);

        DiCommPort.UpdateListener diCommUpdateListenerMock2 = mock(DiCommPort.UpdateListener.class);
        diCommPort.subscribe(diCommUpdateListenerMock2, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        verify(diCommChannelMock, never()).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());
    }

    private void verifyReloadPropertiesSent() {
        goToAvailableState();

        diCommPort.subscribe(diCommUpdateListenerMock, resultListenerMock);
        mockedHandler.executeFirstScheduledExecution();

        reset(diCommUpdateListenerMock);
        mockedHandler.executeFirstScheduledExecution();
        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        reset(diCommChannelMock);
    }

    @Test
    public void whenPropertiesHaveNotChangedThenUpdateListenerIsNotNotified() throws Exception {
        verifyReloadPropertiesSent();

        Map<String, Object> newProperties = new HashMap<>(properties);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock, never()).onPropertiesChanged(anyMap());
    }

    @Test
    public void whenOnePropertyHasChangedThenUpdateListenerIsNotified() throws Exception {
        verifyReloadPropertiesSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
    }

    @Test
    public void whenMultiplePropertiesHaveChangedThenUpdateListenerIsNotified() throws Exception {
        verifyReloadPropertiesSent();

        Map<String, Object> newProperties = new HashMap<>(properties);
        newProperties.put(KEYS[0], DATA[0] * 2);
        newProperties.put(KEYS[1], DATA[1] * 2);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(2, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[1]));
    }

    @Test
    public void whenPropertyValueChangesToExistingOneThenUpdateListenerIsNotified() throws Exception {
        verifyReloadPropertiesSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
    }

    @Test
    public void whenPropertyValueChangesFromNullThenListenerIsNotified() throws Exception {
        properties.put(KEYS[0], null);
        verifyReloadPropertiesSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
    }

    @Test
    public void whenReloadPropertyReturnsResultNotOkThenListenerIsNotified() throws Exception {
        goToAvailableState();
        diCommPort.reloadProperties(mapResultListenerMock);
        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(mapResultListenerMock).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenPutPropertyReturnsResultNotOkThenListenerIsNotified() throws Exception {
        goToAvailableState();
        diCommPort.putProperties(properties, mapResultListenerMock);
        verify(diCommChannelMock).sendProperties(eq(properties), eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(mapResultListenerMock).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenReloadPropertyReturnsResultThenSubscriptionListenerIsNotified() throws Exception {
        verifyReloadPropertiesSent();

        diCommPort.reloadProperties(mapResultListenerMock);
        verify(diCommChannelMock).reloadProperties(eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
    }

    @Test
    public void whenPutPropertyReturnsResultThenSubscriptionListenerIsNotified() throws Exception {
        verifyReloadPropertiesSent();

        diCommPort.putProperties(newProperties, mapResultListenerMock);
        verify(diCommChannelMock).sendProperties(eq(newProperties), eq(PORT_NAME), mapResultListenerArgumentCaptor.capture());

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(newProperties, SHNResult.SHNOk);

        verify(diCommUpdateListenerMock).onPropertiesChanged(mapArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertTrue(mapArgumentCaptor.getValue().containsKey(KEYS[0]));
    }
}