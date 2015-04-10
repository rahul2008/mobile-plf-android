package com.philips.cl.di.digitalcare.cdls.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.contactus.CdlsRequestTask;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsRequestTaskTest extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {

public CdlsRequestTaskTest() {
super(LaunchDigitalCare.class);
}

private CdlsRequestTask mAnalyticsTrackerClass = null;

@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
	.getTargetContext().getCacheDir().getPath());
mAnalyticsTrackerClass = mock(CdlsRequestTask.class);
}

@SmallTest
public void testIsCDLSReequestTaskIsMocked() {
boolean validate = false;
String received = mAnalyticsTrackerClass.getClass().getSimpleName();
if (received.equalsIgnoreCase("CdlsRequestTask_Proxy"))
validate = true;
assertTrue(validate);
}

}
