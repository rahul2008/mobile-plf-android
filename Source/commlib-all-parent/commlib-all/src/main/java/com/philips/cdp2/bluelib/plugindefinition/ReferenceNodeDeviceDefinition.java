package com.philips.cdp2.bluelib.plugindefinition;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreamingVersionSwitcher;
import com.philips.pins.shinelib.services.SHNServiceMoonshineStreaming;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

public class ReferenceNodeDeviceDefinition implements SHNDeviceDefinitionInfo.SHNDeviceDefinition {
    @Override
    public SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral) {
        SHNDeviceImpl shnDevice = new SHNDeviceImpl(shnCentral.getBTDevice(deviceAddress), shnCentral, shnDeviceDefinitionInfo.getDeviceTypeName());

        final SHNServiceMoonshineStreaming shnServiceMoonshineStreaming = new SHNServiceMoonshineStreaming();
        shnDevice.registerService(shnServiceMoonshineStreaming);

        final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming = new SHNProtocolMoonshineStreamingVersionSwitcher(shnServiceMoonshineStreaming, shnCentral.getInternalHandler());
        shnServiceMoonshineStreaming.setShnServiceMoonshineStreamingListener(shnProtocolMoonshineStreaming);

        com.philips.pins.shinelib.capabilities.CapabilityDiComm capabilityDiComm = new StreamingCapability(shnProtocolMoonshineStreaming);
        shnDevice.registerCapability(capabilityDiComm, SHNCapabilityType.DI_COMM);

        return new SHNDeviceWrapper(shnDevice);
    }
}
