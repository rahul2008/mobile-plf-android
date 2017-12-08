/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.strategy;

import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class LanCommStrategy implements CommStrategy {
    private final LanTransportContext lanContext;

    public LanCommStrategy(LanTransportContext transportContext) {
        this.lanContext = transportContext;
    }

    @Override
    public CommStrategyType getType() {
        return CommStrategyType.LAN;
    }

    @Override
    public TransportContext getTransportContext() {
        return lanContext;
    }
}
