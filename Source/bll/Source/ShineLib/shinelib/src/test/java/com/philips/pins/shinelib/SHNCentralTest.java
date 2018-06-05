/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral.SHNCentralListener;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.utility.DataMigrater;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.SharedPreferencesMigrator;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doReturn;

public class SHNCentralTest extends RobolectricTest {
    private static final String TEST_DEVICE_MAC_ADDRESS = "DE:AD:C0:DE:01:23";
    private static final String TEST_DEVICE_TYPE = "TestDeviceType";

    private SHNCentral shnCentral;

    private MockedHandler mockedUserHandler;
    private MockedHandler mockedInternalHandler;
    private Context mockedContext;

    private DataMigrater mockedDataMigrater;

    @Captor
    private ArgumentCaptor<BroadcastReceiver> broadcastReceiverArgumentCaptor;

    @Captor
    private ArgumentCaptor<IntentFilter> intentFilterCaptor;

    @Mock
    private PersistentStorageFactory persistentStorageFactoryMock;

    @Mock
    private SharedPreferencesMigrator mockedSharedPreferencesMigrator;

    @Mock
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfoMock;

    @Mock
    private SHNDeviceDefinitionInfo.SHNDeviceDefinition shnDeviceDefinitionMock;

    @Mock
    private SHNDevice shnDeviceMock;

    @Mock
    private SHNDevice alternateShnDeviceMock;

    @Mock
    private SHNCentral.SHNBondStatusListener shnBondStatusListenerMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Before
    public void setUp() throws SHNBluetoothHardwareUnavailableException, Exception {
        initMocks(this);

        mockedContext = Utility.makeThrowingMock(Context.class);
        mockedUserHandler = new MockedHandler();
        mockedInternalHandler = new MockedHandler();

        PackageManager mockedPackageManager = Utility.makeThrowingMock(PackageManager.class);
        BluetoothManager mockedBluetoothManager = Utility.makeThrowingMock(BluetoothManager.class);
        BluetoothAdapter mockedBluetoothAdapter = Utility.makeThrowingMock(BluetoothAdapter.class);
        SharedPreferences mockedSharedPreferences = Utility.makeThrowingMock(SharedPreferences.class);
        mockedDataMigrater = Utility.makeThrowingMock(DataMigrater.class);

        doReturn("mockedContext").when(mockedContext).toString();
        doReturn(mockedContext).when(mockedContext).getApplicationContext();
        doReturn(mockedPackageManager).when(mockedContext).getPackageManager();
        doReturn(true).when(mockedPackageManager).hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        doReturn(mockedBluetoothManager).when(mockedContext).getSystemService(Context.BLUETOOTH_SERVICE);
        doReturn(mockedBluetoothAdapter).when(mockedBluetoothManager).getAdapter();
        doReturn(true).when(mockedBluetoothAdapter).isEnabled();
        doReturn(null).when(mockedContext).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        doReturn(mockedSharedPreferences).when(mockedContext).getSharedPreferences(anyString(), anyInt());

        doNothing().when(mockedDataMigrater).execute(any(Context.class), any(PersistentStorageFactory.class));

        doReturn(shnDeviceDefinitionMock).when(shnDeviceDefinitionInfoMock).getSHNDeviceDefinition();
        doReturn(TEST_DEVICE_TYPE).when(shnDeviceDefinitionInfoMock).getDeviceTypeName();
        doReturn(TEST_DEVICE_TYPE).when(shnDeviceMock).getDeviceTypeName();
        doReturn(TEST_DEVICE_MAC_ADDRESS).when(shnDeviceMock).getAddress();
        doReturn(TEST_DEVICE_TYPE).when(alternateShnDeviceMock).getDeviceTypeName();
        doReturn(TEST_DEVICE_MAC_ADDRESS).when(alternateShnDeviceMock).getAddress();

        doReturn(appTaggingInterfaceMock).when(appInfraInterfaceMock).getTagging();

        shnCentral = new SHNCentral(mockedUserHandler.getMock(), mockedContext, false, null, false, appInfraInterfaceMock) {
            @Override
            DataMigrater createDataMigrater() {
                return mockedDataMigrater;
            }

            @Override
            Handler createInternalHandler() {
                return mockedInternalHandler.getMock();
            }

            @Override
            PersistentStorageFactory createPersistentStorageFactory(SharedPreferencesProvider sharedPreferencesProvider) {
                return persistentStorageFactoryMock;
            }

            @Override
            SHNUserConfiguration createUserConfiguration() {
                return mock(SHNUserConfiguration.class);
            }
        };

        doReturn(shnDeviceMock).when(shnDeviceDefinitionMock).createDeviceFromDeviceAddress(TEST_DEVICE_MAC_ADDRESS, shnDeviceDefinitionInfoMock, shnCentral);
    }

