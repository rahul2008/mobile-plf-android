/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.HashMap;
import java.util.List;
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


    class AlarmConfig{
        private NotificationType notificationType;
        private  int hours_u;
        private  int minutes_u;
        boolean repeatNotification;
        short lifeTimeSeconds_u;

        public Vibration getVibratation() {
            return vibratation;
        }

        private Vibration  vibratation;

        public NotificationID getNotificationID() {
            return notificationID;
        }

        private NotificationID notificationID;

        public int getHours_u() {
            return hours_u;
        }

        public NotificationType getNotificationType() {
            return notificationType;
        }

        public int getMinutes_u() {
            return minutes_u;
        }

        public boolean isRepeatNotification() {
            return repeatNotification;
        }

        public short getLifeTimeSeconds_u() {
            return lifeTimeSeconds_u;
        }


        public AlarmConfig( NotificationID notificationID,NotificationType notificationType,
                           int hours_u, int minutes_u,
                           boolean repeatNotification,
                           short lifeTimeSeconds_u,Vibration vibration) {
            this.notificationID=notificationID;
            this.notificationType = notificationType;
            this.hours_u = hours_u;
            this.minutes_u = minutes_u;
            this.repeatNotification = repeatNotification;
            this.lifeTimeSeconds_u = lifeTimeSeconds_u;
            this.vibratation=vibration;
        }
    }
    class TransferGetCapabilitiesWithResult {
        private NotificationID notificationID;
        private NotificationType notificationType;

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

    void getNotificationCapabilities(ResultListener<List<TransferGetCapabilitiesWithResult>> resultListener);

    void getNotifications(NotificationID notificationID,ResultListener<AlarmConfig> resultListener);

    void setAlarmFinal(AlarmConfig alarmConfig,final SHNResultListener resultListener);

    void removeAllNotification(SHNResultListener shnResultListener);

    void removedAtIndex(NotificationID notificationID, NotificationType notificationType, ResultListener<GetNotificationResult> shnResultListener);

}
