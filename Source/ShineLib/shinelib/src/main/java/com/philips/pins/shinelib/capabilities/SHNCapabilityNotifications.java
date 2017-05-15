/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.List;

public interface SHNCapabilityNotifications extends SHNCapability {
    enum Type {
        EMAIL,
        IMAGE
    }

    enum Vibration {
        NO_VIBRATION,
        SHORT_VIBRATION,
        LONG_VIBRATION
    }

    enum NotificationID {
        SMALL_FACE_ICON_00(0),
        SMALL_FACE_ICON_01(1),
        SMALL_FACE_ICON_02(2),
        SMALL_FACE_ICON_03(3),
        FULL_SCREEN_ICON_00(100),
        FULL_SCREEN_ICON_01(101),
        FULL_SCREEN_ICON_02(102),
        FULL_SCREEN_ICON_03(103),
        ALARM_00(150),
        INVALID_NOTIFICATION(225);

        private int value;

        NotificationID(int value) {
            this.value = value;
        }

        public static NotificationID valueOf(int notificationIDs) {
            for (NotificationID notificationID : NotificationID.values()) {
                if (notificationID.getValue() == notificationIDs) {
                    return notificationID;
                }
            }
            return INVALID_NOTIFICATION;
        }

        public int getValue() {
            return value;
        }
    }

    enum NotificationType {
        SMALL_FACE_ICON,
        FULL_SCREEN_ICON,
        ALARM_NOTIFICATION,
        INVALID_ICON
    }

    class TransferGetCapabilitiesWithResult {
        private NotificationID notificationID;
        private NotificationType notificationType;

        public TransferGetCapabilitiesWithResult(NotificationID notificationID, NotificationType notificationType) {
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

    void showNotificationForType(Type type, byte[] imageData, SHNResultListener shnResultListener);

    void hideNotificationForType(Type type, SHNResultListener shnResultListener);

    void getNotificationCapabilities(ResultListener<List<TransferGetCapabilitiesWithResult>> resultListener);

    void getNotifications(NotificationID notificationID, ResultListener<AlarmConfig> resultListener);

    void setAlarmFinal(AlarmConfig alarmConfig, final SHNResultListener resultListener);

    void removeAllNotification(SHNResultListener shnResultListener);

    void removedAtIndex(NotificationID notificationID, NotificationType notificationType, ResultListener<GetNotificationResult> shnResultListener);

}
