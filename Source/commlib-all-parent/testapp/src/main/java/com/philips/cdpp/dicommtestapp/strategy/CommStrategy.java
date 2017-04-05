package com.philips.cdpp.dicommtestapp.strategy;

import com.philips.cdp2.commlib.core.context.TransportContext;

public interface CommStrategy
{
    enum CommStrategyType {
        LAN("LAN"), BLE("BLE");

        private String val;

        CommStrategyType(String val) {
            this.val = val;
        }

        public String value() {
            return val;
        }
    }

    CommStrategyType getType();
    TransportContext getTransportContext();
}
