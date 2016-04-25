package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Map;

class DiCommPort implements SHNMapResultListener<String, Object> {

    public static final String TAG = "DiCommPort";
    @NonNull
    private String name;
    @Nullable
    private Listener listener;
    @Nullable
    private DiCommChannel diCommChannel;

    private boolean isAvailable;
    private Map<String, Object> properties;

    public DiCommPort(String name) {
        this.name = name;
    }

    public void setDiCommChannel(DiCommChannel diCommChannel) {
        this.diCommChannel = diCommChannel;
    }

    public void onChannelAvailabilityChanged(boolean isAvailable) {
        if (isAvailable) {
            if (diCommChannel != null) {
                diCommChannel.reloadProperties(name, this);
            }
        } else {
            setIsAvailable(false);
        }
    }

    private void setIsAvailable(boolean isAvailable) {
        if (this.isAvailable != isAvailable) {
            this.isAvailable = isAvailable;
            if (isAvailable) {
                if (listener != null) {
                    listener.onPortAvailable(this);
                }
            } else {
                if (listener != null) {
                    listener.onPortUnavailable(this);
                }
            }
        }
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public void onActionCompleted(Map<String, Object> properties, @NonNull SHNResult result) {
        if (result == SHNResult.SHNOk) {
            this.properties = properties;
            isAvailable = true;
            if (listener != null) {
                listener.onPortAvailable(this);
            }
        } else {
            SHNLogger.d(TAG, "Failed to load properties result: " + result);
        }
    }

    public Map<String,Object> getProperties() {
        return properties;
    }

    public interface Listener {
        void onPortAvailable(DiCommPort diCommPort);

        void onPortUnavailable(DiCommPort diCommPort);
    }

    public interface UpdateListener {
        void onPropertiesChanged(@NonNull Map<String, Object> properties);

        void onSubscriptionFailed(SHNResult shnResult);
    }
}
