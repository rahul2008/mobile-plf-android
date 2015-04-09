package com.philips.cl.di.digitalcare.bean.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.contactus.CdlsPhoneModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsPhoneTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

//	 private CdlsPhoneModel mObject = null;
//	private static final String TIME_ONE = "10:30", TME_TWO = "ksjkfjk",
//			TIME_THREE = "@%&@.sudh", TIME_SAT_ONE = "10:45",
//			TIME_SAT_TWO = "@0003ksksks", TIME_SAT_THREE = "5: 90";
//
//	private static final String content1 = "Chat Param",
//			content2 = "Prefer Chat Param $5040959",
//			content3 = "790023.&(((@@";

	public CdlsPhoneTest() {
		super(LaunchDigitalCare.class);

	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());

		/*mObject =*/ mock(CdlsPhoneModel.class);
	}

	@SmallTest
	public void test() {

	}

}
