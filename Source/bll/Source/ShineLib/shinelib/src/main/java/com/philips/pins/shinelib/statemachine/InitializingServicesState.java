package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class InitializingServicesState extends SHNDeviceState {

    private static final String TAG = InitializingServicesState.class.getSimpleName();

    private static final long INITIALIZING_TIMEOUT = 20_000L;

    private Timer initializingTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "initializing timeout in InitializingServicesState");
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            stateMachine.setState(InitializingServicesState.this, new DisconnectingState(stateMachine, sharedResources));
        }
    }, INITIALIZING_TIMEOUT);

    public InitializingServicesState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        initializingTimer.restart();

        connectUsedServicesToBleLayer();
    }

    @Override
    protected void onExit() {
        initializingTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        if (areAllRegisteredServicesReady()) {
            stateMachine.setState(this, new ReadyState(stateMachine, sharedResources));
        }

        if (state == SHNService.State.Error) {
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
        }
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            disconnect();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state InitializingServicesState");
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
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
