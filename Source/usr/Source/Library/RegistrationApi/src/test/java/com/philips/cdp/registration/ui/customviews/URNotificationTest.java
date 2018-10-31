/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;

import com.philips.cdp.registration.errors.NotificationMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class URNotificationTest {
    @Mock
    Activity activity;
    @Mock
    URNotification.URNotificationInterface urNotificationInterface;

    URNotification urNotification;

    @Mock
    NotificationMessage notificationMessage;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        urNotification = new URNotification(activity, urNotificationInterface);
//        Mockito.when(notificationMessage.getNotificationType()).thenReturn(NotificationType.NOTIFICATION_BAR);
        Mockito.when(notificationMessage.getErrorCode()).thenReturn(210);

    }

//    @Test
//    public void test_showNotification() {
//        Mockito.when(notificationMessage.getErrorCode()).thenReturn(310);
//        urNotification.showNotification(notificationMessage, false);
//    }

    @Test
    public void test_hideNotification() {
        urNotification.showNotification(notificationMessage);
        urNotification.hideNotification();

    }
}