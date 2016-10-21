package com.philips.cdp2.commlib;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.pins.shinelib.SHNCentral;

public class CommLibContext<A extends DICommAppliance> {

    @NonNull
    private final DiscoveryManager<A> mDiscoveryManager;

    @NonNull
    private final BleDeviceCache mBleDeviceCache;

    @NonNull
    private final SHNCentral mShnCentral;

    CommLibContext(final @NonNull DiscoveryManager<A> discoveryManager, final @NonNull SHNCentral shnCentral) {
        mDiscoveryManager = discoveryManager;
        mShnCentral = shnCentral;
        mBleDeviceCache = new BleDeviceCache();
        // TODO register BleDeviceCache as device listener on SHNDeviceScanner
    }

    @NonNull
    public DiscoveryManager<A> getDiscoveryManager() {
        return mDiscoveryManager;
    }

    @NonNull
    public SHNCentral getShnCentral() {
        return mShnCentral;
    }
}
