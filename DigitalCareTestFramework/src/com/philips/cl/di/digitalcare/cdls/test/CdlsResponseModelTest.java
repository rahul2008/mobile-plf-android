package com.philips.cl.di.digitalcare.cdls.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.contactus.CdlsResponseModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsResponseModelTest extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {

public CdlsResponseModelTest() {
super(LaunchDigitalCare.class);
}

private CdlsResponseModel mCdlsResponseModelObject = null;

@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
	.getTargetContext().getCacheDir().getPath());
mCdlsResponseModelObject = mock(CdlsResponseModel.class);
}

@SmallTest
public void testIsAnalyticsTrackerIsMocked() {
boolean validate = false;
String received = mCdlsResponseModelObject.getClass().getSimpleName();
if (received.equalsIgnoreCase("CdlsResponseModel_Proxy"))
validate = true;
assertTrue(validate);
}

}
