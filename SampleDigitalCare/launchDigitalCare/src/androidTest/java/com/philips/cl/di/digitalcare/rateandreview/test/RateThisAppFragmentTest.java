package com.philips.cl.di.digitalcare.rateandreview.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class RateThisAppFragmentTest extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {

public RateThisAppFragmentTest() {
super(LaunchDigitalCare.class);
}

private RateThisAppFragment mRateThisAppScreen = null;


@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
		.getTargetContext().getCacheDir().getPath());
mRateThisAppScreen = mock(RateThisAppFragment.class);
}

@SmallTest
public void testIsRateThisAppScreenIsMocked() {
boolean validate = false;
String received = mRateThisAppScreen.getClass().getSimpleName();
if (received.equalsIgnoreCase("RateThisAppFragment_Proxy"))
	validate = true;
assertTrue(validate);
}

}
