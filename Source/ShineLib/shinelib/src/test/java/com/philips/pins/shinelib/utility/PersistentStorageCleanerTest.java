package com.philips.pins.shinelib.utility;

import com.philips.pins.shinelib.SHNDevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;

import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PersistentStorageCleanerTest {

    public static final String TEST_ADDRESS = "TEST_ADDRESS";
    @Mock
    private PersistentStorageFactory persistentStorageFactoryMock;

    @Mock
    private PersistentStorage userPersistentStorageMock;

    @Mock
    private PersistentStorage devicePersistentStorageMock;

    @Mock
    private PersistentStorage addressesPersistentStorageMock;

    @Mock
    private SHNDevice deviceMock;

    private PersistentStorageCleaner persistentStorageCleaner;

    @Before
    public void setUp() {
        initMocks(this);

        when(deviceMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(persistentStorageFactoryMock.getPersistentStorageForUser()).thenReturn(userPersistentStorageMock);
        when(persistentStorageFactoryMock.getPersistentStorageForDevice(TEST_ADDRESS)).thenReturn(devicePersistentStorageMock);
        when(persistentStorageFactoryMock.getPersistentStorageForDeviceAddresses()).thenReturn(addressesPersistentStorageMock);

        persistentStorageCleaner = new PersistentStorageCleaner(persistentStorageFactoryMock);
    }

    @Test
    public void ShouldClearUserData_WhenAsked() {
        persistentStorageCleaner.clearUserData();

        verify(userPersistentStorageMock).clear();
    }

    @Test
    public void ShouldClearDeviceData_WhenAsked() {
        persistentStorageCleaner.clearDeviceData(deviceMock);

        verify(devicePersistentStorageMock).clear();
    }

    @Test
    public void ShouldRemoveKeyFromAddressList_WhenClearDeviceDataIsCalled() {
        HashSet<String> inputAddresses = new HashSet<>();
        inputAddresses.add(TEST_ADDRESS);
        inputAddresses.add("SOME_OTHER_ADDRESS");

        HashSet<String> outputAddress = new HashSet<>();
        outputAddress.add("SOME_OTHER_ADDRESS");

        when(addressesPersistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_ADDRESS_KEY), anySetOf(String.class))).thenReturn(inputAddresses);

        persistentStorageCleaner.clearDeviceData(deviceMock);

        verify(addressesPersistentStorageMock).put(PersistentStorageFactory.DEVICE_ADDRESS_KEY, outputAddress);
    }

    @Test
    public void ShouldClearListOfDeviceKeys_WhenClearAllDevicesIsCalled() {
        persistentStorageCleaner.clearAllDevices();

        verify(addressesPersistentStorageMock).clear();
    }

    @Test
    public void ShouldClearEachDeviceStorage_WhenClearAllDevicesIsCalled() {
        HashSet<String> inputAddresses = new HashSet<>();
        inputAddresses.add(TEST_ADDRESS);
        String someOtherAddress = "SOME_OTHER_ADDRESS";
        inputAddresses.add(someOtherAddress);

        PersistentStorage otherDevicePersistentStorageMock = mock(PersistentStorage.class);

        when(persistentStorageFactoryMock.getPersistentStorageForDevice(someOtherAddress)).thenReturn(otherDevicePersistentStorageMock);
        when(addressesPersistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_ADDRESS_KEY), anySetOf(String.class))).thenReturn(inputAddresses);

        persistentStorageCleaner.clearAllDevices();

        verify(devicePersistentStorageMock).clear();
        verify(otherDevicePersistentStorageMock).clear();
    }
}
