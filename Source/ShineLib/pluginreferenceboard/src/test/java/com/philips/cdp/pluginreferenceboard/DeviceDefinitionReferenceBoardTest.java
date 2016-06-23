package com.philips.cdp.pluginreferenceboard;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.services.SHNServiceBattery;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;
import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

public class DeviceDefinitionReferenceBoardTest {

    @Mock
    DeviceDefinitionReferenceBoardFactory deviceDefinitionReferenceBoardFactory;

    @Mock
    SHNCentral centralMock;

    @Mock
    SHNDeviceDefinitionInfo deviceDefinitionInfoMock;

    @Mock
    SHNDeviceImpl deviceMock;

    @Mock
    SHNServiceBattery batteryServiceMock;

    @Mock
    SHNCapabilityBattery batteryCapabilityMock;

    @Mock
    SHNServiceDeviceInformation deviceInformationServiceMock;

    @Mock
    SHNCapabilityDeviceInformation deviceInformationCapabilityMock;

    @Mock
    PersistentStorageFactory persistentStorageFactoryMock;

    @Mock
    PersistentStorage persistentStorageMock;

    private static final String MAC_ADDRESS = "12-34-56-78-9A-BC";
    DeviceDefinitionReferenceBoard deviceDefinitionReferenceBoard;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(centralMock.getPersistentStorageFactory()).thenReturn(persistentStorageFactoryMock);
        when(persistentStorageFactoryMock.getPersistentStorageForDevice(deviceMock)).thenReturn(persistentStorageMock);

        when(deviceDefinitionReferenceBoardFactory.createDevice(eq(MAC_ADDRESS), eq(centralMock), anyString())).thenReturn(deviceMock);

        when(deviceDefinitionReferenceBoardFactory.createBatteryService()).thenReturn(batteryServiceMock);
        when(deviceDefinitionReferenceBoardFactory.createDeviceInformationService()).thenReturn(deviceInformationServiceMock);

        when(deviceDefinitionReferenceBoardFactory.createBatteryCapability(batteryServiceMock)).thenReturn(batteryCapabilityMock);
        when(deviceDefinitionReferenceBoardFactory.createDeviceInformationCapability(deviceInformationServiceMock)).thenReturn(deviceInformationCapabilityMock);

        deviceDefinitionReferenceBoard = new DeviceDefinitionReferenceBoard(deviceDefinitionReferenceBoardFactory);
    }

    @Test
    public void shouldSpecifySHNDevice() throws Exception {
        assertNotNull(deviceDefinitionReferenceBoard.createDeviceFromDeviceAddress(MAC_ADDRESS, deviceDefinitionInfoMock, centralMock));
    }

    @Test
    public void shouldReturnWrappedSHNDevice() throws Exception {
        assertTrue(deviceDefinitionReferenceBoard.createDeviceFromDeviceAddress(MAC_ADDRESS, deviceDefinitionInfoMock, centralMock) instanceof SHNDeviceWrapper);
    }

    @Test
    public void shouldRegisterBatteryCapability() throws Exception {
        deviceDefinitionReferenceBoard.createDeviceFromDeviceAddress(MAC_ADDRESS, deviceDefinitionInfoMock, centralMock);

        verify(deviceDefinitionReferenceBoardFactory).createBatteryService();
        verify(deviceDefinitionReferenceBoardFactory).createBatteryCapability(batteryServiceMock);
        verify(deviceMock).registerCapability(batteryCapabilityMock, SHNCapabilityType.BATTERY);
    }

    @Test
    public void shouldRegisterDeviceInformationCapability() throws Exception {
        deviceDefinitionReferenceBoard.createDeviceFromDeviceAddress(MAC_ADDRESS, deviceDefinitionInfoMock, centralMock);

        verify(deviceDefinitionReferenceBoardFactory).createDeviceInformationService();
        verify(deviceDefinitionReferenceBoardFactory).createDeviceInformationCapability(deviceInformationServiceMock);

        verify(deviceMock).registerCapability(any(SHNCapabilityDeviceInformation.class), eq(SHNCapabilityType.DEVICE_INFORMATION));
    }
}