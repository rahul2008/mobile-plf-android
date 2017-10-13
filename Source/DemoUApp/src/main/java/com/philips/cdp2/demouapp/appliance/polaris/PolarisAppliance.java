/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.demouapp.appliance.polaris;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CombinedCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class PolarisAppliance extends Appliance {

    public static final String DEVICETYPE = "Polaris";

    /**
     * Create an appliance.
     * <p>
     * Appliances are usually created inside the {@link ApplianceFactory}. If multiple
     * <code>CommunicationStrategies</code> are provided, they are wrapped with a {@link CombinedCommunicationStrategy}.
     *
     * @param networkNode             The {@link NetworkNode} this <code>Appliance</code> is discovered with.
     * @param communicationStrategies One or multiple <code>CommunicationStrategies</code> used to communicate with the Appliance.
     *                                The order in which they are supplied determines the order in which communication is attemped.
     * @see ApplianceFactory
     */
    public PolarisAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy... communicationStrategies) {
        super(networkNode, communicationStrategies);
    }

    public void usesHttps(boolean isUsed) {
        if(!isUsed) {
            networkNode.useLegacyHttp();
        }
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }
}
