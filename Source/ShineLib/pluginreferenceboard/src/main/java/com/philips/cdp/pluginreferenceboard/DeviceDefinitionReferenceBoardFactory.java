package com.philips.cdp.pluginreferenceboard;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBatteryImpl;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformationImpl;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.services.SHNServiceBattery;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;

class DeviceDefinitionReferenceBoardFactory {

    public SHNDeviceImpl createDevice(String deviceAddress, SHNCentral shnCentral, String deviceTypeName) {
        return new SHNDeviceImpl(shnCentral.getBTDevice(deviceAddress), shnCentral, deviceTypeName);
    }

    public SHNServiceBattery createBatteryService() {
        SHNFactory shnFactory = new SHNFactory(null);
        return new SHNServiceBattery(shnFactory);
    }

    public SHNCapabilityBattery createBatteryCapability(SHNServiceBattery serviceBattery) {
        return new SHNCapabilityBatteryImpl(serviceBattery);
    }

    public SHNServiceDeviceInformation createDeviceInformationService() {
        return new SHNServiceDeviceInformation();
    }

    public SHNCapabilityDeviceInformation createDeviceInformationCapability(SHNServiceDeviceInformation serviceDeviceInformation) {
        return new SHNCapabilityDeviceInformationImpl(serviceDeviceInformation);
    }
}
