package com.philips.cdp.pluginreferenceboard;

import android.os.Handler;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.capabilities.CapabilityFirmwareUpdateDiComm;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBatteryImpl;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformationCached;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformationImpl;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.dicommsupport.DiCommChannel;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreamingVersionSwitcher;
import com.philips.pins.shinelib.services.SHNServiceBattery;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;
import com.philips.pins.shinelib.services.SHNServiceMoonshineStreaming;
import com.philips.pins.shinelib.utility.DeviceInformationCache;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

class DeviceDefinitionReferenceBoard implements SHNDeviceDefinitionInfo.SHNDeviceDefinition {

    public static final int RESPONSE_TIME_OUT = 4000;

    @Override
    public SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral) {
        // using existing building block from BlueLib. It is also possible to create custom implementation inside the plugin
        SHNDeviceImpl device = new SHNDeviceImpl(shnCentral.getBTDevice(deviceAddress), shnCentral, shnDeviceDefinitionInfo.getDeviceTypeName());

        // only registered capabilities are exposed to the user of the plugin
        registerBatteryCapability(device);
        registerDeviceInformationCapability(shnCentral, device);
        registerFirmwareUpdateCapability(shnCentral.getInternalHandler(), device);

        // the SHNDevice implementation needs to be wrapped for thread safety
        return new SHNDeviceWrapper(device);
    }

    private void registerBatteryCapability(SHNDeviceImpl device) {
        SHNServiceBattery batteryService = new SHNServiceBattery(new SHNFactory(null));
        // it is important to register the service for SHNDeviceImpl. SHNDeviceImpl is waiting for all registered services to indicate 'ready' before changing state to 'Connected'
        device.registerService(batteryService.getShnService());

        SHNCapabilityBattery capabilityBattery = new SHNCapabilityBatteryImpl(batteryService);
        device.registerCapability(capabilityBattery, SHNCapabilityType.BATTERY);
    }

    private void registerDeviceInformationCapability(SHNCentral shnCentral, SHNDeviceImpl device) {
        SHNServiceDeviceInformation shnServiceDeviceInformation = new SHNServiceDeviceInformation();
        // it is important to register the service for SHNDeviceImpl. SHNDeviceImpl is waiting for all registered services to indicate 'ready' before changing state to 'Connected'
        device.registerService(shnServiceDeviceInformation);

        SHNCapabilityDeviceInformation capabilityDeviceInformation = new SHNCapabilityDeviceInformationImpl(shnServiceDeviceInformation);
        // register the capability directly
        // device.registerCapability(capabilityDeviceInformation, SHNCapabilityType.DEVICE_INFORMATION);
        // or use SHNCapabilityDeviceInformationCached. SHNCapabilityDeviceInformationCached is a wrapper around SHNCapabilityDeviceInformation that provides access to device information then device is not connected.
        // SHNCapabilityDeviceInformationCached stores  device information in SharedPreferences.
        PersistentStorageFactory persistentStorageFactory = shnCentral.getPersistentStorageFactory();
        DeviceInformationCache deviceInformationCache = new DeviceInformationCache(persistentStorageFactory.getPersistentStorageForDevice(device));
        SHNCapabilityDeviceInformationCached deviceInformationCached = new SHNCapabilityDeviceInformationCached(capabilityDeviceInformation, shnServiceDeviceInformation, deviceInformationCache);
        device.registerCapability(deviceInformationCached, SHNCapabilityType.DEVICE_INFORMATION);
    }

    private void registerFirmwareUpdateCapability(Handler internalHandler, SHNDeviceImpl device) {
        SHNServiceMoonshineStreaming shnServiceMoonshineStreaming = new SHNServiceMoonshineStreaming();
        // it is important to register the service for SHNDeviceImpl. SHNDeviceImpl is waiting for all registered services to indicate 'ready' before changing state to 'Connected'
        device.registerService(shnServiceMoonshineStreaming);

        SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming = new SHNProtocolMoonshineStreamingVersionSwitcher(shnServiceMoonshineStreaming, internalHandler);
        // Let the protocol know about service state changes
        shnServiceMoonshineStreaming.setShnServiceMoonshineStreamingListener(shnProtocolMoonshineStreaming);

        // create DiComm port and channel
        DiCommChannel commChannel = new DiCommChannel(shnProtocolMoonshineStreaming, RESPONSE_TIME_OUT);
        DiCommFirmwarePort firmwarePort = new DiCommFirmwarePort(internalHandler);
        // add supported ports to the channel
        commChannel.addPort(firmwarePort);

        // register the capability
        SHNCapabilityFirmwareUpdate capabilityFirmwareUpdate = new CapabilityFirmwareUpdateDiComm(firmwarePort, internalHandler);
        device.registerCapability(capabilityFirmwareUpdate, SHNCapabilityType.FIRMWARE_UPDATE);
    }
}
