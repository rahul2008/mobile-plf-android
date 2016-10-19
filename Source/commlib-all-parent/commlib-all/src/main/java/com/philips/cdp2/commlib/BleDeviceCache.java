package com.philips.cdp2.commlib;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.communication.BleStrategy;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

import java.util.HashMap;
import java.util.Map;

public class BleDeviceCache implements SHNDeviceScanner.SHNDeviceScannerListener, BleStrategy.ByteInterfaceProvider {
    private final Map<String, SHNDevice> deviceMap = new HashMap<>();


    @VisibleForTesting
    String getDeviceCppId(SHNDevice device) {
        //TODO implement

        return device.getAddress(); // WRONG!!!
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();
        deviceMap.put(getDeviceCppId(device), device);
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        //don't care
    }

    @Override
    public BleStrategy.ByteInterface interfaceFor(String cppId) {
        SHNDevice device = deviceMap.get(cppId);
        final CapabilityDiComm capability = (CapabilityDiComm) device.getCapabilityForType(SHNCapabilityType.DI_COMM);

        return new BleStrategy.ByteInterface() {
            @Override
            public void write(byte[] bytes) {
                capability.writeData(bytes);
            }

            @Override
            public void addListener(final BleStrategy.ByteListener byteListener) {
                capability.addDataListener(new ResultListener<SHNDataRaw>() {
                    @Override
                    public void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
                        byteListener.onBytes(shnDataRaw.getRawData());
                    }
                });
            }
        };
    }
}
