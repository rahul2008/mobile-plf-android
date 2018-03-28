package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.UUID;

public class InitializingServicesState extends State {

    private static final String TAG = InitializingServicesState.class.getSimpleName();

    public InitializingServicesState(StateContext context) {
        super(context);

        connectUsedServicesToBleLayer();
    }

    private void connectUsedServicesToBleLayer() {
        for (BluetoothGattService bluetoothGattService : context.getBtGatt().getServices()) {
            SHNService shnService = getSHNService(bluetoothGattService.getUuid());
            SHNLogger.i(TAG, "onServicedDiscovered: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not used by plugin" : " connecting plugin service to ble service"));

            if (discoveryListener != null) {
                discoveryListener.onServiceDiscovered(bluetoothGattService.getUuid(), shnService);
            }

            if (shnService != null) {
                shnService.connectToBLELayer(context.getBtGatt(), bluetoothGattService);
            }
        }
    }

    private SHNService getSHNService(UUID serviceUUID) {
        return registeredServices.get(serviceUUID);
    }
}
