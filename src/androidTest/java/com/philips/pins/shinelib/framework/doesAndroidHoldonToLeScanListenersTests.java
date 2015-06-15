package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.test.AndroidTestCase;

import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;

import java.lang.ref.WeakReference;

/**
 * Created by 310188215 on 11/03/15.
 */
public class doesAndroidHoldonToLeScanListenersTests extends AndroidTestCase {

    private static class TestClass implements BluetoothAdapter.LeScanCallback, LeScanCallbackProxy.LeScanCallback {
        private BluetoothAdapter bluetoothAdapter;
        private LeScanCallbackProxy leScanCallbackProxy;

        public void registerProxyListener() {
            leScanCallbackProxy = new LeScanCallbackProxy();
            leScanCallbackProxy.startLeScan(this, bluetoothAdapter);
        }

        public void unregisterProxyListener() {
            leScanCallbackProxy.stopLeScan(this);
        }

        public TestClass (Context context) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        public void registerClassListener() {
            bluetoothAdapter.startLeScan(this);
        }

        public void unregisterClassListener() {
            bluetoothAdapter.stopLeScan(this);
        }

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        }
    }

    @Override
    public void setUp() {
        BleUtilities.init(getContext());
    }

    public void test01WeakReferenceGetsClearedAfterGC() {
        TestClass testClass = new TestClass(getContext());
        WeakReference<TestClass> testClassWeakReference = new WeakReference<TestClass>(testClass);
        assertNotNull(testClassWeakReference.get());
        testClass = null;
        Runtime.getRuntime().gc();
        assertNull(testClassWeakReference.get());
    }

    public void test02AfterUnregisterAndroidHoldsAReferenceToTheLeScanListener() {
        TestClass testClass = new TestClass(getContext());
        WeakReference<TestClass> testClassWeakReference = new WeakReference<TestClass>(testClass);

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }

        testClass.registerClassListener();

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }

        testClass.unregisterClassListener();

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }


        assertNotNull(testClassWeakReference.get());
        testClass = null;
        Runtime.getRuntime().gc();
        assertNotNull(testClassWeakReference.get());
    }

    public void test03UsingAProxyMakesTheLeakSmaller() {
        TestClass testClass = new TestClass(getContext());
        WeakReference<TestClass> testClassWeakReference = new WeakReference<TestClass>(testClass);

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }

        testClass.registerProxyListener();

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }

        testClass.unregisterProxyListener();

        try { Thread.sleep(1000l); } catch (InterruptedException e) { e.printStackTrace(); }


        assertNotNull(testClassWeakReference.get());
        testClass = null;
        Runtime.getRuntime().gc();
        assertNull(testClassWeakReference.get());
    }

}
