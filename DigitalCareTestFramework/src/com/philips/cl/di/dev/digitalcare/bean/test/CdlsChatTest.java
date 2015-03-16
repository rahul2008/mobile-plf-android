package com.philips.cl.di.dev.digitalcare.bean.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.dev.digitalcare.Utility.test.Log;
import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.cl.di.digitalcare.contactus.CdlsChat;

/**
 * 
 * @author naveen@philips.com
 * @description Testing BeanChat Logic Functions.
 * @Since Mar 11, 2015
 */
public class CdlsChatTest extends
		ActivityInstrumentationTestCase2<DigitalCareActivity> {

	public CdlsChatTest() {
		super(DigitalCareActivity.class);

	}

	private CdlsChat mObject = null;
	private static final String TAG = CdlsChatTest.class.getSimpleName();

	private static final String val1 = "10:30", val2 = "ksjkfjk",
			val3 = "@%&@.sudh";
	private static final String content1 = "Chat Param",
			content2 = "Prefer Chat Param $5040959",
			content3 = "790023.&(((@@";

	@Before
	protected void onLoad() throws Exception {
		Log.d(TAG, "OnLoad()");

	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "SetUp");
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mObject = mock(CdlsChat.class);

		when(mObject.getOpeningHoursWeekdays()).thenReturn(val1);
		when(mObject.getOpeningHoursSaturday()).thenReturn(val1);
		when(mObject.getContent()).thenReturn(content1);

	}

	@SmallTest
	public void testWeekdayHoursLogic() {
		Log.d(TAG, "TestWeedays");

		String answer = mObject.getOpeningHoursWeekdays();
		Log.d(TAG, answer);

		assertEquals(val1, answer);
	}

	@SmallTest
	public void testWeekEndHoursLogic() {
		Log.d(TAG, "Saturday Support Time");

		String answer = mObject.getOpeningHoursSaturday();
		Log.d(TAG, answer);

		assertEquals(val1, answer);
	}

	@SmallTest
	public void testWeekHoursContentLogic() {
		Log.d(TAG, "Content Description");

		String answer = mObject.getContent();
		Log.d(TAG, answer);
		assertEquals(content1, answer);
	}

	/**
	 * Cycle 2
	 */

	@SmallTest
	public void testWeekdayHoursLogicCycleTwo() {
		Log.d(TAG, "TestWeedays 2nd Cycle");
		when(mObject.getOpeningHoursWeekdays()).thenReturn(val2);
		String answer = mObject.getOpeningHoursWeekdays();
		Log.d(TAG, answer);
		assertEquals(val2, answer);
	}

	@SmallTest
	public void testWeekEndHoursLogicCycleTwo() {
		Log.d(TAG, "Saturday Support Time 2nd Cycle");
		when(mObject.getOpeningHoursSaturday()).thenReturn(val2);
		String answer = mObject.getOpeningHoursSaturday();
		Log.d(TAG, answer);
		assertEquals(val2, answer);
	}

	@SmallTest
	public void testWeekHoursContentLogicCycleTwo() {
		Log.d(TAG, "Content Description 2nd Time Cycle");
		when(mObject.getContent()).thenReturn(content2);
		String answer = mObject.getContent();
		Log.d(TAG, answer);
		assertEquals(content2, answer);
	}

	/**
	 * Cycle 3
	 */
	/**
	 * 
	 */

	@SmallTest
	public void testWeekdayHoursLogicCycleThree() {
		Log.d(TAG, "TestWeedays 3rd Cycle");
		when(mObject.getOpeningHoursWeekdays()).thenReturn(val3);
		String answer = mObject.getOpeningHoursWeekdays();
		Log.d(TAG, answer);
		assertEquals(val3, answer);
	}

	@SmallTest
	public void testWeekEndHoursLogicCycleThree() {
		Log.d(TAG, "Saturday Support Time 3rd Cycle");
		when(mObject.getOpeningHoursSaturday()).thenReturn(val3);
		String answer = mObject.getOpeningHoursSaturday();
		Log.d(TAG, answer);
		assertEquals(val3, answer);
	}

	@SmallTest
	public void testWeekHoursContentLogicCycleThree() {
		Log.d(TAG, "Content Description 2nd Time Cycle");
		when(mObject.getContent()).thenReturn(content3);
		String answer = mObject.getContent();
		Log.d(TAG, answer);
		assertEquals(content3, answer);
	}

}
