package com.philips.cl.di.dev.pa.test;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.notification.NotificationIntentService;

public class NotificationIntentServiceTest extends InstrumentationTestCase {

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
}
