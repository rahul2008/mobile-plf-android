package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.GraphPathDraw;
import com.philips.cl.di.dev.pa.view.FontTextView;

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
		FontTextView lastDayBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
		FontTextView lastWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
		FontTextView lastFourWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		ImageView mapClickImg = (ImageView) activity.findViewById(R.id.oDmapInlarge);
		
		assertEquals(true, lastDayBtn.isClickable());
		assertEquals(true, lastWeekBtn.isClickable());
		assertEquals(true, lastFourWeekBtn.isClickable());
		assertEquals(true, mapClickImg.isClickable());
	}
	
	public void testButtonText() {
		FontTextView lastDayBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastDayLabel);
		FontTextView lastWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastWeekLabel);
		FontTextView lastFourWeekBtn = (FontTextView) activity.findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		
		assertEquals("Last day", lastDayBtn.getText().toString());
		assertEquals("Last 7 days", lastWeekBtn.getText().toString());
		assertEquals("Last 4 weeks", lastFourWeekBtn.getText().toString());
	}
	
	@UiThreadTest
	public void testSetAdviceIconTex() {
		try {
			Method setAdviceIconTexMethod = OutdoorDetailsActivity.class.getDeclaredMethod("setAdviceIconTex", int.class);
			setAdviceIconTexMethod.setAccessible(true);
			setAdviceIconTexMethod.invoke(activity, 0);

			FontTextView avoidTxt = (FontTextView) activity.findViewById(R.id.avoidOutdoorTxt); 
			FontTextView openWindowTxt = (FontTextView) activity.findViewById(R.id.openWindowTxt); 
			FontTextView maskTxt = (FontTextView) activity.findViewById(R.id.maskTxt);
			
			assertEquals("OK for outdoor activities", avoidTxt.getText().toString());
			assertEquals("Masks not needed", maskTxt.getText().toString());
			assertEquals("OK to open windows", openWindowTxt.getText().toString());
			
				
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetXCoordinates_1() {
		try {
			Method getXCoordinatesMethod = OutdoorDetailsActivity.class.getDeclaredMethod("getXCoordinates", (Class<?>[])null);
			getXCoordinatesMethod.setAccessible(true);
			getXCoordinatesMethod.invoke(activity, (Object[])null);
			
			Field keysField1 = OutdoorDetailsActivity.class.getDeclaredField("lastDayAQIReadings");
			keysField1.setAccessible(true);
			float lastDayAQIReadings[] = (float[])keysField1.get(activity);
			assertEquals(24, lastDayAQIReadings.length);
			
				
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetXCoordinates_2() {
		try {
			Method getXCoordinatesMethod = OutdoorDetailsActivity.class.getDeclaredMethod("getXCoordinates", (Class<?>[])null);
			getXCoordinatesMethod.setAccessible(true);
			getXCoordinatesMethod.invoke(activity, (Object[])null);
			
			Field keysField2 = OutdoorDetailsActivity.class.getDeclaredField("last7dayAQIReadings");
			keysField2.setAccessible(true);
			float last7dayAQIReadings[] = (float[])keysField2.get(activity);
			assertEquals(7, last7dayAQIReadings.length);
				
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetXCoordinates_3() {
		try {
			Method getXCoordinatesMethod = OutdoorDetailsActivity.class.getDeclaredMethod("getXCoordinates", (Class<?>[])null);
			getXCoordinatesMethod.setAccessible(true);
			getXCoordinatesMethod.invoke(activity, (Object[])null);
			
			Field keysField3 = OutdoorDetailsActivity.class.getDeclaredField("last4weekAQIReadings");
			keysField3.setAccessible(true);
			float last4weekAQIReadings[] = (float[])keysField3.get(activity);
			assertEquals(28, last4weekAQIReadings.length);
				
		} catch (Exception e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void testGraphPathDrawOutdoor() {
		
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
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		}
		
	}
	
	public void testGraphPathDrawIndoor() {
		
		try {
			/**
			 * Indoor
			 */
			GraphPathDraw graphPathDraw1 = new GraphPathDraw(coordinates, false);
			
			Field yCoordinateField2 = GraphPathDraw.class.getDeclaredField("yIndoorCoordinates");
			yCoordinateField2.setAccessible(true);
			float yIndoorCoordinates2[]  = (float[]) yCoordinateField2.get(graphPathDraw1);
			
			assertEquals(11, yIndoorCoordinates2.length);
			
			Field yCoordinateField3 = GraphPathDraw.class.getDeclaredField("yOutdoorCoordinates");
			yCoordinateField3.setAccessible(true);
			float yOutdoorCoordinates3[]  = (float[]) yCoordinateField3.get(graphPathDraw1);
			
			assertNull(yOutdoorCoordinates3);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			TestCase.fail(e.getMessage());
		}
		
	}
	/**
	 * 
	 */
	public void testDrawOuterCircle_1() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawOuterCircle(1, 1, canvas, paint, false, 5);
		assertEquals("Gray", graphPathDraw.testStr);
	}
	
	public void testDrawOuterCircle_2() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawOuterCircle(1, 1, canvas, paint, true, 5);
		assertEquals("Not Gray", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testGetColorIndoor_1() {
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_2 + 5);
		int color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_STATE_BLUE, color);
	}
	
	public void testGetColorIndoor_2() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_3 + 5);
		int color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_MIDNIGHT_BLUE, color);
	}
	
	public void testGetColorIndoor_3() {
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_4 + 5);
		int color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_PURPLE, color);
	}
	
	public void testGetColorIndoor_4() {
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.ID_YAXIS_6);
		int color = graphPathDraw.getColorIndoor(y);
		assertEquals(GraphConst.COLOR_RED, color);
	}
	/**
	 * 
	 */
	public void testGetColorOutdoor_1() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, GraphConst.OD_YAXIS50 + 5);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_DEEPSKY_BLUE, color);
	}
	
	public void testGetColorOutdoor_2() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, 210);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_ROYAL_BLUE, color);
	}
	public void testGetColorOutdoor_3() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, 185);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_INDIGO, color);
	}
	
	public void testGetColorOutdoor_4() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, 160);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_PURPLE, color);
	}
	
	public void testGetColorOutdoor_5() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, 125);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_DEEP_PINK, color);
	}

	public void testGetColorOutdoor_6() {
		
		float y = Coordinates.getPxWithRespectToDip(activity, 50);
		int color = graphPathDraw.getColorOutdoor(y);
		assertEquals(GraphConst.COLOR_RED, color);
	}
	/**
	 * 
	 */
	public void testDrawPoint_1() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawPathAfterLastPoint(2, 2, 10, 10, canvas, paint, true);
		assertEquals("outdoor", graphPathDraw.testStr);
	}
	
	public void testDrawPoint_2() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		graphPathDraw.drawPathAfterLastPoint(2, 2, 10, 10, canvas, paint, false);
		assertEquals("indoor", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testGetIndoorYcoordinate_1() {
		float yfloat = graphPathDraw.getIndoorYcoordinate(-1F);
		assertEquals(-1F, yfloat, 0.00F);
	}
	
	public void testGetIndoorYcoordinate_2() {
		float yfloat = graphPathDraw.getIndoorYcoordinate(0);
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
	public void testUpDownPath_1() {
		graphPathDraw.upDownPath(1, 1, 5, 5, new Canvas(), 1, new Paint(), true);
		assertEquals("upward", graphPathDraw.testStr);
	}
	
	public void testUpDownPath_2() {
		graphPathDraw.upDownPath(1, 1, 5, 5, new Canvas(), 0, new Paint(), true);
		assertEquals("downward", graphPathDraw.testStr);
	}
	/**
	 * 
	 */
	public void testyCoordinateConditions_1() {
		graphPathDraw.yCoordinateConditions(1, -1, 2, 2, new Canvas(), new Paint(), true);
		assertEquals("y1 less than zero", graphPathDraw.testStr);
	}
	
	public void testyCoordinateConditions_2() {
		graphPathDraw.yCoordinateConditions(1, 1, 2, -1, new Canvas(), new Paint(), true);
		assertEquals("y2 less than zero", graphPathDraw.testStr);
	}
	
	public void testyCoordinateConditions_3() {
		graphPathDraw.yCoordinateConditions(1, 4, 2, 2, new Canvas(), new Paint(), true);
		assertEquals("upward", graphPathDraw.testStr);
	}
	
	public void testyCoordinateConditions_4() {
		graphPathDraw.yCoordinateConditions(1, 2, 1, 5, new Canvas(), new Paint(), true);
		assertEquals("downward", graphPathDraw.testStr);
	}
 }
