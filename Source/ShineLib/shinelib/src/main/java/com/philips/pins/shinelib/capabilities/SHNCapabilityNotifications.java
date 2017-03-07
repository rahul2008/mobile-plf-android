/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.HashMap;
import java.util.Map;

public interface SHNCapabilityNotifications extends SHNCapability {


    enum Type {
        EMAIL,
        IMAGE
    }

    enum Notifications {
        NotificationID,
        NotificationType,
        TransferResult
    }

    enum Vibration {
        NoVibration,
        ShortVibration,
        LongVibration
    }

    public enum NotificationID {
        SmallFaceIcon00(0),
        SmallFaceIcon01(1),
        SmallFaceIcon02(2),
        SmallFaceIcon03(3),
        FullScreenIcon00(100),
        FullScreenIcon01(101),
        FullScreenIcon02(102),
        FullScreenIcon03(103),
        Alarm00(150),
        InvalidNotification(225);

        private int value;
        private static Map map = new HashMap<>();

         NotificationID(int value) {
            this.value = value;
        }

        static {
            for (NotificationID notificationID : NotificationID.values()) {
                map.put(notificationID.value, notificationID);
            }
        }

        public static NotificationID valueOf(int notificationIDs) {
            return (NotificationID) map.get(notificationIDs);
        }

        public int getValue() {
            return value;
        }
    }

    enum NotificationType {
        SmallFaceIcon,
        FullScreenIcon,
        AlarmNotification,
        InvalidIcon
    }

    enum TransferResult {
        Ok,
        SizeError,
        NotStarted,
        UnknownID,
        FunctionalityNotSupported
    }

    class TransferGetCapabilitiesWithResult {
        private NotificationID notificationID;
        private NotificationType notificationType;
        private boolean allocated;
        private boolean isLast;


        public TransferGetCapabilitiesWithResult(NotificationID notificationID, NotificationType notificationType,
                                                 boolean allocated, boolean isLast) {
            this.notificationID = notificationID;
            this.notificationType = notificationType;
            this.allocated = allocated;
            this.isLast = isLast;
        }

        public TransferGetCapabilitiesWithResult(NotificationID notificationID, NotificationType notificationType
        ) {
            this.notificationID = notificationID;
            this.notificationType = notificationType;

        }

        public NotificationID getNotificationID() {
            return notificationID;
        }

        public NotificationType getNotificationType() {
            return notificationType;
        }

        public boolean isAllocated() {
            return allocated;
        }

        public boolean isLast() {
            return isLast;
        }

    }

    class GetNotificationResult {

        private NotificationID notificationID;
        private boolean isSucess;

        public NotificationID getNotificationID() {
            return notificationID;
        }

        public boolean isSucess() {
            return isSucess;
        }

        public GetNotificationResult(NotificationID notificationId, boolean ok) {
            this.notificationID = notificationId;
            this.isSucess = ok;
        }
    }

    class GetRemoveNotification {
        public GetRemoveNotification(boolean ok) {
            this.isNotificationRemoved = ok;
        }

        public boolean isNotificationRemoved() {
            return isNotificationRemoved;
        }

        private boolean isNotificationRemoved;

    }


    class ImageSize {
        private final int width;
        private final int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    void showNotificationForType(Type type, byte[] imageData, SHNResultListener shnResultListener);

    void hideNotificationForType(Type type, SHNResultListener shnResultListener);

    void getMaxImageSizeForType(Type type, ResultListener<ImageSize> resultListener);

    void getNotificationCapabilities(Type type, ResultListener<TransferGetCapabilitiesWithResult> resultListener);

    void setAlarm(TransferGetCapabilitiesWithResult transferGetCapabilitiesWithResult, byte hours_u, byte minutes_u,
                  boolean repeatNotification, short lifeTimeSeconds_u, ResultListener<GetNotificationResult> shnResultListener);

    void removeAllNotification(ResultListener<GetRemoveNotification> shnResultListener);

    void removedAtIndex(NotificationID notificationID, NotificationType notificationType, ResultListener<GetNotificationResult> shnResultListener);

}
