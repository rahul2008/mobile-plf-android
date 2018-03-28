package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class InitializingServicesState extends State {

    private static final String TAG = InitializingServicesState.class.getSimpleName();

    private static final long CONNECT_TIMEOUT = 20000L;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            //SHNLogger.e(TAG, "connect timeout in state: " + internalState);
            //failedToConnectResult = SHNResult.SHNErrorTimeout;
            context.setState(new DisconnectingState(context));
        }
    }, CONNECT_TIMEOUT);

    public InitializingServicesState(StateContext context) {
        super(context);

        connectUsedServicesToBleLayer();

        connectTimer.restart();
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        connectTimer.stop();

        if (areAllRegisteredServicesReady()) {
            context.setState(new ReadyState(context));
        }

        if (state == SHNService.State.Error) {
            context.setState(new DisconnectingState(context));
        }
    }

    private void connectUsedServicesToBleLayer() {
        for (BluetoothGattService bluetoothGattService : context.getBtGatt().getServices()) {
            SHNService shnService = context.getSHNService(bluetoothGattService.getUuid());
            SHNLogger.i(TAG, "onServicedDiscovered: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not used by plugin" : " connecting plugin service to ble service"));

            if (context.getDiscoveryListener() != null) {
                context.getDiscoveryListener().onServiceDiscovered(bluetoothGattService.getUuid(), shnService);
            }

            if (shnService != null) {
                shnService.connectToBLELayer(context.getBtGatt(), bluetoothGattService);
            }
        }
    }

    @Override
    void disconnect() {
        connectTimer.stop();
        context.setState(new DisconnectingState(context));
    }

    private boolean areAllRegisteredServicesReady() {
        Boolean allReady = true;
        for (SHNService service : context.getSHNServices().values()) {
            if (service.getState() != SHNService.State.Ready) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }

}
