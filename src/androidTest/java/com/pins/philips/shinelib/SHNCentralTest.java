package com.pins.philips.shinelib;

import android.test.AndroidTestCase;

import com.pins.philips.shinelib.bletestsupport.BleUtilities;
import com.pins.philips.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.pins.philips.shinelib.utilities.MockedBleUtilitiesBuilder;

import java.lang.ref.WeakReference;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SHNCentralTest extends AndroidTestCase {
    private static final String TAG = SHNCentralTest.class.getSimpleName();
    private SHNCentral shnCentral;

    private SHNBluetoothHardwareUnavailableException createSHNCentralHelper() {
        shnCentral = null;
        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = null;
        try {
            shnCentral = new SHNCentral(null, getContext());
        } catch (SHNBluetoothHardwareUnavailableException e) {
            shnBluetoothHardwareUnavailableException = e;
        }
        return shnBluetoothHardwareUnavailableException;
    }

    @Override public void setUp() {
        System.setProperty(
                "dexmaker.dexcache",
                getContext().getCacheDir().getPath());
//                getInstrumentation().getTargetContext().getCacheDir().getPath());

        BleUtilities.setInstance(null);
        BleUtilities.init(getContext());
    }

    @Override public void tearDown() {
        if (shnCentral != null) {
            WeakReference<SHNCentral> shnCentralWeakReference = new WeakReference<SHNCentral>(shnCentral);
            shnCentral.shutdown();
            shnCentral = null; // Remove the strong reference
            long startTime = System.currentTimeMillis();
            while (shnCentralWeakReference.get() != null && (System.currentTimeMillis() - startTime < 10000l)) {
                Runtime.getRuntime().gc();
                try {
                    Thread.sleep(100l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            assertNull("References should be released within 10 seconds", shnCentralWeakReference.get());
        }
    }

    public void test01CannotCreateSHNCentralWhenBLEUnavailable() {
        new MockedBleUtilitiesBuilder()
                .setDeviceHasBle(false)
                .buildAndUse();

        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = createSHNCentralHelper();
        assertNotNull(shnBluetoothHardwareUnavailableException);
        assertNull(shnCentral);
    }

    public void test02CanCreateSHNCentralStartEnableBleActivity() {
        MockedBleUtilitiesBuilder mockedBleUtilitiesBuilder = new MockedBleUtilitiesBuilder()
                .setDeviceHasBle(true)
                .setBluetoothEnabled(false);
        mockedBleUtilitiesBuilder.buildAndUse();

        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = createSHNCentralHelper();
        assertNull(shnBluetoothHardwareUnavailableException);
        assertNotNull(shnCentral);
        verify(mockedBleUtilitiesBuilder.getBleUtilities())._startEnableBluetoothActivity();
        assertFalse(shnCentral.isBluetoothAdapterEnabled());
    }

    public void test03CanCreateSHNCentralWhenBLEAvailable() {
        new MockedBleUtilitiesBuilder()
                .setDeviceHasBle(true)
                .setBluetoothEnabled(true)
                .buildAndUse();

        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = createSHNCentralHelper();
        assertNull(shnBluetoothHardwareUnavailableException);
        assertNotNull(shnCentral);
        assertTrue(shnCentral.isBluetoothAdapterEnabled());
    }

    public void test04CanRegisterASHNDeviceDefinitionInfo() {
        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = createSHNCentralHelper();
        assertNull(shnBluetoothHardwareUnavailableException);
        assertNotNull(shnCentral);

        SHNDeviceDefinitionInfo mockedSHShnDeviceDefinitionInfo = mock(SHNDeviceDefinitionInfo.class);
        assertEquals(0, shnCentral.getRegisteredDeviceDefinitions().size());
        shnCentral.registerDeviceDefinition(mockedSHShnDeviceDefinitionInfo);
        assertEquals(1, shnCentral.getRegisteredDeviceDefinitions().size());

        assertEquals(mockedSHShnDeviceDefinitionInfo, shnCentral.getRegisteredDeviceDefinitions().get(0));
    }

    public void test05CanGetASHNDeviceScannerAndScan() {
        SHNBluetoothHardwareUnavailableException shnBluetoothHardwareUnavailableException = createSHNCentralHelper();
        assertNotNull(shnCentral);

        SHNDeviceDefinitionInfo mockedSHShnDeviceDefinitionInfo = mock(SHNDeviceDefinitionInfo.class);
        shnCentral.registerDeviceDefinition(mockedSHShnDeviceDefinitionInfo);

        SHNDeviceScanner shnDeviceScanner = shnCentral.getShnDeviceScanner();
        assertNotNull(shnDeviceScanner);

        SHNDeviceScanner.SHNDeviceScannerListener mockedShnDeviceScannerListener = mock(SHNDeviceScanner.SHNDeviceScannerListener.class);

        shnDeviceScanner.startScanning(mockedShnDeviceScannerListener);

        try {
            Thread.sleep(100l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shnDeviceScanner.stopScanning(mockedShnDeviceScannerListener);
    }
}