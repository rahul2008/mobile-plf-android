package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PersistentStorageFactoryTest {

    public static final String TEST_ADDRESS = "TEST_ADDRESS";
    @Mock
    private Context context;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private SHNDevice shnDeviceMock;

    @Mock
    private PersistentStorage persistentStorageMock;

    private PersistentStorageFactory persistentStorageFactory;

    List<String> keyList = new ArrayList<>();

    @Before
    public void setUp() {
        initMocks(this);

        when(shnCentralMock.getApplicationContext()).thenReturn(context);
        when(shnDeviceMock.getAddress()).thenReturn(TEST_ADDRESS);

        persistentStorageFactory = new TestPersistentStorageFactory(shnCentralMock);
    }

    @Test
    public void ShouldCreateRootPreferences_WhenGetPersistentStorageIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorage();

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.SANDBOX_KEY);
    }

    @Test
    public void ShouldCreateUserPreferences_WhenGetPersistentStorageForUserIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorageForUser();

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.USER_KEY);
    }

    @Test
    public void ShouldCreateDevicePreferences_WhenGetPersistentStorageForDeviceIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorageForDevice(shnDeviceMock);

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY);
        assertThat(keyList.get(1)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS);
    }

    @Test
    public void ShouldSaveKeyForDevicePreferences_WhenGetPersistentStorageForDeviceIsCalled() {
        persistentStorageFactory.getPersistentStorageForDevice(shnDeviceMock);

        Set<String> keySet = getKeySet(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS);
        verify(persistentStorageMock).put(PersistentStorageFactory.DEVICE_KEY, keySet);
    }

    @Test
    public void ShouldCreateDevicePreferences_WhenGetPersistentStorageForDeviceCapabilityIsCalled() {
        SHNCapabilityType capabilityType = SHNCapabilityType.CLEAR_USER_DATA;

        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorageForDeviceCapability(shnDeviceMock, capabilityType);

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY);
        assertThat(keyList.get(1)).isEqualTo(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS + capabilityType.name());
    }

    @Test
    public void ShouldSaveKeyForDevicePreferences_WhenGetPersistentStorageForDeviceCapabilityIsCalled() {
        SHNCapabilityType capabilityType = SHNCapabilityType.CLEAR_USER_DATA;

        persistentStorageFactory.getPersistentStorageForDeviceCapability(shnDeviceMock, capabilityType);

        Set<String> keySet = getKeySet(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS + capabilityType.name());
        verify(persistentStorageMock).put(PersistentStorageFactory.DEVICE_KEY, keySet);
    }

    @Test
    public void ShouldNotRenewKeystore_WhenKeyIsAlreadyPresent() {
        Set<String> keySet = getKeySet(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.SANDBOX_KEY);
        when(persistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_KEY), anySet())).thenReturn(keySet);

        persistentStorageFactory.getPersistentStorage();

        verify(persistentStorageMock, never()).put(PersistentStorageFactory.DEVICE_KEY, keySet);
    }

    @Test
    public void ShouldClearUserData_WhenAsked() {
        persistentStorageFactory.clearUserData();

        verify(persistentStorageMock).clear();
    }

    @Test
    public void ShouldClearDevicePreferences_WhenClearIsCalledForADevice() {
        String key1 = PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS;
        String key2 = PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + "SomeOtherAddress";
        when(persistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_KEY), anySet())).thenReturn(getKeySet(key1, key2));

        persistentStorageFactory.clearDeviceData(shnDeviceMock);

        verify(persistentStorageMock).clear();

        verify(persistentStorageMock).put(PersistentStorageFactory.DEVICE_KEY, getKeySet(key2));
    }

    @Test
    public void ShouldClearAllDevicePreferences_WhenClearAllIsCalled() {
        String key1 = PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + TEST_ADDRESS;
        String key2 = PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY + "SomeOtherAddress";
        when(persistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_KEY), anySet())).thenReturn(getKeySet(key1, key2));

        persistentStorageFactory.clearAllDevices();

        verify(persistentStorageMock, times(3)).clear();

        assertThat(keyList).hasSize(3);
        assertThat(keyList).contains(PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY, key1, key2);
    }

    @Test
    public void ShouldClearAllData_WhenAsked() {
        persistentStorageFactory.clearAllData();

        assertThat(keyList).hasSize(3);
        assertThat(keyList).contains(
                PersistentStorageFactory.SHINELIB + PersistentStorageFactory.DEVICE_KEY,
                PersistentStorageFactory.SHINELIB + PersistentStorageFactory.USER_KEY,
                PersistentStorageFactory.SHINELIB + PersistentStorageFactory.SANDBOX_KEY);

        verify(persistentStorageMock, times(3)).clear();
    }

    // -----------

    @NonNull
    private Set<String> getKeySet(final String... keys) {
        Set<String> keySet = new HashSet<>();
        Collections.addAll(keySet, keys);
        return keySet;
    }

    private class TestPersistentStorageFactory extends PersistentStorageFactory {

        public TestPersistentStorageFactory(@NonNull final SHNCentral shnCentral) {
            super(shnCentral);
        }

        @NonNull
        @Override
        protected PersistentStorage createPersistentStorage(@NonNull final String key) {
            keyList.add(key);
            return persistentStorageMock;
        }
    }
}