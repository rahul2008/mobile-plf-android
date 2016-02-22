package com.philips.pins.shinelib.utility;

import android.app.Application;
import android.content.Context;

import com.philips.pins.shinelib.RobolectricTest;
import com.philips.pins.shinelib.SHNDeviceAssociationHelper;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DataMigraterTest extends RobolectricTest {

    public static final String TEST_DEVICE_1 = "TEST_DEVICE_1";
    public static final String TEST_DEVICE_2 = "TEST_DEVICE_2";
    public static final String TEST_DEVICE_3 = "TEST_DEVICE_3";
    public static final String KEY_1 = "KEY_1";
    public static final String KEY_2 = "KEY_2";
    public static final String VALUE_1_STRING = "VALUE_1_STRING";
    public static final int VALUE_2_INT = 222;

    private DataMigrater dataMigrater;

    @Before
    public void setUp() {
        dataMigrater = new DataMigrater();
    }

    @Test
    public void ShouldMoveShineData_WhenNoDeviceDataPresent() {
        Application context = RuntimeEnvironment.application;

        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferences, Context.MODE_PRIVATE));

        insertTestData(oldRootStorage);

        dataMigrater.execute(context, new PersistentStorageFactory(context));

        PersistentStorageFactory storageFactory = new PersistentStorageFactory(context);
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();

        verifyTestData(newRootStorage);
    }

    @Test
    public void ShouldMoveNoData_WhenNoShineOrDeviceDataPresent() {
        Application context = RuntimeEnvironment.application;

        dataMigrater.execute(context, new PersistentStorageFactory(context));

        PersistentStorageFactory storageFactory = new PersistentStorageFactory(context);
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();

        assertThat(newRootStorage.getAll()).isEmpty();
    }

    @Test
    public void ShouldMoveShineAndDeviceData_WhenPresent() {
        Application context = RuntimeEnvironment.application;

        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferences, Context.MODE_PRIVATE));
        PersistentStorage oldDevice1Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_1 + DataMigrater.oldDevicePreferencesSuffix, Context.MODE_PRIVATE));
        PersistentStorage oldDevice2Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_2 + DataMigrater.oldDevicePreferencesSuffix, Context.MODE_PRIVATE));
        PersistentStorage oldDevice3Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_3 + DataMigrater.oldDevicePreferencesSuffix, Context.MODE_PRIVATE));

        insertTestData(oldRootStorage);
        insertTestData(oldDevice1Storage);
        insertTestData(oldDevice2Storage);
        insertTestData(oldDevice3Storage);

        Set<String> deviceAddresses = new HashSet<>();
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_1);
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_2);
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_3);

        oldRootStorage.put(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES, deviceAddresses);

        dataMigrater.execute(context, new PersistentStorageFactory(context));

        PersistentStorageFactory storageFactory = new PersistentStorageFactory(context);
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();
        PersistentStorage newDevice1Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_1);
        PersistentStorage newDevice2Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_2);
        PersistentStorage newDevice3Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_3);

        verifyTestData(newRootStorage);
        verifyTestData(newDevice1Storage);
        verifyTestData(newDevice2Storage);
        verifyTestData(newDevice3Storage);

        assertThat(oldRootStorage.getAll()).isEmpty();
        assertThat(oldDevice1Storage.getAll()).isEmpty();
        assertThat(oldDevice2Storage.getAll()).isEmpty();
        assertThat(oldDevice3Storage.getAll()).isEmpty();
    }

    private void verifyTestData(final PersistentStorage storage) {
        assertThat(storage.get(KEY_1)).isEqualTo(VALUE_1_STRING);
        assertThat(storage.get(KEY_2)).isEqualTo(VALUE_2_INT);
    }

    private void insertTestData(final PersistentStorage storage) {
        storage.put(KEY_1, VALUE_1_STRING);
        storage.put(KEY_2, VALUE_2_INT);
    }
}