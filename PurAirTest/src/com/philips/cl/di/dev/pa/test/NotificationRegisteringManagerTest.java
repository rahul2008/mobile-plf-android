package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class NotificationRegisteringManagerTest extends TestCase {	

	private Field registrationField;
	private NotificationRegisteringManager manager;
	
	@Override
	protected void setUp() throws Exception {
		manager = new NotificationRegisteringManager();
		try {
			registrationField= NotificationRegisteringManager.class.getDeclaredField("regid");
			registrationField.setAccessible(true);
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.NOTIFICATION, "Test: " + e.getMessage());
			e.printStackTrace();
		}
		super.setUp();
	}
	

	public void testNotificationRegisteringManager(){
		String regStr = "";
		try {
			regStr = (String) registrationField.get(manager);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		assertNotNull(regStr);
	}

//	public void testNotificationRegisteringManager_2(){
//		String regStr = "";
//		try {
//			Class[] cArg = new Class[2];
//			cArg[0] = Context.class;
//			cArg[1] = String.class;
//			Method mPreferences = NotificationRegisteringManager.class.getDeclaredMethod("storeRegistrationId", cArg);
//			mPreferences.setAccessible(true);
//			
//			mPreferences.invoke(manager, PurAirApplication.getAppContext(), null);     
//			regStr = (String) registrationField.get(manager);
//			
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}
//		assertNull(regStr);
//	}

}
