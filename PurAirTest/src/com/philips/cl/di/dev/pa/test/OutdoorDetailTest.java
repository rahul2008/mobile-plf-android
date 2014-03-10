package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;
import com.philips.cl.di.dev.pa.detail.utils.GraphPathDraw;
import com.philips.cl.di.dev.pa.ews.EwsActivity;
import com.philips.cl.di.dev.pa.screens.OutdoorDetailsActivity;
import com.philips.cl.disecurity.DISecurity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class OutdoorDetailTest extends ActivityInstrumentationTestCase2<OutdoorDetailsActivity> {
	
	private OutdoorDetailsActivity activity;
	private Coordinates coordinates;
	private GraphPathDraw graphPathDraw;

	public OutdoorDetailTest() {
		super(OutdoorDetailsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
		coordinates =  Coordinates.getInstance(activity);
		graphPathDraw = new GraphPathDraw(coordinates, true);
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
	/**
	 * 
	 */
	public void testGraphPathDraw() {
		
		try {
			/**
			 * Outdoor
			 */
			GraphPathDraw graphPathDraw = new GraphPathDraw(coordinates, true);
			
			Field yCoordinateField = GraphPathDraw.class.getDeclaredField("yIndoorCoordinates");
			yCoordinateField.setAccessible(true);
			float yIndoorCoordinates[]  = (float[]) yCoordinateField.get(graphPathDraw);
			
			assertNull(yIndoorCoordinates);
			
			Field yCoordinateField1 = GraphPathDraw.class.getDeclaredField("yOutdoorCoordinates");
			yCoordinateField1.setAccessible(true);
			float yOutdoorCoordinates[]  = (float[]) yCoordinateField1.get(graphPathDraw);
			
			assertEquals(8, yOutdoorCoordinates.length);
			/**
			 * Indoor
			 */
			GraphPathDraw graphPathDraw1 = new GraphPathDraw(coordinates, false);
			
			Field yCoordinateField2 = GraphPathDraw.class.getDeclaredField("yIndoorCoordinates");
			yCoordinateField2.setAccessible(true);
			float yIndoorCoordinates2[]  = (float[]) yCoordinateField.get(graphPathDraw1);
			
			assertEquals(11, yIndoorCoordinates2.length);
			
			Field yCoordinateField3 = GraphPathDraw.class.getDeclaredField("yOutdoorCoordinates");
			yCoordinateField3.setAccessible(true);
			float yOutdoorCoordinates3[]  = (float[]) yCoordinateField3.get(graphPathDraw1);
			
			assertNull(yOutdoorCoordinates3);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 */
	public void testDrawOuterCircle() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawOuterCircle(1, 1, canvas, paint, false, 5);
		
		assertEquals("Gray", graphPathDraw.testStr);
		
		graphPathDraw.drawOuterCircle(1, 1, canvas, paint, true, 5);
		
		assertEquals("Not Gray", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testGetColorIndoor() {
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_2 + 5);
		int color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_STATE_BLUE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_3 + 5);
		color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_MIDNIGHT_BLUE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_4 + 5);
		color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_PURPLE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_6);
		color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_RED, color);
	}
	
	/**
	 * 
	 */
	public void testGetColorOutdoor() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.OD_YAXIS50 + 5);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_DEEPSKY_BLUE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, 210);
		color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_ROYAL_BLUE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, 185);
		color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_INDIGO, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, 160);
		color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_PURPLE, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, 125);
		color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_DEEP_PINK, color);
		
		y = Coordinates.getPxWithRespectToDip(activity, GraphConst.OD_YAXIS400 + 5);
		color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_RED, color);
	}
	/**
	 * 
	 */
	public void testDrawPoint() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawPathAfterLastPoint(2, 2, 10, 10, canvas, paint, true);
		
		assertEquals("outdoor", graphPathDraw.testStr);
		
		graphPathDraw.drawPathAfterLastPoint(2, 2, 10, 10, canvas, paint, false);
		
		assertEquals("indoor", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testGetIndoorYcoordinate() {
		float yfloat = graphPathDraw.getIndoorYcoordinate(-1F);
		assertEquals(-1F, yfloat, 0.00F);
		
		yfloat = graphPathDraw.getIndoorYcoordinate(0);
		assertEquals(coordinates.getIdY0(), yfloat, 0.00F);
	}
	/**
	 * 
	 */
	public void testGetOutdoorYcoordinate() {
		float yfloat = graphPathDraw.getOutdoorYcoordinate(0);
		assertEquals(coordinates.getOdY0(), yfloat, 0.00F);
	}
	/**
	 * 
	 */
	public void testUpDownPath() {
		graphPathDraw.upDownPath(1, 1, 5, 5, new Canvas(), 1, new Paint(), true);
		assertEquals("upward", graphPathDraw.testStr);
		
		graphPathDraw.upDownPath(1, 1, 5, 5, new Canvas(), 0, new Paint(), true);
		assertEquals("downward", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testyCoordinateConditions() {
		graphPathDraw.yCoordinateConditions(1, -1, 2, 2, new Canvas(), new Paint(), true);
		assertEquals("y1 less than zero", graphPathDraw.testStr);
		
		graphPathDraw.yCoordinateConditions(1, 1, 2, -1, new Canvas(), new Paint(), true);
		assertEquals("y2 less than zero", graphPathDraw.testStr);
		
		graphPathDraw.yCoordinateConditions(1, 4, 2, 2, new Canvas(), new Paint(), true);
		Log.i("test", "graphPathDraw.testStr=="+graphPathDraw.testStr);
		assertEquals("upward", graphPathDraw.testStr);
		
		graphPathDraw.yCoordinateConditions(1, 2, 1, 5, new Canvas(), new Paint(), true);
		assertEquals("downward", graphPathDraw.testStr);
	}
 }
