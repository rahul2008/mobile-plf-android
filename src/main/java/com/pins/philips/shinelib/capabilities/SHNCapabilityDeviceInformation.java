package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNStringResultListener;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNCapabilityDeviceInformation {
    public enum SHNDeviceInformationType {
        SHNDeviceInformationTypeManufacturerName,
        SHNDeviceInformationTypeModelNumber,
        SHNDeviceInformationTypeSerialNumber,
        SHNDeviceInformationTypeHardwareRevision,
        SHNDeviceInformationTypeFirmwareRevision,
        SHNDeviceInformationTypeSoftwareRevision,
        SHNDeviceInformationTypeSystemID,
        SHNDeviceInformationTypeUnknown
    }

    public void readDeviceInformation(SHNDeviceInformationType shnDeviceInformationType, SHNStringResultListener shnStringResultListener) { throw new UnsupportedOperationException(); }
}
