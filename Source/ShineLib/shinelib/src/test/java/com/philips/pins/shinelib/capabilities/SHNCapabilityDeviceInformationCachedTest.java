package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;
import com.philips.pins.shinelib.utility.DeviceInformationCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.Date;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityDeviceInformationCachedTest {

    public static final SHNCapabilityDeviceInformation.SHNDeviceInformationType INFORMATION_TYPE = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final Date TEST_DATE = new Date();

    @Mock
    private SHNServiceDeviceInformation deviceInformationMock;

    @Mock
    private DeviceInformationCache cacheMock;

    @Mock
    private SHNCapabilityDeviceInformation.Listener deviceInformationListenerMock;

    @Captor
    private ArgumentCaptor<SHNCapabilityDeviceInformation.Listener> deviceInformationListenerCaptor;

    private SHNCapabilityDeviceInformationCached deviceInformationCached;

    @Before
    public void setUp() throws ParseException {
        initMocks(this);

        deviceInformationCached = new SHNCapabilityDeviceInformationCached(deviceInformationMock, cacheMock);
    }

    @Test
    public void shouldInformListener_whenDeviceInformationIsReceived() {
        deviceInformationCached.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(deviceInformationMock).readDeviceInformation(eq(INFORMATION_TYPE), deviceInformationListenerCaptor.capture());
        SHNCapabilityDeviceInformation.Listener listener = deviceInformationListenerCaptor.getValue();

        listener.onDeviceInformation(INFORMATION_TYPE, TEST_MESSAGE, TEST_DATE);

        verify(deviceInformationListenerMock).onDeviceInformation(INFORMATION_TYPE, TEST_MESSAGE, TEST_DATE);
    }

    @Test
    public void shouldCacheInformation_whenDeviceInformationIsReceived() {
        deviceInformationCached.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(deviceInformationMock).readDeviceInformation(eq(INFORMATION_TYPE), deviceInformationListenerCaptor.capture());
        SHNCapabilityDeviceInformation.Listener listener = deviceInformationListenerCaptor.getValue();

        listener.onDeviceInformation(INFORMATION_TYPE, TEST_MESSAGE, TEST_DATE);

        verify(cacheMock).save(INFORMATION_TYPE, TEST_MESSAGE);
    }

    @Test
    public void shouldInformListener_whenErrorIsReceived_AndNoCachedData() {
        deviceInformationCached.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(deviceInformationMock).readDeviceInformation(eq(INFORMATION_TYPE), deviceInformationListenerCaptor.capture());
        SHNCapabilityDeviceInformation.Listener listener = deviceInformationListenerCaptor.getValue();

        SHNResult result = SHNResult.SHNErrorBluetoothDisabled;
        listener.onError(INFORMATION_TYPE, result);

        verify(deviceInformationListenerMock).onError(INFORMATION_TYPE, result);
    }

    @Test
    public void shouldInformListener_whenErrorIsReceived_AndHasCachedData() {
        deviceInformationCached.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(deviceInformationMock).readDeviceInformation(eq(INFORMATION_TYPE), deviceInformationListenerCaptor.capture());
        SHNCapabilityDeviceInformation.Listener listener = deviceInformationListenerCaptor.getValue();

        when(cacheMock.getValue(INFORMATION_TYPE)).thenReturn(TEST_MESSAGE);
        when(cacheMock.getDate(INFORMATION_TYPE)).thenReturn(TEST_DATE);

        SHNResult result = SHNResult.SHNErrorBluetoothDisabled;
        listener.onError(INFORMATION_TYPE, result);

        verify(deviceInformationListenerMock).onDeviceInformation(INFORMATION_TYPE, TEST_MESSAGE, TEST_DATE);
    }
}
