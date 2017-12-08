package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

public interface SHNCapabilityClearUserData extends SHNCapability {
    void clearUserData(@NonNull final SHNResultListener listener);
}
