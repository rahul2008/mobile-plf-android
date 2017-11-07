package com.philips.cdp2.commlib.devicetest.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;


public abstract class BaseAppliance extends Appliance {
    @NonNull
    private final CombinedCommunicationTestingStrategy communicationStrategy;

    public BaseAppliance(@NonNull NetworkNode networkNode, final @NonNull CombinedCommunicationTestingStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        this.communicationStrategy = communicationStrategy;

    }

    public CombinedCommunicationTestingStrategy getCommunicationStrategy() {
        return communicationStrategy;
    }
}
