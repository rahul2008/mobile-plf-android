/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.plugindefinition;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolByteStreamingVersionSwitcher;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import com.philips.pins.shinelib.services.SHNServiceDiCommStreaming;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

class ReferenceNodeDeviceDefinition implements SHNDeviceDefinitionInfo.SHNDeviceDefinition {
    @Override
    public SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral) {
        SHNDeviceImpl shnDevice = new SHNDeviceImpl(shnCentral.getBTDevice(deviceAddress), shnCentral, shnDeviceDefinitionInfo.getDeviceTypeName());

        final SHNServiceDiCommStreaming shnServiceDiCommStreaming = new SHNServiceDiCommStreaming();
        shnDevice.registerService(shnServiceDiCommStreaming);

        final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming = new SHNProtocolByteStreamingVersionSwitcher(shnServiceDiCommStreaming, shnCentral.getInternalHandler());
        shnServiceDiCommStreaming.setShnServiceMoonshineStreamingListener(shnProtocolMoonshineStreaming);

        CapabilityDiComm capabilityDiComm = new StreamingCapability(shnProtocolMoonshineStreaming);
        shnDevice.registerCapability(capabilityDiComm, SHNCapabilityType.DI_COMM);

        return new SHNDeviceWrapper(shnDevice);
    }
}
