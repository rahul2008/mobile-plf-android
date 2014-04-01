package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;

public class IndoorDetailsTest extends ActivityInstrumentationTestCase2<IndoorDetailsActivity> {
	
	IndoorDetailsActivity activity;

	public IndoorDetailsTest() {
		super(IndoorDetailsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}
	
	public void testClickEvents() {
		CustomTextView lastDayBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
		CustomTextView lastWeekBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
		CustomTextView lastFourWeekBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		
		assertEquals(true, lastDayBtn.isClickable());
		assertEquals(true, lastWeekBtn.isClickable());
		assertEquals(true, lastFourWeekBtn.isClickable());
	}
	
	public void testParseReading_1() {
		activity.parseReading();
		float testLast7daysRDCPVal[] = activity.last7daysRDCPVal;
		assertEquals(7, testLast7daysRDCPVal.length);
			
	}
	
	public void testParseReading_2() {
		activity.parseReading();
		float testLastDayRDCPVal[] = activity.lastDayRDCPVal;
		assertEquals(24, testLastDayRDCPVal.length);
	}
	
	public void testParseReading_3() {
		activity.parseReading();
		float testLast4weeksRDCPVal[] = activity.last4weeksRDCPVal;
		assertEquals(28, testLast4weeksRDCPVal.length);
	}
	
	
	@UiThreadTest
	public void testAqiAnalysisClick() {
		Button button = (Button) activity.findViewById(R.id.idAnExplain);
		assertEquals("Air quality explained", button.getText().toString());
	}
}
