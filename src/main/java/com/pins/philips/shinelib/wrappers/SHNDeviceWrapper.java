package com.pins.philips.shinelib.wrappers;

import android.os.Handler;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNCapabilityType;
import com.pins.philips.shinelib.SHNDevice;
import com.pins.philips.shinelib.SHNService;

import java.util.Set;

/**
 * Created by 310188215 on 05/05/15.
 */
public class SHNDeviceWrapper implements SHNDevice, SHNDevice.SHNDeviceListener {
    private final SHNDevice shnDevice;
    private final Handler internalHandler;
    private final Handler userHandler;
    private SHNDeviceListener shnDeviceListener;

    public SHNDeviceWrapper(SHNDevice shnDevice, Handler internalHandler, Handler userHandler) {
        this.shnDevice = shnDevice;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        shnDevice.setShnDeviceListener(this);
    }

    // implements SHNDevice
    @Override
    public State getState() {
        return shnDevice.getState();
    }

    @Override
    public String getAddress() {
        return shnDevice.getAddress();
    }

    @Override
    public String getName() {
        return shnDevice.getName();
    }

    @Override
    public void connect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDevice.connect();
            }
        };
        internalHandler.post(runnable);
    }

    @Override
    public void disconnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDevice.disconnect();
            }
        };
        internalHandler.post(runnable);
    }

    @Override
    public void setShnDeviceListener(SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return shnDevice.getSupportedCapabilityTypes();
    }

    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return shnDevice.getCapabilityForType(type);
    }

    // implements SHNDevice.SHNDeviceListener
    @Override
    public void onStateUpdated(final SHNDevice shnDevice) {
        if (shnDeviceListener != null) {
            userHandler.post(new Runnable() {
                @Override
                public void run() {
                    shnDeviceListener.onStateUpdated(shnDevice);
                }
            });
        }
    }
}
