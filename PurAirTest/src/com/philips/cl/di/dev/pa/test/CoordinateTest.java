package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.GraphConst;

public class CoordinateTest extends TestCase {
	private Coordinates coordinates;
	
	@Override
	protected void setUp() throws Exception {
		coordinates = Coordinates.getInstance(PurAirApplication.getAppContext());
		super.setUp();
	}
	
	public void testGetX1() {
		float dip1 = coordinates.getX1();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetY1() {
		float dip1 = coordinates.getY1();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetXlabelPaddLast() {
		float dip1 = coordinates.getXlabelPaddLast();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.XLABEL_PADD_LAST);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetxLabelPadd() {
		float dip1 = coordinates.getxLabelPadd();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.XLABEL_PADD);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetMultiplyConst() {
		float dip1 = coordinates.getMultiplyConst();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.MULTIPLY_CONST);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetPowerY1() {
		float dip1 = coordinates.getPowerY1();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.POWERY1);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetPowerY2() {
		float dip1 = coordinates.getPowerY2();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.POWERY2);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetPowerRect1() {
		float dip1 = coordinates.getPowerRect1();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.POWER_RECT1);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetPowerRect2() {
		float dip1 = coordinates.getPowerRect2();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.POWER_RECT2);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetPowerLabelPadd() {
		float dip1 = coordinates.getPowerLabelPadd();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.POWER_LABEL_PADD);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOuterlineDist() {
		float dip1 = coordinates.getOuterlineDist();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OUTER_LINE_DIST);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOuterCirclePadd() {
		float dip1 = coordinates.getOuterCirclePadd();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OUTER_CIRCLE_PADD);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOuterIndexPadd() {
		float dip1 = coordinates.getOuterIndexPadd();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OUTER_INDEX_PADD);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetStrokeWidth() {
		float dip1 = coordinates.getStrokeWidth();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.STROKE_WIDTH);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdGraphHeight() {
		float dip1 = coordinates.getIdGraphHeight();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.GRAPH_BG_HEGHT);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdPaddingRight() {
		float dip1 = coordinates.getIdPaddingRight();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_PADDING_RIGHT);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdRectMarginLeft() {
		float dip1 = coordinates.getIdRectMarginLeft();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_RECT_MARGIN_LEFT);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdRectWidth() {
		float dip1 = coordinates.getIdRectWidth();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_RECT_WIDTH);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY10Point0() {
		float dip1 = coordinates.getIdY10Point0();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY5Point5() {
		float dip1 = coordinates.getIdY5Point5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY4Point5() {
		float dip1 = coordinates.getIdY4Point5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY3Point5() {
		float dip1 = coordinates.getIdY3Point5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY2Point5() {
		float dip1 = coordinates.getIdY2Point5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY1Point5() {
		float dip1 = coordinates.getIdY1Point5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY0Point0() {
		float dip1 = coordinates.getIdY0Point0();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdX0() {
		float dip1 = coordinates.getIdX0();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.XAXIS0);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdRadius() {
		float dip1 = coordinates.getIdRadius();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.RADIUS);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdYxLabelPadding() {
		float dip1 = coordinates.getIdYxLabelPadding();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.FIVE);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdYxTopLabelPadding() {
		float dip1 = coordinates.getIdYxTopLabelPadding();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.TEN);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdTxtSize() {
		float dip1 = coordinates.getIdTxtSize();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.TEXT_SIZE);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdX0() {
		float dip1 = coordinates.getOdX0();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_XAXIS0);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY500() {
		float dip1 = coordinates.getOdY500();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS500);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY400() {
		float dip1 = coordinates.getOdY400();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS400);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY300() {
		float dip1 = coordinates.getOdY300();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS300);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY200() {
		float dip1 = coordinates.getOdY200();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS200);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY150() {
		float dip1 = coordinates.getOdY150();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS150);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY100() {
		float dip1 = coordinates.getOdY100();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS100);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY50() {
		float dip1 = coordinates.getOdY50();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS50);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdY0() {
		float dip1 = coordinates.getOdY0();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS0);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdRectMarginLeft() {
		float dip1 = coordinates.getOdRectMarginLeft();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS_RECT_MARGIN_LEFT);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdRectWidth() {
		float dip1 = coordinates.getOdRectWidth();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_YAXIS_RECT_WIDTH);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetOdPaddingRight() {
		float dip1 = coordinates.getOdPaddingRight();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.OD_PADDING_RIGHT);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY10() {
		float dip1 = coordinates.getIdY10();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_10);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY9() {
		float dip1 = coordinates.getIdY9();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_9);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY8() {
		float dip1 = coordinates.getIdY8();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_7_5);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY7() {
		float dip1 = coordinates.getIdY7();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY6() {
		float dip1 = coordinates.getIdY6();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_5_5);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY5() {
		float dip1 = coordinates.getIdY5();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY4() {
		float dip1 = coordinates.getIdY4();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_3_5);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY3() {
		float dip1 = coordinates.getIdY3();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_2_3);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY2() {
		float dip1 = coordinates.getIdY2();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_1_4);
		assertEquals(dip2, dip1, 0F);
	}
	
	public void testGetIdY1() {
		float dip1 = coordinates.getIdY1();
		assertEquals(0, dip1, 0F);
	}
	
	public void testGetIdY0() {
		float dip1 = coordinates.getIdY0();
		float dip2 = Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), GraphConst.ID_YAXIS_0_0);
		assertEquals(dip2, dip1, 0F);
	}

}
