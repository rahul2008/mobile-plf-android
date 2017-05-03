/*
 * Copyright (c) Koninklijke Philips N.V.,2017.
 * All rights reserved.
 */
package com.philips.pins.shinelib.capabilities;

public class AlarmConfig {
    private SHNCapabilityNotifications.NotificationType notificationType;
    private int hours;
    private int minutes;
    boolean repeatNotification;
    short lifeTimeSeconds;
    private SHNCapabilityNotifications.Vibration vibratation;

    public SHNCapabilityNotifications.Vibration getVibratation() {
        return vibratation;
    }

    public SHNCapabilityNotifications.NotificationID getNotificationID() {
        return notificationID;
    }

    private SHNCapabilityNotifications.NotificationID notificationID;

    public int getHours() {
        return hours;
    }

    public SHNCapabilityNotifications.NotificationType getNotificationType() {
        return notificationType;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean isRepeatNotification() {
        return repeatNotification;
    }

    public short getLifeTimeSeconds() {
        return lifeTimeSeconds;
    }

    public AlarmConfig(SHNCapabilityNotifications.NotificationID notificationID, SHNCapabilityNotifications.NotificationType notificationType, int hours, int minutes, boolean repeatNotification, short lifeTimeSeconds, SHNCapabilityNotifications.Vibration vibration) {
        this.notificationID = notificationID;
        this.notificationType = notificationType;
        this.hours = hours;
        this.minutes = minutes;
        this.repeatNotification = repeatNotification;
        this.lifeTimeSeconds = lifeTimeSeconds;
        this.vibratation = vibration;
    }
}
