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
        void onAutoConsentFailed(int userIndex, int consentCode, SHNResult shnResult);
        void onMismatchedDatabaseIncrement(int userIndex);
    }

    void setSHNCapabilityUserControlPointListener(SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener);
    int getCurrentUserIndex();
    int getCurrentConsentCode();
    void registerNewUser(int consentCode, SHNIntegerResultListener shnIntegerResultListener);
    void setCurrentUser(int userIndex, int consentCode, SHNResultListener shnResultListener);
    void deleteCurrentUser(SHNResultListener shnResultListener);
    void pushUserConfiguration(SHNResultListener shnResultListener);
}
