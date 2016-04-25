package com.philips.pins.shinelib.dicommsupport;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommPortTest {

    @Mock
    private DiCommChannel diCommChannelMock;

    @Mock
    private DiCommPort.Listener diCommPortListenerMock;

    @Captor
    private ArgumentCaptor<SHNMapResultListener<String, Object>> mapResultListenerArgumentCaptor;

    public static final String PORT_NAME = "Port";
    private DiCommPort diCommPort;
    private Map<String, Object> properties = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        diCommPort = new DiCommPort(PORT_NAME);
        diCommPort.setListener(diCommPortListenerMock);

        properties.put("data", 5);
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommPort(PORT_NAME);
    }

    @Test
    public void whenCreatedThenNotAvailable() throws Exception {
        assertFalse(diCommPort.isAvailable());
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
}