package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;

public interface SHNCapabilityDataModelDebuggingMoonshine extends SHNCapability {

    void setEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> listener);

    void writeInput(@NonNull final String message, @NonNull final ResultListener<String> listener);
}
