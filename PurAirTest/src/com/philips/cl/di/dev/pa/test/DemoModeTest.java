package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.demo.DemoModeConstant;
import com.philips.cl.di.dev.pa.demo.DemoModeStartFragement;
import com.philips.cl.di.dev.pa.registration.CreateAccountFragment;
import com.philips.cl.di.dev.pa.util.ALog;

public class DemoModeTest extends ActivityInstrumentationTestCase2<DemoModeActivity> {
	
	private DemoModeActivity activity;
	
	public DemoModeTest() {
		super(DemoModeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		setActivityInitialTouchMode(false);
		activity = getActivity();
		
		super.setUp();
	}
	
	public void testDemoModeStartFragment() {
		List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
		assertEquals(DemoModeStartFragement.class.getSimpleName(), fragments.get(0).getClass().getSimpleName());
	}
	
	public void testDemoModeStepOneFragment() {
		activity.gotoStepOneScreen();
		int step = DemoModeConstant.DEMO_MODE_STEP_INTRO;
		try {
			Field stepField = DemoModeActivity.class.getDeclaredField("demoStep");
			stepField.setAccessible(true);
			step = (Integer) stepField.getInt((DemoModeActivity)getActivity());
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ConnectivityManager connManager = 
				(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null && networkInfo.isConnected()) {
			assertEquals(DemoModeConstant.DEMO_MODE_STEP_ONE, step);	
		} else {
			assertEquals(DemoModeConstant.DEMO_MODE_STEP_INTRO, step);	
		}
		
			
	}
	
	public void testDemoModeSupportFragment() {
		activity.showSupportScreen();
		int step = DemoModeConstant.DEMO_MODE_STEP_INTRO;
		try {
			Field stepField = DemoModeActivity.class.getDeclaredField("demoStep");
			stepField.setAccessible(true);
			step = (Integer) stepField.getInt((DemoModeActivity)getActivity());
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		assertEquals(DemoModeConstant.DEMO_MODE_STEP_SUPPORT, step);		
	}
	
	public void testDemoModeFinalFragment() {
		activity.showFinalScreen();
		int step = DemoModeConstant.DEMO_MODE_STEP_INTRO;
		try {
			Field stepField = DemoModeActivity.class.getDeclaredField("demoStep");
			stepField.setAccessible(true);
			step = (Integer) stepField.getInt((DemoModeActivity)getActivity());
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		assertEquals(DemoModeConstant.DEMO_MODE_STEP_FINAL, step);		
	}
	
	public void testDemoModeSupportFragment1() {
		
		Field nameField;
		try {
			nameField = DemoModeActivity.class.getDeclaredField("apModeFailCounter");
			nameField.setAccessible(true);
			nameField.set((DemoModeActivity)getActivity(), 3);
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
		} catch (IllegalAccessException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
		}
		
		activity.showStepToSetupAPMode();
		int step = DemoModeConstant.DEMO_MODE_STEP_INTRO;
		try {
			Field stepField = DemoModeActivity.class.getDeclaredField("demoStep");
			stepField.setAccessible(true);
			step = (Integer) stepField.getInt((DemoModeActivity)getActivity());
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.DEMO_MODE, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		assertEquals(DemoModeConstant.DEMO_MODE_STEP_SUPPORT, step);		
	}

}
