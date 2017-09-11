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
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BleReferenceApplianceFactory implements DICommApplianceFactory<BleReferenceAppliance> {
    @NonNull
    public static final String TAG = "BleReferenceApplianceFactory";

    private final BleTransportContext bleTransportContext;

    private ConnectivityDeviceType deviceType;


    public BleReferenceApplianceFactory(@NonNull BleTransportContext bleTransportContext, ConnectivityDeviceType deviceType) {
        this.bleTransportContext = bleTransportContext;
        this.deviceType = deviceType;

        RALog.d(TAG, "device type : " + deviceType);
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        RALog.d(TAG, " To check if appliance for node be created ");
        switch (deviceType) {
            case POWER_SLEEP:
                return getSupportedDeviceTypes().contains(networkNode.getModelId());
            default:
                return BleReferenceAppliance.MODELNAME.equals(networkNode.getDeviceType());
        }
    }

    @Override
    public BleReferenceAppliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = bleTransportContext.createCommunicationStrategyFor(networkNode);

            return new BleReferenceAppliance(networkNode, communicationStrategy, deviceType);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(BleReferenceAppliance.MODELNAME);
            add(BleReferenceAppliance.MODEL_NAME_HH1600);
            add(BleReferenceAppliance.MODEL_NAME_HHS);
        }});
    }
}
