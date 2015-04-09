package com.philips.cl.di.digitalcare.bean.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.contactus.CdlsEmailModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsEmailTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private CdlsEmailModel mObject1, mObject2, mObject3 = null;

	private String Label1 = "jjsuioso@kakss.com", label2 = "@askkjnss85955",
			label3 = "skkksidndn.com";
	private String Path1 = "c://path",
			path2 = "http://www.philips.screen.value", path3 = "ftp://askksks";

	public CdlsEmailTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mObject1 = mock(CdlsEmailModel.class);
		mObject2 = mock(CdlsEmailModel.class);
		mObject3 = mock(CdlsEmailModel.class);

		when(mObject1.getLabel()).thenReturn(Label1);
		when(mObject2.getLabel()).thenReturn(label2);
		when(mObject3.getLabel()).thenReturn(label3);

		when(mObject1.getContentPath()).thenReturn(Path1);
		when(mObject2.getContentPath()).thenReturn(path2);
		when(mObject3.getContentPath()).thenReturn(path3);
	}

	@SmallTest
	public void testLabelCycle1() {

		assertEquals(Label1, mObject1.getLabel());
	}

	@SmallTest
	public void testLabelCycle2() {
		assertEquals(label2, mObject2.getLabel());
	}

	@SmallTest
	public void testLabelCycle3() {
		assertEquals(label3, mObject3.getLabel());
	}

	@SmallTest
	public void testContentPathCycle1() {

		assertEquals(Path1, mObject1.getContentPath());
	}

	@SmallTest
	public void testContentPathCycle2() {
		assertEquals(path2, mObject2.getContentPath());
	}

	@SmallTest
	public void testContentPathCycle3() {
		assertEquals(path3, mObject3.getContentPath());
	}

}
