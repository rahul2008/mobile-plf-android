/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity.appliance;


import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BleReferenceApplianceFactory implements DICommApplianceFactory<BleReferenceAppliance> {
    @NonNull
    public static final String TAG = "BleReferenceApplianceFactory";
    public static final long serialVersionUID = 11L;

    private final BleTransportContext bleTransportContext;


    public BleReferenceApplianceFactory(@NonNull BleTransportContext bleTransportContext) {
        this.bleTransportContext = bleTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        RALog.d(TAG," To check if appliance for node be created ");
        return BleReferenceAppliance.MODELNAME.equals(networkNode.getDeviceType());
    }

    @Override
    public BleReferenceAppliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = bleTransportContext.createCommunicationStrategyFor(networkNode);

            return new BleReferenceAppliance(networkNode, communicationStrategy);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedModelNames() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(BleReferenceAppliance.MODELNAME);
        }});
    }
}
