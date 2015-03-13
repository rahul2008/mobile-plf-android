package com.philips.cl.di.digitalcare.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.DigitalCareBaseActivity;

/**
 * 
 * @author naveen@philips.com
 * @description
 * @Since Mar 1, 2015
 */
public class DigitalCareBaseActivityTest extends
		ActivityInstrumentationTestCase2<DigitalCareBaseActivity> {

	

	public DigitalCareBaseActivityTest() {
		super(DigitalCareBaseActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCompare() {
		String s = "Naveen", y = "Naveen";

		assertEquals("Both are different", s, y);
	}

}
