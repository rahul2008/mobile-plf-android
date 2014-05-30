package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.scheduler.RepeatFragment;
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
			activity.dispatchInformations("0123456");
			
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
			activity.dispatchInformations2("Turbo");
			
			Field fanSpeedField = SchedulerActivity.class.getDeclaredField("selectedFanspeed");
			fanSpeedField.setAccessible(true);
			String fanspeed = (String) fanSpeedField.get(activity);
			assertEquals("Turbo", fanspeed);	
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
			ALog.e(ALog.SCHEDULER, "Failed getting fanSpeedField field ");
		}
	}
	
	public void testGetTime() {
		try {
			TimePicker view = null;
			int hourOfDay = 11;
			int minute = 59;
			activity.onTimeSet(view, hourOfDay, minute);
			
			Field timeField = SchedulerActivity.class.getDeclaredField("selectedTime");
			timeField.setAccessible(true);
			String time = (String) timeField.get(activity);
			assertEquals("11:59", time);	
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
			ALog.e(ALog.SCHEDULER, "Failed getting selectedTime field ");
		}
	}
	
}
