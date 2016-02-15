package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

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

        persistentStorageFactory = new TestPersistentStorageFactory(context);
    }

    @Test
    public void ShouldCreateRootPreferences_WhenGetPersistentStorageIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorage();

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.SHINELIB_KEY);
    }

    @Test
    public void ShouldCreateUserPreferences_WhenGetPersistentStorageForUserIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorageForUser();

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.USER_KEY);
    }

    @Test
    public void ShouldCreateDevicePreferences_WhenGetPersistentStorageForDeviceIsCalled() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorageForDevice(shnDeviceMock);

        assertThat(persistentStorage).isSameAs(persistentStorageMock);
        assertThat(keyList.get(0)).isEqualTo(PersistentStorageFactory.DEVICE_ADDRESS_KEY);
        assertThat(keyList.get(1)).isEqualTo(TEST_ADDRESS + PersistentStorageFactory.DEVICE_KEY);
    }

    @Test
    public void ShouldSaveAddress_WhenGetPersistentStorageForDeviceIsCalled() {
        persistentStorageFactory.getPersistentStorageForDevice(shnDeviceMock);

        Set<String> keySet = getKeySet(TEST_ADDRESS);
        verify(persistentStorageMock).put(PersistentStorageFactory.DEVICE_ADDRESS_KEY, keySet);
    }

    @Test
    public void ShouldNotRenewKeystore_WhenKeyIsAlreadyPresent() {
        Set<String> keySet = getKeySet(PersistentStorageFactory.SHINELIB_KEY);
        when(persistentStorageMock.getStringSet(eq(PersistentStorageFactory.DEVICE_KEY), anySet())).thenReturn(keySet);

        persistentStorageFactory.getPersistentStorage();

        verify(persistentStorageMock, never()).put(PersistentStorageFactory.DEVICE_KEY, keySet);
    }

    // -----------

    @NonNull
    private Set<String> getKeySet(final String... keys) {
        Set<String> keySet = new HashSet<>();
        Collections.addAll(keySet, keys);
        return keySet;
    }

    private class TestPersistentStorageFactory extends PersistentStorageFactory {

        public TestPersistentStorageFactory(@NonNull final Context context) {
            super(context);
        }

        @NonNull
        @Override
        PersistentStorage createPersistentStorage(@NonNull final String key) {
            keyList.add(key);
            return persistentStorageMock;
        }
    }
}