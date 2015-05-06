package com.pins.philips.shinelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.pins.philips.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.pins.philips.shinelib.helper.MockedHandler;
import com.pins.philips.shinelib.helper.Utility;
import com.pins.philips.shinelib.utility.Utilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by 310188215 on 06/05/15.
 */
@RunWith(PowerMockRunner.class)
public class SHNCentralTest {
    private SHNCentral shnCentral;
    private MockedHandler mockedUserHandler;
    private Context mockedContext;
    private PackageManager mockedPackageManager;
    private BluetoothManager mockedBluetoothManager;
    private BluetoothAdapter mockedBluetoothAdapter;

    @Before
    public void setUp() throws SHNBluetoothHardwareUnavailableException {
        mockedContext = (Context) Utility.makeThrowingMock(Context.class);
        mockedUserHandler = new MockedHandler();
        mockedPackageManager = (PackageManager) Utility.makeThrowingMock(PackageManager.class);
        mockedBluetoothManager = (BluetoothManager) Utility.makeThrowingMock(BluetoothManager.class);
        mockedBluetoothAdapter = (BluetoothAdapter) Utility.makeThrowingMock(BluetoothAdapter.class);

        doReturn(mockedContext).when(mockedContext).getApplicationContext();
        doReturn(mockedPackageManager).when(mockedContext).getPackageManager();
        doReturn(true).when(mockedPackageManager).hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        doReturn(mockedBluetoothManager).when(mockedContext).getSystemService(Context.BLUETOOTH_SERVICE);
        doReturn(mockedBluetoothAdapter).when(mockedBluetoothManager).getAdapter();
        doReturn(true).when(mockedBluetoothAdapter).isEnabled();
        doReturn(null).when(mockedContext).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));

        shnCentral = new SHNCentral(mockedUserHandler.getMock(), mockedContext);
    }

    @Test
    public void testGetShnCentralState() {

    }

    @Test
    public void testGetInternalHandler() {

    }

    @Test
    public void testGetUserHandler() {

    }

    @Test
    public void testShutdown() {

    }

    @Test
    public void testGetApplicationContext() {

    }

    @Test
    public void testRunOnHandlerThread() {

    }

    @Test
    public void testIsBluetoothAdapterEnabled() {

    }

    @Test
    public void testGetVersion() {

    }

    @Test
    public void testRegisterDeviceDefinition() {

    }

    @Test
    public void testGetRegisteredDeviceDefinitions() {

    }

    @Test
    public void testGetShnCentralListener() {

    }

    @Test
    public void testSetShnCentralListener() {

    }

    @Test
    public void testGetShnDeviceScanner() {

    }

    @Test
    public void testGetShnDeviceAssociation() {

    }

    @Test
    public void testGetBTDevice() {

    }
}