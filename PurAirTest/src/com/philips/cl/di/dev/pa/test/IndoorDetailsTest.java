package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.view.FontTextView;

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
		FontTextView lastDayBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
		FontTextView lastWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
		FontTextView lastFourWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		
		assertEquals(true, lastDayBtn.isClickable());
		assertEquals(true, lastWeekBtn.isClickable());
		assertEquals(true, lastFourWeekBtn.isClickable());
	}
	
	public void testParseReading_1() {
		activity.addAqiReading();
		float testLast7daysRDCPVal[] = activity.last7daysRDCPVal;
		assertEquals(7, testLast7daysRDCPVal.length);
			
	}
	
	public void testParseReading_2() {
		activity.addAqiReading();
		float testLastDayRDCPVal[] = activity.lastDayRDCPVal;
		assertEquals(24, testLastDayRDCPVal.length);
	}
	
	public void testParseReading_3() {
		activity.addAqiReading();
		float testLast4weeksRDCPVal[] = activity.last4weeksRDCPVal;
		assertEquals(28, testLast4weeksRDCPVal.length);
	}

}
