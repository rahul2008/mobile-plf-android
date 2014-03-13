package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.IndoorAQIAnalysisActivity;
import com.philips.cl.di.dev.pa.screens.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.screens.OutdoorDetailsActivity;

public class IndoorDetailsTest extends ActivityInstrumentationTestCase2<IndoorDetailsActivity> {
	
	IndoorDetailsActivity activity;

	public IndoorDetailsTest() {
		super(IndoorDetailsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(), IndoorDetailsActivity.class);
//	    startActivity(intent, null, null);
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
	
	public void testParseReading() {
		try {
			Method getXCoordinatesMethod = IndoorDetailsActivity.class.getDeclaredMethod("parseReading", (Class<?>[])null);
			getXCoordinatesMethod.setAccessible(true);
			getXCoordinatesMethod.invoke(activity, (Object[])null);
			
			Field keysField1 = OutdoorDetailsActivity.class.getDeclaredField("lastDayRDCPValues");
			keysField1.setAccessible(true);
			float lastDayRDCPValues[] = (float[])keysField1.get(activity);
			assertEquals(24, lastDayRDCPValues.length);
			
			Field keysField2 = OutdoorDetailsActivity.class.getDeclaredField("last4weeksRDCPVal");
			keysField1.setAccessible(true);
			float last4weeksRDCPVal[] = (float[])keysField2.get(activity);
			assertEquals(0, last4weeksRDCPVal.length);
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@UiThreadTest
	public void testAqiAnalysisClick() {
		Button button = (Button) activity.findViewById(R.id.idAnExplain);
//		button.performClick();
		assertEquals("Air quality explained", button.getText().toString());
//		Intent triggeredIntent = new Intent(activity, IndoorAQIAnalysisActivity.class);
//	    assertNotNull("Intent was null", triggeredIntent);
//	    String dataIndoor = triggeredIntent.getExtras().getString("indoortitle");
//	    String dataOutdoor = triggeredIntent.getExtras().getString("outdoortitle");
//
//	    assertNotNull(dataIndoor);
//	    assertNotNull(dataOutdoor);
	}
}
