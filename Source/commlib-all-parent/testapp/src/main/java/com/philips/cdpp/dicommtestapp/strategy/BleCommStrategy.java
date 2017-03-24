package com.philips.cdpp.dicommtestapp.strategy;

import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.context.TransportContext;

public class BleCommStrategy implements CommStrategy
{
    private final BleTransportContext mBleContext;

    public BleCommStrategy(BleTransportContext transportContext) {
        this.mBleContext = transportContext;
    }

    @Override
    public CommStrategyType getType() {
        return CommStrategyType.BLE;
    }

    @Override
    public TransportContext getTransportContext() {
        return this.mBleContext;
    }
}
