package com.philips.cl.di.digitalcare.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.cl.di.digitalcare.SupportHomeFragment;

public class SupportHomeFragmentTest extends
ActivityInstrumentationTestCase2<DigitalCareActivity> {

public SupportHomeFragmentTest() {
super(DigitalCareActivity.class);
}

private SupportHomeFragment mSupportHomeFragment = null;


@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
		.getTargetContext().getCacheDir().getPath());
mSupportHomeFragment = mock(SupportHomeFragment.class);
}

@SmallTest
public void testIsSupportHomeFragmentIsMocked() {
boolean validate = false;
String received = mSupportHomeFragment.getClass().getSimpleName();
if (received.equalsIgnoreCase("SupportHomeFragment_Proxy"))
	validate = true;
assertTrue(validate);
}

}
