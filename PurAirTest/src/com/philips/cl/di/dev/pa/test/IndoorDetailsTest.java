package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;

public class IndoorDetailsTest extends ActivityInstrumentationTestCase2<IndoorDetailsActivity> {
	
	private IndoorDetailsActivity activity;
	private Instrumentation instrumentation;

	public IndoorDetailsTest() {
		super(IndoorDetailsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		activity = getActivity();
		instrumentation = getInstrumentation();

		super.setUp();
	}
	
	@UiThreadTest
	public void testEnableDiscoveryInOnResume() {
		DiscoveryManager discManager = mock(DiscoveryManager.class);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
		
		instrumentation.callActivityOnResume(activity);
		
		verify(discManager).start(any(DiscoveryEventListener.class));
		verify(discManager, never()).stop();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
	}
	
	@UiThreadTest
	public void testDisableDiscoveryInOnPause() {
		DiscoveryManager discManager = mock(DiscoveryManager.class);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
		
		instrumentation.callActivityOnPause(activity);
		
		verify(discManager, never()).start(any(DiscoveryEventListener.class));
		verify(discManager).stop();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
	}
	
//	public void testClickEvents() {
//		FontTextView lastDayBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
//		FontTextView lastWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
//		FontTextView lastFourWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
//		
//		assertEquals(true, lastDayBtn.isClickable());
//		assertEquals(true, lastWeekBtn.isClickable());
//		assertEquals(true, lastFourWeekBtn.isClickable());
//	}
//	
//	public void testParseReading_1() {
//		activity.addAqiReading();
//		float testLast7daysRDCPVal[] = activity.last7daysRDCPVal;
//		assertEquals(7, testLast7daysRDCPVal.length);
//			
//	}
//	
//	public void testParseReading_2() {
//		activity.addAqiReading();
//		float testLastDayRDCPVal[] = activity.lastDayRDCPVal;
//		assertEquals(24, testLastDayRDCPVal.length);
//	}
//	
//	public void testParseReading_3() {
//		activity.addAqiReading();
//		float testLast4weeksRDCPVal[] = activity.last4weeksRDCPVal;
//		assertEquals(28, testLast4weeksRDCPVal.length);
//	}

}
