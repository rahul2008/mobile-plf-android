package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityNotifications {
    public  enum SHNNotificationType {
        SHNNotificationTypeEmail
    }

    void showNotificationForType(SHNNotificationType shnNotificationType, SHNResultListener shnResultListener);
    void hideNotificationForType(SHNNotificationType shnNotificationType, SHNResultListener shnResultListener);
}
