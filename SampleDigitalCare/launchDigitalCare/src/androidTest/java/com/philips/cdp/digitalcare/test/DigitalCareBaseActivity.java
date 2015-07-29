package com.philips.cdp.digitalcare.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.philips.cdp.digitalcare.activity.DigitalCareBaseActivity;
import com.philips.cdp.sampledigitalcareapp.LaunchDigitalCare;



class DigitalCareBaseActivityTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private DigitalCareBaseActivity mObject = null;

	public DigitalCareBaseActivityTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mObject = mock(DigitalCareBaseActivity.class);
	}

	@SmallTest
	public void testDigicareBaseActivityisMOcked() {
		assertNotNull(mObject);
	}

	@SmallTest
	public void testIsDigitalCareBaseActivityMocked() {
		assertNotNull(mObject);
	}

	@SmallTest
	public void testTheFragmentMethod()
	{
		
	}
}
