/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivity.demouapp;

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
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RefAppApplianceFactory implements ApplianceFactory {
    public static final String TAG = RefAppApplianceFactory.class.getSimpleName();
    public static final String PRODUCT_STUB_BOARD = "PS1234";
    public static final String DI_COMM_BLE_REFERENCE = "DiCommBLEReference";

    @NonNull
    private final BleTransportContext bleTransportContext;

    @NonNull
    private final LanTransportContext lanTransportContext;

    @NonNull
    private final CloudTransportContext cloudTransportContext;

    private ConnectivityDeviceType deviceType;

    public RefAppApplianceFactory(@NonNull final BleTransportContext bleTransportContext, @NonNull final LanTransportContext lanTransportContext, @NonNull final CloudTransportContext cloudTransportContext) {
        this.bleTransportContext = bleTransportContext;
        this.lanTransportContext = lanTransportContext;
        this.cloudTransportContext = cloudTransportContext;
    }

    public void setDeviceType(ConnectivityDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return networkNode.isValid();
    }

    @Override
    public Appliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            RALog.i(TAG, networkNode.getDeviceType());
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
                    if(networkNode.getName().equals(DI_COMM_BLE_REFERENCE) ||
                            networkNode.getModelId().equals(PRODUCT_STUB_BOARD)) {
                        return new RefAppBleReferenceAppliance(networkNode, communicationStrategy, deviceType);
                    }
                    else {
                        return new BleReferenceAppliance(networkNode, communicationStrategy);
                    }
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
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(RefAppBleReferenceAppliance.MODELNAME);
            add(RefAppBleReferenceAppliance.MODEL_NAME_HH1600);
            add(RefAppBleReferenceAppliance.MODEL_NAME_HHS);
        }});
    }
}