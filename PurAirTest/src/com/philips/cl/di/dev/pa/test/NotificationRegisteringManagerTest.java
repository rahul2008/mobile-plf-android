package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class NotificationRegisteringManagerTest extends TestCase {	
	
	private Field registrationId;

	public void testNotificationRegisteringManager(){
		try {
			registrationId= NotificationRegisteringManager.class.getDeclaredField("regid");
			registrationId.setAccessible(true);
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.NOTIFICATION, "Test: " + e.getMessage());
			e.printStackTrace();
		}
		assertNotNull(registrationId);
	}
	
}
