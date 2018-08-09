package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

public abstract class SHNDeviceState extends State<SHNDeviceStateMachine> {

    protected String logTag;

    protected final SHNDeviceResources sharedResources;

    public SHNDeviceState(@NonNull SHNDeviceStateMachine stateMachine, String logTag) {
        super(stateMachine);
        this.logTag = logTag;
        sharedResources = stateMachine.getSharedResources();
    }

    public abstract SHNDevice.State getExternalState();

    public void connect() {
        showLogOfEmptyMethodCall("connect");
    }

    public void connect(long connectTimeOut) {
        showLogOfEmptyMethodCall("connect");
    }

    public void disconnect() {
        showLogOfEmptyMethodCall("disconnect");
    }

    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        showLogOfEmptyMethodCall("onServiceStateChanged");
    }

    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        showLogOfEmptyMethodCall("onBondStatusChanged");
    }

    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        showLogOfEmptyMethodCall("onStateUpdated");
    }

    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        showLogOfEmptyMethodCall("onConnectionStateChange");
    }

    public void onServicesDiscovered(BTGatt gatt, int status) {
        showLogOfEmptyMethodCall("onServicesDiscovered");
    }

    private void showLogOfEmptyMethodCall(String methodName) {
        SHNLogger.d(logTag, String.format("Called empty implementation of %s, current state: %s", methodName, this.stateMachine.getState().getClass().getSimpleName()));
    }

    public String getLogTag() {
        return logTag;
    }
}