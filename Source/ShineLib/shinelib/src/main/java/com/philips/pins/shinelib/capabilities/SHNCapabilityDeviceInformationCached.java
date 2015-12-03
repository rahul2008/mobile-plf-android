/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.services.SHNServiceDeviceInformation;
import com.philips.pins.shinelib.utility.DeviceInformationCache;

import java.util.Date;

public class SHNCapabilityDeviceInformationCached implements SHNCapabilityDeviceInformation {

    @NonNull
    private final SHNServiceDeviceInformation shnServiceDeviceInformation;

    @NonNull
    private final DeviceInformationCache deviceInformationCache;

    public SHNCapabilityDeviceInformationCached(@NonNull final SHNServiceDeviceInformation shnServiceDeviceInformation, @NonNull final DeviceInformationCache deviceInformationCache) {
        this.shnServiceDeviceInformation = shnServiceDeviceInformation;
        this.deviceInformationCache = deviceInformationCache;
    }

    @Deprecated
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        shnServiceDeviceInformation.readDeviceInformation(shnDeviceInformationType, shnStringResultListener);
    }

    @Override
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener) {
        shnServiceDeviceInformation.readDeviceInformation(deviceInformationType, new Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date lastCacheUpdate) {
                deviceInformationCache.save(deviceInformationType, value);
                listener.onDeviceInformation(deviceInformationType, value, lastCacheUpdate);
            }

            @Override
            public void onError(@NonNull final SHNDeviceInformationType informationType, @NonNull final SHNResult error) {
                String value = deviceInformationCache.getValue(informationType);
                Date date = deviceInformationCache.getDate(informationType);

                if (value == null || date == null) {
                    listener.onError(informationType, error);
                } else {
                    listener.onDeviceInformation(informationType, value, date);
                }
            }
        });
    }
}
