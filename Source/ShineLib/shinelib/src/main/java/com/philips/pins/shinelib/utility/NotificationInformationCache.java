package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;

/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NotificationInformationCache {
    @NonNull
    private final PersistentStorage persistentStorage;

    public NotificationInformationCache(@NonNull final PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }
   //TODO storage functionality
    /* public void save(  SHNCapabilityNotifications.NotificationIDs notificationID,  SHNCapabilityNotifications.NotificationID value) {
        Log.i("notifications=","^====="+notificationID.name());
          persistentStorage.put(notificationID.getNotificationID().name(), value);

    }
    public void saveNotificationType(SHNCapabilityNotifications.NotificationType notificationType, String value) {
        Log.i("notifications=","^====="+notificationType.name());
        persistentStorage.put(notificationType.name(), value);

    }*/
}
