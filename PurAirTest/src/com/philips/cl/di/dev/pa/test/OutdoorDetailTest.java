package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.ews.EwsActivity;
import com.philips.cl.di.dev.pa.screens.OutdoorDetailsActivity;
import com.philips.cl.disecurity.DISecurity;

import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageView;

public class OutdoorDetailTest extends ActivityInstrumentationTestCase2<OutdoorDetailsActivity> {
	
	OutdoorDetailsActivity activity;

	public OutdoorDetailTest() {
		super(OutdoorDetailsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}
	
	public void testClickedEventText() {
		CustomTextView lastDayBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
		CustomTextView lastWeekBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
		CustomTextView lastFourWeekBtn = (CustomTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		ImageView mapClickImg = (ImageView) activity.findViewById(R.id.oDmapInlarge); 
		
		assertEquals(true, lastDayBtn.isClickable());
		assertEquals(true, lastWeekBtn.isClickable());
		assertEquals(true, lastFourWeekBtn.isClickable());
		assertEquals(true, mapClickImg.isClickable());
		
		assertEquals("Last day", lastDayBtn.getText().toString());
		assertEquals("Last 7 days", lastWeekBtn.getText().toString());
		assertEquals("Last 4 weeks", lastFourWeekBtn.getText().toString());
		
		
	}
	
	public void testSetAdviceIconTex() {
		try {
			Method setAdviceIconTexMethod = EwsActivity.class.getDeclaredMethod("setAdviceIconTex", new Class[]{Integer.class});
			setAdviceIconTexMethod.setAccessible(true);
			setAdviceIconTexMethod.invoke(activity, 0);

			CustomTextView avoidTxt = (CustomTextView) activity.findViewById(R.id.avoidOutdoorTxt); 
			CustomTextView openWindowTxt = (CustomTextView) activity.findViewById(R.id.openWindowTxt); 
			CustomTextView maskTxt = (CustomTextView) activity.findViewById(R.id.maskTxt);
			
			assertEquals("OK for outdoor activities", avoidTxt.getText().toString());
			assertEquals("Masks not needed", maskTxt.getText().toString());
			assertEquals("OK to open windows", openWindowTxt.getText().toString());
			
				
		} catch (Exception e) {
			
		}
	}
	
	public void testGetXCoordinates() {
		try {
			Method getXCoordinatesMethod = EwsActivity.class.getDeclaredMethod("getXCoordinates", (Class<?>[])null);
			getXCoordinatesMethod.setAccessible(true);
			getXCoordinatesMethod.invoke(activity, (Object[])null);
			
			Field keysField1 = DISecurity.class.getDeclaredField("lastDayAQIReadings");
			keysField1.setAccessible(true);
			float lastDayAQIReadings[] = (float[])keysField1.get(activity);
			assertEquals(24, lastDayAQIReadings.length);
			
			Field keysField2 = DISecurity.class.getDeclaredField("last7dayAQIReadings");
			float last7dayAQIReadings[] = (float[])keysField2.get(activity);
			keysField2.setAccessible(true);
			assertEquals(7, last7dayAQIReadings.length);
			
			Field keysField3 = DISecurity.class.getDeclaredField("last4weekAQIReadings");
			float last4weekAQIReadings[] = (float[])keysField3.get(activity);
			keysField3.setAccessible(true);
			assertEquals(28, last4weekAQIReadings.length);
				
		} catch (Exception e) {
			
		}
	}

}