    private BroadcastReceiver captureBroadCastReceiver(@NonNull String action) {
        verify(mockedContext, atLeastOnce()).registerReceiver(broadcastReceiverArgumentCaptor.capture(), intentFilterCaptor.capture());

        List<IntentFilter> allValues = intentFilterCaptor.getAllValues();
        for (int i = 0; i < allValues.size(); i++) {
            IntentFilter intentFilter = allValues.get(i);
            if (intentFilter.hasAction(action))
                return broadcastReceiverArgumentCaptor.getAllValues().get(i);
        }

        return null;
    }

    private void simulateBLEStateChange(int state) {
        Intent intentMock = mock(Intent.class);
        when(intentMock.getAction()).thenReturn(BluetoothAdapter.ACTION_STATE_CHANGED);
        when(intentMock.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)).thenReturn(state);

        BroadcastReceiver broadcastReceiver = captureBroadCastReceiver(BluetoothAdapter.ACTION_STATE_CHANGED);
        broadcastReceiver.onReceive(mockedContext, intentMock);
    }

    @Test
    public void givenASHNCentralListenerIsRegistered_WhenAStateChangeOccurs_ItNotifiesTheListenerOnTheUserHandler() throws InterruptedException {
        SHNCentralListener listenerMock = mock(SHNCentralListener.class);
        shnCentral.registerShnCentralListener(listenerMock);

        mockedUserHandler.enableImmediateExecuteOnPost(false);

        simulateBLEStateChange(BluetoothAdapter.STATE_TURNING_OFF);

        verify(listenerMock, never()).onStateUpdated(any(SHNCentral.class));

        verify(mockedUserHandler.getMock(), times(2)).post(isA(Runnable.class));
        mockedUserHandler.executeFirstPostedExecution();
        mockedUserHandler.executeFirstPostedExecution();

        verify(listenerMock).onStateUpdated(isA(SHNCentral.class));
    }

    @Test
    public void whenCreatedThenDataMigratedExecuteIsCalled() {
        verify(mockedDataMigrater).execute(mockedContext, persistentStorageFactoryMock);
    }

    private void createSHNCentralWithProvider(final SharedPreferencesProvider mockedSharedPreferencesProvider, boolean migrateFromDefaultProviderToCustom) throws SHNBluetoothHardwareUnavailableException {
        new SHNCentral(mockedUserHandler.getMock(), mockedContext, false, mockedSharedPreferencesProvider, migrateFromDefaultProviderToCustom, appInfraInterfaceMock) {
            @Override
            SharedPreferencesMigrator createSharedPreferencesMigrator(PersistentStorageFactory source, PersistentStorageFactory destination) {
                return mockedSharedPreferencesMigrator;
            }

            @Override
            DataMigrater createDataMigrater() {
                return mockedDataMigrater;
            }

            @Override
            PersistentStorageFactory createPersistentStorageFactory(SharedPreferencesProvider sharedPreferencesProvider) {
                return persistentStorageFactoryMock;
            }

            @Override
            Handler createInternalHandler() {
                return mockedInternalHandler.getMock();
            }

            @Override
            long getHandlerThreadId(Handler handler) {
                return 1;
            }

            @Override
            SHNUserConfiguration createUserConfiguration() {
                return mock(SHNUserConfiguration.class);
            }
        };
    }

    @Test
    public void whenCreatedWithCustomProviderAndMigrationIsRequestedThenExecuteIsCalled() throws SHNBluetoothHardwareUnavailableException {
        SharedPreferencesProvider mockedSharedPreferencesProvider = mock(SharedPreferencesProvider.class);

        createSHNCentralWithProvider(mockedSharedPreferencesProvider, true);

        verify(mockedSharedPreferencesMigrator).execute();
    }

    @Test
    public void whenCreatedWithCustomProviderAndMigrationIsNotRequestedThenExecuteIsNotCalled() throws SHNBluetoothHardwareUnavailableException {
        SharedPreferencesProvider mockedSharedPreferencesProvider = mock(SharedPreferencesProvider.class);

        createSHNCentralWithProvider(mockedSharedPreferencesProvider, false);

        verify(mockedSharedPreferencesMigrator, never()).execute();
    }

    @Test
    public void whenCreatedWithCustomProviderAndMigrationIsNotRequestedThenContainsDataMustReturnTrue() throws SHNBluetoothHardwareUnavailableException {
        SharedPreferencesProvider mockedSharedPreferencesProvider = mock(SharedPreferencesProvider.class);

        reset(mockedDataMigrater);
        doNothing().when(mockedDataMigrater).execute(any(Context.class), any(PersistentStorageFactory.class));

        createSHNCentralWithProvider(mockedSharedPreferencesProvider, false);

        verify(mockedDataMigrater).execute(mockedContext, persistentStorageFactoryMock);
    }

    @Test
    public void whenCreatedWithNoCustomProviderThenExecuteIsNotCalled() throws SHNBluetoothHardwareUnavailableException {
        createSHNCentralWithProvider(null, false);
        createSHNCentralWithProvider(null, true);

        verify(mockedSharedPreferencesMigrator, never()).execute();
    }

    @Test
    public void whenCreatedWithCustomProviderAndMigrationIsRequestedAndMigrationAlreadyRunThenExecuteIsNotCalledAgain() throws SHNBluetoothHardwareUnavailableException {
        when(mockedSharedPreferencesMigrator.destinationPersistentStorageContainsData()).thenReturn(true);
        SharedPreferencesProvider mockedSharedPreferencesProvider = mock(SharedPreferencesProvider.class);

        createSHNCentralWithProvider(mockedSharedPreferencesProvider, true);

        verify(mockedSharedPreferencesMigrator, never()).execute();
    }

    @Test
    public void verifyThatRemoveDeviceFromDeviceCacheCorrectlyRemovesItFromTheCache() {
        // Verify that shnDeviceMock is returned when a device needs to be created
        SHNDevice shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(TEST_DEVICE_MAC_ADDRESS, shnDeviceDefinitionInfoMock);
        verify(shnDeviceDefinitionMock).createDeviceFromDeviceAddress(anyString(), isA(SHNDeviceDefinitionInfo.class), isA(SHNCentral.class));
        assertEquals(shnDeviceMock, shnDevice);

        // Change the setup such that the alternativeSHNDevice is returned when a new one needs to be created
        doReturn(alternateShnDeviceMock).when(shnDeviceDefinitionMock).createDeviceFromDeviceAddress(TEST_DEVICE_MAC_ADDRESS, shnDeviceDefinitionInfoMock, shnCentral);

        // verify that when a the device is again created it is actually obtained from a cache
        shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(TEST_DEVICE_MAC_ADDRESS, shnDeviceDefinitionInfoMock);
        verify(shnDeviceDefinitionMock).createDeviceFromDeviceAddress(anyString(), isA(SHNDeviceDefinitionInfo.class), isA(SHNCentral.class));
        assertEquals(shnDeviceMock, shnDevice);

        // Remove the device from the cache and verify that the alternative is now returned
        shnCentral.removeDeviceFromDeviceCache(shnDevice);
        shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(TEST_DEVICE_MAC_ADDRESS, shnDeviceDefinitionInfoMock);
        verify(shnDeviceDefinitionMock, times(2)).createDeviceFromDeviceAddress(anyString(), isA(SHNDeviceDefinitionInfo.class), isA(SHNCentral.class));
        assertEquals(alternateShnDeviceMock, shnDevice);
    }

    @Test
    public void verifyVersionIsPresent() {
        String version = shnCentral.getVersion();

        assertNotNull(version);
        assertTrue(version.contains(BuildConfig.VERSION_NAME));
    }

    @Test
    public void givenListenerIsRegistered_whenStatusChanges_thenListenerIsNotified() throws Exception {
        SHNCentralListener listener = mock(SHNCentralListener.class);
        shnCentral.addInternalListener(listener);

        simulateBLEStateChange(BluetoothAdapter.STATE_TURNING_OFF);

        verify(listener).onStateUpdated(shnCentral);
    }

    @Test
    public void givenListenerIsRegisteredAndUnregistered_whenStatusChanges_thenListenerIsNotNotified() throws Exception {
        SHNCentralListener listener = mock(SHNCentralListener.class);
        shnCentral.addInternalListener(listener);
        shnCentral.removeInternalListener(listener);

        simulateBLEStateChange(BluetoothAdapter.STATE_TURNING_OFF);

        verify(listener, times(0)).onStateUpdated(shnCentral);
    }

    @Test
    public void givenBondListenerIsRegistered_whenBondIsCreated_thenListenerIsNotified() {
        Intent intentMock = mock(Intent.class);
        Bundle bundleMock = mock(Bundle.class);
        BluetoothDevice bluetoothDeviceMock = mock(BluetoothDevice.class);
        when(intentMock.getExtras()).thenReturn(bundleMock);
        when(bundleMock.getInt(BluetoothDevice.EXTRA_BOND_STATE)).thenReturn(0);
        when(bundleMock.getInt(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE)).thenReturn(1);
        when(bundleMock.getParcelable(BluetoothDevice.EXTRA_DEVICE)).thenReturn(bluetoothDeviceMock);
        final String address = "00:11:22:33:44:55";
        when(bluetoothDeviceMock.getAddress()).thenReturn(address);
        shnCentral.registerBondStatusListenerForAddress(shnBondStatusListenerMock, address);

        shnCentral.bondStateChangedReceiver.onReceive(mockedContext, intentMock);

        verify(shnBondStatusListenerMock).onBondStatusChanged(bluetoothDeviceMock, 0, 1);
    }
}
