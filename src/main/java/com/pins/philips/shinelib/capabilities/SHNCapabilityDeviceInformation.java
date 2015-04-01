package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNStringResultListener;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityDeviceInformation extends SHNCapability {
    public enum SHNDeviceInformationType {
        ManufacturerName,
        ModelNumber,
        SerialNumber,
        HardwareRevision,
        FirmwareRevision,
        SoftwareRevision,
        SystemID,
        Unknown
    }

    public void readDeviceInformation(SHNDeviceInformationType shnDeviceInformationType, SHNStringResultListener shnStringResultListener);
}
