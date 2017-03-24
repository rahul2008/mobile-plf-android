package com.philips.cdpp.dicommtestapp.strategy;

import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class WlanCommStrategy implements CommStrategy
{
    private final LanTransportContext mWifiContext;

    public WlanCommStrategy(LanTransportContext transportContext) {
        this.mWifiContext = transportContext;
    }

    @Override
    public CommStrategyType getType() {
        return CommStrategyType.LAN;
    }

    @Override
    public TransportContext getTransportContext() {
        return mWifiContext;
    }
}
