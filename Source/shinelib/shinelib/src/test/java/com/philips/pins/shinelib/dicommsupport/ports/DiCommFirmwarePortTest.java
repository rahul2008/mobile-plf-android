package com.philips.pins.shinelib.dicommsupport.ports;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.dicommsupport.DiCommChannel;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommFirmwarePortTest {
    @Mock
    private DiCommChannel diCommChannelMock;

    @Captor
    private ArgumentCaptor<SHNMapResultListener> mapResultListenerArgumentCaptor;

    private Map<String, Object> properties = new HashMap<>();
    private DiCommFirmwarePort diCommFirmwarePort;
    private MockedHandler mockedHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockedHandler = new MockedHandler();
        diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());
    }

    @Test
    public void canConvertStringToAKnownState() throws Exception {
        assertEquals(DiCommFirmwarePort.State.Ready, DiCommFirmwarePort.State.fromString("ready"));
    }

    @Test
    public void canConvertUpperCaseStringToAKnownState() throws Exception {
        assertEquals(DiCommFirmwarePort.State.Ready, DiCommFirmwarePort.State.fromString("Ready"));
    }

    @Test
    public void whenStringIsNotAValidStateThenStateIsUnknown() throws Exception {
        assertEquals(DiCommFirmwarePort.State.Unknown, DiCommFirmwarePort.State.fromString("notAState"));
    }

    @Test
    public void canConvertCommandToAString() throws Exception {
        assertEquals("go", DiCommFirmwarePort.Command.DeployGo.getName());
    }

    @Test
    public void whenCreatedThenStateIsUnknown() throws Exception {
        DiCommFirmwarePort diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());

        assertEquals(DiCommFirmwarePort.State.Unknown, diCommFirmwarePort.getState());
    }

    private void reloadProperties(String key, Object data) {
        diCommFirmwarePort.setDiCommChannel(diCommChannelMock);
        diCommFirmwarePort.onChannelAvailabilityChanged(true);
        verify(diCommChannelMock).reloadProperties(eq(DiCommFirmwarePort.FIRMWARE), mapResultListenerArgumentCaptor.capture());

        properties.put(key, data);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);
    }

    @Test
    public void whenPropertiesAreReceivedThenStateIsUpdated() throws Exception {
        reloadProperties("state", "idle");

        assertEquals(DiCommFirmwarePort.State.Idle, diCommFirmwarePort.getState());
    }

    @Test
    public void whenCreatedThenMaxChunkSizeIsNotDefined() throws Exception {
        DiCommFirmwarePort diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());

        assertEquals(Integer.MAX_VALUE, diCommFirmwarePort.getMaxChunkSize());
    }

    @Test
    public void whenPropertiesAreReceivedThenMaxChunkSizeIsUpdated() throws Exception {
        reloadProperties("maxchunksize", 255);

        int expected = (int) (255 * 0.75);
        assertEquals(expected, diCommFirmwarePort.getMaxChunkSize());
    }

    @Test
    public void whenCreatedThenStatusMessageIsUndefined() throws Exception {
        DiCommFirmwarePort diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());

        assertNull(diCommFirmwarePort.getStatusMessage());
    }

    @Test
    public void whenPropertiesAreReceivedThenStatusMessageIsUpdated() throws Exception {
        String data = "Error downloading";
        reloadProperties("statusmsg", data);

        assertEquals(data, diCommFirmwarePort.getStatusMessage());
    }

    @Test
    public void whenCreatedThenUpgradeIsUndefined() throws Exception {
        DiCommFirmwarePort diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());

        assertNull(diCommFirmwarePort.getUploadedUpgradeVersion());
    }

    @Test
    public void whenPropertiesAreReceivedThenUpgradeIsUpdated() throws Exception {
        String data = "Latest";
        reloadProperties("upgrade", data);

        assertEquals(data, diCommFirmwarePort.getUploadedUpgradeVersion());
    }

    @Test
    public void whenCreatedThenCanUpgradeIsUndefined() throws Exception {
        DiCommFirmwarePort diCommFirmwarePort = new DiCommFirmwarePort(mockedHandler.getMock());

        assertFalse(diCommFirmwarePort.getCanUpgrade());
    }

    @Test
    public void whenPropertiesAreReceivedThenCanUpgradeIsUpdated() throws Exception {
        String data = "true";
        reloadProperties("canupgrade", data);

        assertTrue(data, diCommFirmwarePort.getCanUpgrade());
    }

    @Test
    public void whenPropertiesAreReceivedWithInvalidValueThenCanUpgradeIsFalse() throws Exception {
        String data = "gtrue";
        reloadProperties("canupgrade", data);

        assertFalse(data, diCommFirmwarePort.getCanUpgrade());
    }
}