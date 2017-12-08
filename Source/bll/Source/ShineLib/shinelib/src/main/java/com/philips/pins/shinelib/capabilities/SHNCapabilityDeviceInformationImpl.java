/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;

/**
 * Default implementation for {@link SHNCapabilityDeviceInformation}.
 *
 * @publicPluginApi
 */
public class SHNCapabilityDeviceInformationImpl implements SHNCapabilityDeviceInformation {
    private final SHNServiceDeviceInformation shnServiceDeviceInformation;

    public SHNCapabilityDeviceInformationImpl(SHNServiceDeviceInformation shnServiceDeviceInformation) {
        this.shnServiceDeviceInformation = shnServiceDeviceInformation;
    }

    @Deprecated
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        shnServiceDeviceInformation.readDeviceInformation(shnDeviceInformationType, shnStringResultListener);
    }

    @Override
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener) {
        shnServiceDeviceInformation.readDeviceInformation(deviceInformationType, listener);
    }
}
