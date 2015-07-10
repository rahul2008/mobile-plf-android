package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 09/07/15.
 */
public interface SHNCapabilityUserControlPoint extends SHNCapability {
    public interface SHNCapabilityUserControlPointListener {
        void onAutoConsentFailed(short userIndex, int consentCode, SHNResult shnResult);
        void onMismatchedDatabaseIncrement(short userIndex, long localIncrement, long remoteIncrement);
    }

    void setSHNCapabilityUserControlPointListener(SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener);
    short getCurrentUserIndex();
    int getCurrentConsentCode();
    void registerNewUser(short consentCode, SHNIntegerResultListener shnIntegerResultListener);
    void setCurrentUser(short userIndex, int consentCode, SHNResultListener shnResultListener);
    void deleteCurrentUser(SHNResultListener shnResultListener);
    void pushUserConfiguration(SHNResultListener shnResultListener);
}
