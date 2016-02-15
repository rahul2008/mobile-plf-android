package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;

public interface SHNCapabilityDataModelDebugging extends SHNCapability {

    void setEnabled(final boolean enabled);

    void writeInput(@NonNull final String message);

    void setOutputListener(@Nullable final ResultListener<String> listener);
}
