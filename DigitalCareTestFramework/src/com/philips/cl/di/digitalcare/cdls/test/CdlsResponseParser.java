package com.philips.cl.di.digitalcare.cdls.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsResponseParser extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {

public CdlsResponseParser() {
super(LaunchDigitalCare.class);
}

private CdlsResponseParser mCDLSResponseparser = null;

@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
	.getTargetContext().getCacheDir().getPath());
mCDLSResponseparser = mock(CdlsResponseParser.class);
}

@SmallTest
public void testIsCDLSResponseIsMocked() {
boolean validate = false;
String received = mCDLSResponseparser.getClass().getSimpleName();
if (received.equalsIgnoreCase("CdlsResponseParser_Proxy"))
validate = true;
assertTrue(validate);
}

}
