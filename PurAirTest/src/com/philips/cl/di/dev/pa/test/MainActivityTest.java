package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;

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

	@UiThreadTest
	public void testEnableDiscoveryInOnResume() {
		if(UserRegistrationController.getInstance().isUserLoggedIn()) {
			DiscoveryManager discManager = mock(DiscoveryManager.class);
			DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
			
			instrumentation.callActivityOnResume(activity);
			
			verify(discManager).start(activity);
			verify(discManager, never()).stop();
			
			DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		} else {
			assertFalse(false);
		}
		
		
	}
	
	@UiThreadTest
	public void testDisableDiscoveryInOnPause() {
		
		if(UserRegistrationController.getInstance().isUserLoggedIn()) {
			DiscoveryManager discManager = mock(DiscoveryManager.class);
			DiscoveryManager.setDummyDiscoveryManagerForTesting(discManager);
			
			instrumentation.callActivityOnPause(activity);
			
			verify(discManager, never()).start(activity);
			verify(discManager).stop();
			
			DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		} else {
			assertFalse(false);
		}
		
	}
	
	@UiThreadTest
	public void testRegisterSubscriptionInOnResume() {
		if(UserRegistrationController.getInstance().isUserLoggedIn()) {
			AirPurifierManager purManager = mock(AirPurifierManager.class);
			AirPurifierManager.setDummyPurifierManagerForTesting(purManager);
			
			instrumentation.callActivityOnResume(activity);
			
			verify(purManager).addAirPurifierEventListener(activity);
//			verify(purManager, never()).removeAirPurifierEventListener(activity);
			
			AirPurifierManager.setDummyPurifierManagerForTesting(null);
		} else {
			assertFalse(false);
		}
		
	}
	
	@UiThreadTest
	public void testUnRegisterSubscriptionInOnPause() {
		if(UserRegistrationController.getInstance().isUserLoggedIn()) {
			AirPurifierManager purManager = mock(AirPurifierManager.class);
			AirPurifierManager.setDummyPurifierManagerForTesting(purManager);
			
			instrumentation.callActivityOnPause(activity);
			
			verify(purManager, never()).addAirPurifierEventListener(activity);
			verify(purManager).removeAirPurifierEventListener(activity);
			
			AirPurifierManager.setDummyPurifierManagerForTesting(null);
		
		} else {
			assertFalse(false);
		}
	}
}
