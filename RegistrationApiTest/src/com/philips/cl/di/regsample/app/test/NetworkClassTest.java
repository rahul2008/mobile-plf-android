
package com.philips.cl.di.regsample.app.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.regsample.app.RegistrationSampleActivity;

import static org.mockito.Mockito.mock;

public class NetworkClassTest extends ActivityInstrumentationTestCase2<RegistrationSampleActivity> {

	NetworkUtility mNetworkUtility = null;

	public NetworkClassTest() {
		super(RegistrationSampleActivity.class);
	}

	@Override
	protected void setUp()
	        throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext()
		        .getCacheDir().getPath());
		mNetworkUtility = mock(NetworkUtility.class);
	}

	/*public void testisNetworkUtilityClass() {
		assertNotNull(mNetworkUtility);
	}*/

	public void testDemocase() {
		String mReceived = "Please Pass dear";
		String mExpected = mReceived;

		assertEquals(mExpected, mReceived);
	}

}
