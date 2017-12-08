/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.utility.DeviceInformationCache;

import java.util.Date;

/**
 * Caching decorator for{@link SHNCapabilityDeviceInformation}. As soon as the {@link SHNService} becomes ready this decorator will query for all values of {@link SHNDeviceInformationType} and cache them.<br/>
 * Every time When data is returned successfully, the value is cached. When an error occurs during retrieving of the data (for example when the device is not connected), but there is data in the cache, the cached
 * data will be returned to the listener.
 *
 * @publicPluginApi
 */
public class SHNCapabilityDeviceInformationCached implements SHNCapabilityDeviceInformation {

    @NonNull
    private final SHNCapabilityDeviceInformation shnServiceDeviceInformation;

    @NonNull
    private final DeviceInformationCache deviceInformationCache;

    public SHNCapabilityDeviceInformationCached(@NonNull final SHNCapabilityDeviceInformation capabilityDeviceInformation, @NonNull final SHNService shnService, @NonNull final DeviceInformationCache deviceInformationCache) {
        this.shnServiceDeviceInformation = capabilityDeviceInformation;
        this.deviceInformationCache = deviceInformationCache;

        shnService.registerSHNServiceListener(new SHNService.SHNServiceListener() {
            @Override
            public void onServiceStateChanged(final SHNService shnService, final SHNService.State state) {
                if (SHNService.State.Ready == state) {
                    initCache();
                }
            }
        });
    }

    @Deprecated
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        readDeviceInformation(shnDeviceInformationType, new Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired) {
                shnStringResultListener.onActionCompleted(value, SHNResult.SHNOk);
            }

            @Override
            public void onError(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error) {
                shnStringResultListener.onActionCompleted(null, error);
            }
        });
    }

    @Override
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener) {
        shnServiceDeviceInformation.readDeviceInformation(deviceInformationType, new Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired) {
                deviceInformationCache.save(deviceInformationType, value);
                listener.onDeviceInformation(deviceInformationType, value, dateWhenAcquired);
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

    public void initCache() {
        for (SHNDeviceInformationType value : SHNDeviceInformationType.values()) {
            readDeviceInformation(value, new Listener() {
                @Override
                public void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired) {
                }

                @Override
                public void onError(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error) {
                }
            });
        }
    }
}
