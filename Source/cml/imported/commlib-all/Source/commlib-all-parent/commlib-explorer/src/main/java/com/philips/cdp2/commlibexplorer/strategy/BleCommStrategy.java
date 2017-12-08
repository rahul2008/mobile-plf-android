/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.strategy;

import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.context.TransportContext;

public class BleCommStrategy implements CommStrategy {
    private final BleTransportContext bleContext;

    public BleCommStrategy(BleTransportContext transportContext) {
        this.bleContext = transportContext;
    }

    @Override
    public CommStrategyType getType() {
        return CommStrategyType.BLE;
    }

    @Override
    public TransportContext getTransportContext() {
        return this.bleContext;
    }
}
