package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;

/**
 * Created by 310188215 on 31/03/15.
 */
public class SHNCapabilityDeviceInformationImpl implements SHNCapabilityDeviceInformation {
    private final SHNServiceDeviceInformation shnServiceDeviceInformation;

    public SHNCapabilityDeviceInformationImpl(SHNServiceDeviceInformation shnServiceDeviceInformation) {
        this.shnServiceDeviceInformation = shnServiceDeviceInformation;
    }

    public void readDeviceInformation(SHNDeviceInformationType shnDeviceInformationType, SHNStringResultListener shnStringResultListener) {
        shnServiceDeviceInformation.readDeviceInformation(shnDeviceInformationType, shnStringResultListener);
    }
}
