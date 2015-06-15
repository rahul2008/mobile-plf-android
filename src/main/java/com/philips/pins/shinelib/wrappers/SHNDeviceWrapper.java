package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.util.Log;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 05/05/15.
 */
public class SHNDeviceWrapper implements SHNDevice, SHNDevice.SHNDeviceListener {
    private static final String TAG = SHNDeviceWrapper.class.getSimpleName();
    private static final boolean LOGGING = false;
    private final SHNDevice shnDevice;
    private static Handler tempInternalHandler;
    private static Handler tempUserHandler;
    private final Handler internalHandler;
    private final Handler userHandler;
    private List<SHNDeviceListener> shnDeviceListeners;

    public static void setHandlers(Handler internalHandler, Handler userHandler) {
        tempInternalHandler = internalHandler;
        tempUserHandler = userHandler;
    }

    public SHNDeviceWrapper(SHNDevice shnDevice) {
        this.shnDevice = shnDevice;
        this.internalHandler = tempInternalHandler;
        this.userHandler = tempUserHandler;
        shnDevice.registerSHNDeviceListener(this);
        shnDeviceListeners = new ArrayList<>();
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
    public String getDeviceTypeName() {
        return shnDevice.getDeviceTypeName();
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
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        synchronized (shnDeviceListeners) {
            if (!shnDeviceListeners.contains(shnDeviceListener)) {
                shnDeviceListeners.add(shnDeviceListener);
            }
        }
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        synchronized (shnDeviceListeners) {
            shnDeviceListeners.remove(shnDeviceListener);
        }
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
        synchronized (shnDeviceListeners) {
            for (final SHNDeviceListener shnDeviceListener: shnDeviceListeners) {
                if (shnDeviceListener != null) {
                    if (LOGGING) Log.i(TAG, "posting onStateUpdated() to the user");
                    userHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            shnDeviceListener.onStateUpdated(shnDevice);
                        }
                    });
                }
            }
        }
    }
}
