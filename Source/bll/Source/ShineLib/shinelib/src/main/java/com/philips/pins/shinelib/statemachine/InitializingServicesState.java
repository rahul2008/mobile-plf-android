package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class InitializingServicesState extends State {

    private static final String TAG = InitializingServicesState.class.getSimpleName();

    private static final long CONNECT_TIMEOUT = 20_000L;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            stateMachine.setState(InitializingServicesState.this, new DisconnectingState(stateMachine));
        }
    }, CONNECT_TIMEOUT);

    public InitializingServicesState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void setup() {
        connectUsedServicesToBleLayer();
        connectTimer.restart();
    }

    @Override
    public void breakdown() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        connectTimer.stop();

        if (areAllRegisteredServicesReady()) {
            stateMachine.setState(this, new ReadyState(stateMachine));
        }

        if (state == SHNService.State.Error) {
            stateMachine.setState(this, new DisconnectingState(stateMachine));
        }
    }

    private void connectUsedServicesToBleLayer() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt == null) {
            return;
        }

        for (BluetoothGattService bluetoothGattService : btGatt.getServices()) {
            SHNService shnService = sharedResources.getSHNService(bluetoothGattService.getUuid());
            SHNLogger.i(TAG, "onServicedDiscovered: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not used by plugin" : " connecting plugin service to ble service"));

            if (sharedResources.getDiscoveryListener() != null) {
                sharedResources.getDiscoveryListener().onServiceDiscovered(bluetoothGattService.getUuid(), shnService);
            }

            if (shnService != null) {
                shnService.connectToBLELayer(btGatt, bluetoothGattService);
            }
        }
    }

    @Override
    public void disconnect() {
        connectTimer.stop();
        stateMachine.setState(this, new DisconnectingState(stateMachine));
    }

    private boolean areAllRegisteredServicesReady() {
        Boolean allReady = true;
        for (SHNService service : sharedResources.getSHNServices().values()) {
            if (service.getState() != SHNService.State.Ready) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }

}
