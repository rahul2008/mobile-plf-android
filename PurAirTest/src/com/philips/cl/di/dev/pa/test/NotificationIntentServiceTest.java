package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verifyZeroInteractions;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.notification.NotificationIntentService;

public class NotificationIntentServiceTest extends InstrumentationTestCase {
	
	private NotificationManager mNotificationManager; 
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		mNotificationManager = mock(NotificationManager.class);
		super.setUp();
	}

	public void testGetMessageFromNullBundle() {
		Bundle notificationExtras = null;
		
		NotificationIntentService service = new NotificationIntentService();
		String message = service.getMessageFromNotification(notificationExtras);
		
		assertNull(message);
	}
	
	public void testGetMessageFromEmptyBundle() {
		Bundle notificationExtras = new Bundle();
		
		NotificationIntentService service = new NotificationIntentService();
		String message = service.getMessageFromNotification(notificationExtras);
		
		assertNull(message);
	}
	
	public void testGetMessageFromBundleWithNullAlert() {
		Bundle notificationExtras = new Bundle();
		notificationExtras.putString("alert", null);
		
		NotificationIntentService service = new NotificationIntentService();
		String message = service.getMessageFromNotification(notificationExtras);
		
		assertNull(message);
	}
	
	public void testGetMessageFromBundleWithEmptyAlert() {
		Bundle notificationExtras = new Bundle();
		notificationExtras.putString("alert", "");
		
		NotificationIntentService service = new NotificationIntentService();
		String message = service.getMessageFromNotification(notificationExtras);
		
		assertNull(message);
	}
	
	public void testGetMessageFromBundleWithAlert() {
		String alert = "Your Philips Smart Air Purifier has detected an air level above your set level. Would you like to change your settings?";
		Bundle notificationExtras = new Bundle();
		notificationExtras.putString("alert", alert);
		
		NotificationIntentService service = new NotificationIntentService();
		String message = service.getMessageFromNotification(notificationExtras);
		
		assertEquals(alert, message);
	}
	
	public void testReceiveNotificationNullMessage() {
		NotificationIntentService service = new NotificationIntentService();
		service.showNotification(mNotificationManager, getInstrumentation().getTargetContext(), null);
		
		verify(mNotificationManager, never()).notify(anyInt(), any(Notification.class));
	}

	public void testReceiveNotificationEmptyMessage() {
		NotificationIntentService service = new NotificationIntentService();
		service.showNotification(mNotificationManager, getInstrumentation().getTargetContext(), "");
		
		verify(mNotificationManager, never()).notify(anyInt(), any(Notification.class));
	}
	
	public void testReceiveNotificationProperMessage() {
		NotificationIntentService service = new NotificationIntentService();
		service.showNotification(mNotificationManager, getInstrumentation().getTargetContext(), "test");
		
		verify(mNotificationManager).notify(anyInt(), any(Notification.class));
	}
}
