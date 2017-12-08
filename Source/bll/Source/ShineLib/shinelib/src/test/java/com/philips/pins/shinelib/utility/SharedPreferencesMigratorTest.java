package com.philips.pins.shinelib.utility;

import com.philips.pins.shinelib.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SharedPreferencesMigratorTest extends RobolectricTest {

    public static final String KEY_1 = "KEY_1";
    public static final String KEY_2 = "KEY_2";
    public static final String KEY_3 = "KEY_3";
    public static final String KEY_4 = "KEY_4";

    public static final String TEST_DEVICE_1 = "TEST_DEVICE_1";
    public static final String TEST_DEVICE_2 = "TEST_DEVICE_2";

    public static final String VALUE_1_STRING = "VALUE_1_STRING";
    public static final int VALUE_2_INT = 222;

    @Mock
    private PersistentStorageFactory sourcePersistentStorageFactoryMock;

    @Mock
    private PersistentStorageFactory destinationPersistentStorageFactoryMock;

    @Mock
    private PersistentStorage persistentStorageMock;

    @Mock
    private PersistentStorage userPersistentStorageMock;

    @Mock
    private PersistentStorage deviceAdressesPersistentStorageMock;

    @Mock
    private PersistentStorage devicePersistentStorageMock;

    @Mock
    private PersistentStorageCleaner persistentStorageCleaner;

    private SharedPreferencesMigrator sharedPreferencesMigrator;

    private Map<String, Object> keyMap;

    private Set<String> deviceAddresses;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(sourcePersistentStorageFactoryMock.getPersistentStorageCleaner()).thenReturn(persistentStorageCleaner);

        when(sourcePersistentStorageFactoryMock.getPersistentStorageForUser()).thenReturn(userPersistentStorageMock);
        when(sourcePersistentStorageFactoryMock.getPersistentStorageForDeviceAddresses()).thenReturn(deviceAdressesPersistentStorageMock);
        when(sourcePersistentStorageFactoryMock.getPersistentStorageForDevice(anyString())).thenReturn(devicePersistentStorageMock);
        when(sourcePersistentStorageFactoryMock.getPersistentStorage()).thenReturn(persistentStorageMock);

        when(destinationPersistentStorageFactoryMock.getPersistentStorageForUser()).thenReturn(userPersistentStorageMock);
        when(destinationPersistentStorageFactoryMock.getPersistentStorageForDevice(anyString())).thenReturn(devicePersistentStorageMock);
        when(destinationPersistentStorageFactoryMock.getPersistentStorage()).thenReturn(persistentStorageMock);

        keyMap = new HashMap<>();
        doReturn(keyMap).when(userPersistentStorageMock).getAll();
        doReturn(keyMap).when(deviceAdressesPersistentStorageMock).getAll();
        doReturn(keyMap).when(devicePersistentStorageMock).getAll();
        doReturn(keyMap).when(persistentStorageMock).getAll();

        deviceAddresses = new HashSet<>();
        deviceAddresses.add(TEST_DEVICE_1);
        deviceAddresses.add(TEST_DEVICE_2);

        sharedPreferencesMigrator = new SharedPreferencesMigrator(sourcePersistentStorageFactoryMock, destinationPersistentStorageFactoryMock);
    }

    @Test
    public void shouldCallContainsWithMigrationKeyWhenDestinationContainsDataIsCalled() throws Exception {
        sharedPreferencesMigrator.destinationPersistentStorageContainsData();

        verify(persistentStorageMock).contains(DataMigrater.MIGRATION_ID_KEY);
    }

    @Test
    public void shouldMoveAllUserDataWhenExecuteIsCalled() throws Exception {
        keyMap.clear();
        keyMap.put(KEY_1, VALUE_1_STRING);
        keyMap.put(KEY_2, VALUE_1_STRING);
        keyMap.put(KEY_3, VALUE_1_STRING);

        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock).getAll();

        verify(destinationPersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock, times(3)).put(anyString(), any());
    }

    @Test
    public void shouldMoveStringValuesWhenExecuteIsCalled() throws Exception {
        when(userPersistentStorageMock.get(KEY_1)).thenReturn(VALUE_1_STRING);
        keyMap.clear();
        keyMap.put(KEY_1, VALUE_1_STRING);

        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock).getAll();

        verify(destinationPersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock).put(KEY_1, VALUE_1_STRING);
    }

    @Test
    public void shouldMoveIntValuesWhenExecuteIsCalled() throws Exception {
        when(userPersistentStorageMock.get(KEY_1)).thenReturn(VALUE_2_INT);
        keyMap.put(KEY_1, VALUE_2_INT);

        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock).getAll();

        verify(destinationPersistentStorageFactoryMock).getPersistentStorageForUser();
        verify(userPersistentStorageMock).put(KEY_1, VALUE_2_INT);
    }

    @Test
    public void shouldMoveAllDeviceAddressesDataWhenExecuteIsCalled() throws Exception {
        when(deviceAdressesPersistentStorageMock.getStringSet(KEY_1, null)).thenReturn(deviceAddresses);
        keyMap.clear();
        keyMap.put(KEY_1, VALUE_1_STRING);

        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForDeviceAddresses();
        verify(deviceAdressesPersistentStorageMock).getAll();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForDevice(TEST_DEVICE_1);
        verify(destinationPersistentStorageFactoryMock).getPersistentStorageForDevice(TEST_DEVICE_1);

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageForDevice(TEST_DEVICE_2);
        verify(destinationPersistentStorageFactoryMock).getPersistentStorageForDevice(TEST_DEVICE_2);
    }

    @Test
    public void shouldMoveAllDeviceDataWhenExecuteIsCalled() throws Exception {
        when(deviceAdressesPersistentStorageMock.getStringSet(KEY_1, null)).thenReturn(deviceAddresses);

        keyMap.clear();
        keyMap.put(KEY_1, VALUE_1_STRING);
        keyMap.put(KEY_2, VALUE_1_STRING);

        when(deviceAdressesPersistentStorageMock.getString(KEY_1, null)).thenReturn(VALUE_1_STRING);
        when(deviceAdressesPersistentStorageMock.getString(KEY_2, null)).thenReturn(VALUE_1_STRING);


        sharedPreferencesMigrator.execute();

        verify(devicePersistentStorageMock, times(2)).getAll();

        verify(devicePersistentStorageMock, times(4)).put(anyString(), any());
    }

    @Test
    public void shouldMoveAllPersistentDataWhenExecuteIsCalled() throws Exception {
        keyMap.clear();
        when(persistentStorageMock.get(KEY_1, null)).thenReturn(VALUE_1_STRING);
        when(persistentStorageMock.get(KEY_2, null)).thenReturn(VALUE_1_STRING);
        when(persistentStorageMock.get(KEY_3, null)).thenReturn(VALUE_1_STRING);
        when(persistentStorageMock.get(KEY_4, null)).thenReturn(VALUE_1_STRING);

        keyMap.put(KEY_1, VALUE_1_STRING);
        keyMap.put(KEY_2, VALUE_1_STRING);
        keyMap.put(KEY_3, VALUE_1_STRING);
        keyMap.put(KEY_4, VALUE_1_STRING);

        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorage();
        verify(persistentStorageMock).getAll();

        verify(destinationPersistentStorageFactoryMock).getPersistentStorage();
        verify(persistentStorageMock, times(4)).put(anyString(), any());
    }

    @Test
    public void shouldClearOldPersistentStorage() throws Exception {
        sharedPreferencesMigrator.execute();

        verify(sourcePersistentStorageFactoryMock).getPersistentStorageCleaner();
        verify(persistentStorageCleaner).clearAllData();
    }
}