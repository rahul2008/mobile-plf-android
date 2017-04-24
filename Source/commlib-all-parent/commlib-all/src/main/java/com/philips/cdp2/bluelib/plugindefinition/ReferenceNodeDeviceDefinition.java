package com.philips.cdp2.bluelib.plugindefinition;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNSharedConnectionDevice;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformationImpl;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;

class ReferenceNodeDeviceDefinition implements SHNDeviceDefinitionInfo.SHNDeviceDefinition {
    @Override
    public SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral) {
        SHNDeviceImpl shnDevice = new SHNDeviceImpl(shnCentral.getBTDevice(deviceAddress), shnCentral, shnDeviceDefinitionInfo.getDeviceTypeName());

        // DiComm streaming
        /*final SHNServiceDiCommStreaming shnServiceDiCommStreaming = new SHNServiceDiCommStreaming();
        shnDevice.registerService(shnServiceDiCommStreaming);

        final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming = new SHNProtocolByteStreamingVersionSwitcher(shnServiceDiCommStreaming, shnCentral.getInternalHandler());
        shnServiceDiCommStreaming.setShnServiceMoonshineStreamingListener(shnProtocolMoonshineStreaming);

        CapabilityDiComm capabilityDiComm = new StreamingCapability(shnProtocolMoonshineStreaming);
        shnDevice.registerCapability(CapabilityDiComm.class, capabilityDiComm);*/

        // Device Information
        final SHNServiceDeviceInformation shnServiceDeviceInformation = new SHNServiceDeviceInformation();
        shnDevice.registerService(shnServiceDeviceInformation);

        SHNCapabilityDeviceInformation capabilityDeviceInformation =  new SHNCapabilityDeviceInformationImpl(shnServiceDeviceInformation);
        shnDevice.registerCapability(capabilityDeviceInformation, SHNCapabilityType.DEVICE_INFORMATION);

        return new SHNSharedConnectionDevice(shnDevice);
    }
}
