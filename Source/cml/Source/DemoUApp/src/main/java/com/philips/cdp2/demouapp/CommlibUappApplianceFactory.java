/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CombinedCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;
import com.philips.cdp2.demouapp.appliance.airpurifier.ComfortAirPurifier;
import com.philips.cdp2.demouapp.appliance.airpurifier.JaguarAirPurifier;
import com.philips.cdp2.demouapp.appliance.brighteyes.BrightEyesAppliance;
import com.philips.cdp2.demouapp.appliance.polaris.PolarisAppliance;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.cdp2.demouapp.appliance.reference.WifiReferenceAppliance;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class CommlibUappApplianceFactory implements ApplianceFactory {

    @NonNull
    private final BleTransportContext bleTransportContext;

    @NonNull
    private final LanTransportContext lanTransportContext;

    @NonNull
    private final CloudTransportContext cloudTransportContext;

    public CommlibUappApplianceFactory(@NonNull final BleTransportContext bleTransportContext, @NonNull final LanTransportContext lanTransportContext, @NonNull final CloudTransportContext cloudTransportContext) {
        this.bleTransportContext = bleTransportContext;
        this.lanTransportContext = lanTransportContext;
        this.cloudTransportContext = cloudTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return networkNode.isValid();
    }

    @Override
    public Appliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = new CombinedCommunicationStrategy(
                    bleTransportContext.createCommunicationStrategyFor(networkNode),
                    lanTransportContext.createCommunicationStrategyFor(networkNode),
                    cloudTransportContext.createCommunicationStrategyFor(networkNode));

            switch (networkNode.getDeviceType()) {
                case AirPurifier.DEVICETYPE:
                    networkNode.useLegacyHttp();

                    if (ComfortAirPurifier.MODELID.equals(networkNode.getModelId())) {
                        return new ComfortAirPurifier(networkNode, communicationStrategy);
                    } else {
                        return new JaguarAirPurifier(networkNode, communicationStrategy);
                    }
                case BleReferenceAppliance.DEVICETYPE:
                    return new BleReferenceAppliance(networkNode, communicationStrategy);
                case WifiReferenceAppliance.DEVICETYPE:
                    return new WifiReferenceAppliance(networkNode, communicationStrategy);
                case BrightEyesAppliance.DEVICETYPE:
                    return new BrightEyesAppliance(networkNode, communicationStrategy);
                case PolarisAppliance.DEVICETYPE:
                    PolarisAppliance polaris = new PolarisAppliance(networkNode, communicationStrategy);
                    polaris.usesHttps(true);
                    return polaris;
                default:
                    return new Appliance(networkNode, communicationStrategy) {
                        @Override
                        public String getDeviceType() {
                            return null;
                        }
                    };
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        return Collections.unmodifiableSet(new HashSet<String>());
    }
}
