/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.strategy;

import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class WlanCommStrategy implements CommStrategy
{
    private final LanTransportContext wifiContext;

    public WlanCommStrategy(LanTransportContext transportContext) {
        this.wifiContext = transportContext;
    }

    @Override
    public CommStrategyType getType() {
        return CommStrategyType.LAN;
    }

    @Override
    public TransportContext getTransportContext() {
        return wifiContext;
    }
}
