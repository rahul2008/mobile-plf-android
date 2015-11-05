package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.scheduler.SchedulerActivity;
import com.philips.cl.di.dev.pa.util.ALog;

public class SchedulerTest extends ActivityInstrumentationTestCase2<SchedulerActivity> {
	private SchedulerActivity activity;
	
	public SchedulerTest() {
		super(SchedulerActivity.class);
	}	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		activity = getActivity();
		assertNotNull(activity);
	}
	
	public void testGetDays() {
		try {
			activity.setDays("0123456");
			
			Field daysField = SchedulerActivity.class.getDeclaredField("selectedDays");
			daysField.setAccessible(true);
			String days = (String)daysField.get(activity);
			assertEquals("0123456", days);	
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
			ALog.e(ALog.SCHEDULER, "Failed getting selectedDays field ");
		}
	}
	
	public void testGetFanSpeed() {
		try {
			activity.setFanSpeed("t");
			
			Field fanSpeedField = SchedulerActivity.class.getDeclaredField("selectedFanspeed");
			fanSpeedField.setAccessible(true);
			String fanspeed = (String) fanSpeedField.get(activity);
			assertEquals("t", fanspeed);	
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
			ALog.e(ALog.SCHEDULER, "Failed getting fanSpeedField field ");
		}
	}
	
}
