package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;

import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.errors.NotificationMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(CustomRobolectricRunner.class)
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

    @Test
    public void test_showNotification() {
        urNotification.showNotification(notificationMessage);
    }

    @Test
    public void test_hideNotification() {
        urNotification.showNotification(notificationMessage);
        urNotification.hideNotification();

    }
}