package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity activity;
	private Instrumentation instrumentation;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		setActivityInitialTouchMode(false);
		activity = getActivity();
		instrumentation = this.getInstrumentation();
		
		super.setUp();
	}

	public void testActivityTitle() {
		assertEquals("Smart Air", activity.getTitle());
	}
	
	@UiThreadTest
	public void testEnableDiscoveryInOnResume() {
		DiscoveryManager discManager = mock(DiscoveryManager.class);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
		
		instrumentation.callActivityOnResume(activity);
		
		verify(discManager).start(activity);
		verify(discManager, never()).stop(any(DiscoveryEventListener.class));
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
	}
	
	@UiThreadTest
	public void testDisableDiscoveryInOnPause() {
		DiscoveryManager discManager = mock(DiscoveryManager.class);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
		
		instrumentation.callActivityOnPause(activity);
		
		verify(discManager, never()).start(activity);
		verify(discManager).stop(activity);
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
	}
	
	@UiThreadTest
	public void testRegisterSubscriptionInOnResume() {
		PurifierManager purManager = mock(PurifierManager.class);
		PurifierManager.setDummyPurifierManagerForTesting(purManager);
		
		instrumentation.callActivityOnResume(activity);
		
		verify(purManager).addAirPurifierEventListener(activity);
		verify(purManager, never()).removeAirPurifierEventListener(activity);
		
		PurifierManager.setDummyPurifierManagerForTesting(null);
	}
	
	@UiThreadTest
	public void testUnRegisterSubscriptionInOnPause() {
		PurifierManager purManager = mock(PurifierManager.class);
		PurifierManager.setDummyPurifierManagerForTesting(purManager);
		
		instrumentation.callActivityOnPause(activity);
		
		verify(purManager, never()).addAirPurifierEventListener(activity);
		verify(purManager).removeAirPurifierEventListener(activity);
		
		PurifierManager.setDummyPurifierManagerForTesting(null);
	}
}
