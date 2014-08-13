package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
	
//	@UiThreadTest
//	public void testEnableDiscoveryInOnResume() {
//		if (activity == null) return;
//		DiscoveryManager discManager = mock(DiscoveryManager.class);
//		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
//		
//		instrumentation.callActivityOnResume(activity);
//		
//		verify(discManager).start(any(DiscoveryEventListener.class));
////		verify(discManager, never()).stop();
//		
//		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
//	}
//	
//	@UiThreadTest
//	public void testDisableDiscoveryInOnPause() {
//		if (activity == null) return;
//		DiscoveryManager discManager = mock(DiscoveryManager.class);
//		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
//		
//		instrumentation.callActivityOnPause(activity);
//		
////		verify(discManager).stop();
//		
//		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
//	}
	
}
