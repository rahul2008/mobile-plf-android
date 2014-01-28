package com.philips.cl.di.dev.pa.detail.utils;

import android.content.Context;

public class Coordinates {
	/**
	 * Indoor and outdoor
	 * */
	private float x1 = 0;
	private float y1 = 0;
	private float xlabelPaddLast = 15;
	private float xLabelPadd = 6;
	private float multiplyConst = .5F;
	private float powerY1 = 40F;
	private float powerY2 = 20F;
	private float powerRect1 = 35F;
	private float powerRect2 = 25F;
	private float powerLabelPadd = 50F;
	private float outerlineDist = 40F;
	private float outerCirclePadd = 40F;
	private float outerIndexPadd = 37F;
	private float strokeWidth;
	
	/**
	 * Indoor
	 * */
	private float idGraphHeight= 280;
	private float idPaddingRight = 30;
	private float idRectMarginLeft = 20;
	private float idRectWidth = 25;  
	private float idY10_0 = 0;
	private float idY5_5 = 40;
	private float idY4_5 = 80; 
	private float idY3_5 = 120;
	private float idY2_5 = 160;
	private float idY1_5 = 200;
	private float idY0_0 = 260;
	
	private float idY10;
	private float idY9;
	private float idY8;
	private float idY7;
	private float idY6;
	private float idY5;
	private float idY4;
	private float idY3;
	private float idY2;
	private float idY1;
	private float idY0;
	
	private float idX0 = 30;
	private float idRadius = 3;
	private float idYxLabelPadding;
	private float idYxTopLabelPadding;
	private float idTxtSize;
	
	/**
	 * Outdoor
	 * */
	private float odX0;
	private float odY500;
	private float odY400;
	private float odY300;
	private float odY200;
	private float odY150;
	private float odY100;
	private float odY50;
	private float odY0;
	private float odRectMarginLeft;
	private float odRectWidth;
	private float odPaddingRight;
	
	private Context context;
	
	public Coordinates () {
		
	}
	public Coordinates (Context ctx) {
		/**
		 * Initialize
		 * */
		context = ctx;
		x1 = 0;
		y1 = 0;
		xlabelPaddLast = getPxWithRespectToDip(GraphConst.XLABEL_PADD_LAST);
		xLabelPadd = getPxWithRespectToDip(GraphConst.XLABEL_PADD);
		multiplyConst = getPxWithRespectToDip(GraphConst.MULTIPLY_CONST);
		powerY1 = getPxWithRespectToDip(GraphConst.POWERY1);
		powerY2 = getPxWithRespectToDip(GraphConst.POWERY2);
		powerRect1 = getPxWithRespectToDip(GraphConst.POWER_RECT1);
		powerRect2 = getPxWithRespectToDip(GraphConst.POWER_RECT2);
		powerLabelPadd = getPxWithRespectToDip(GraphConst.POWER_LABEL_PADD);
		outerlineDist = getPxWithRespectToDip(GraphConst.OUTER_LINE_DIST);
		outerCirclePadd = getPxWithRespectToDip(GraphConst.OUTER_CIRCLE_PADD);
		outerIndexPadd = getPxWithRespectToDip(GraphConst.OUTER_INDEX_PADD);
		strokeWidth = getPxWithRespectToDip(GraphConst.STROKE_WIDTH);
		
		/**
		 * Indoor
		 * */
		idGraphHeight = getPxWithRespectToDip(GraphConst.GRAPH_BG_HEGHT);
		idPaddingRight = getPxWithRespectToDip(GraphConst.ID_PADDING_RIGHT);
		idRectMarginLeft = getPxWithRespectToDip(GraphConst.ID_YAXIS_RECT_MARGIN_LEFT);
		idRectWidth = getPxWithRespectToDip(GraphConst.ID_YAXIS_RECT_WIDTH);
		idY10_0 = 0;
		idY5_5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_5_5);
		idY4_5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_4_5);
		idY3_5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_3_5);
		idY2_5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_2_5);
		idY1_5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_1_5);
		idY0_0 = getPxWithRespectToDip(GraphConst.ID_YAXIS_0);
		idX0 = getPxWithRespectToDip(GraphConst.XAXIS0);
		idRadius = getPxWithRespectToDip(GraphConst.RADIUS);
		idYxLabelPadding = getPxWithRespectToDip(GraphConst.FIVE);
		idYxTopLabelPadding = getPxWithRespectToDip(GraphConst.TEN);
		idTxtSize = getPxWithRespectToDip(GraphConst.TEXT_SIZE);
		
		idY10 = getPxWithRespectToDip(GraphConst.ID_YAXIS_10);
		idY9 = getPxWithRespectToDip(GraphConst.ID_YAXIS_9);
		idY8 = getPxWithRespectToDip(GraphConst.ID_YAXIS_8);
		idY7 = getPxWithRespectToDip(GraphConst.ID_YAXIS_7);
		idY6 = getPxWithRespectToDip(GraphConst.ID_YAXIS_6);
		idY5 = getPxWithRespectToDip(GraphConst.ID_YAXIS_5);
		idY4 = getPxWithRespectToDip(GraphConst.ID_YAXIS_4);
		idY3 = getPxWithRespectToDip(GraphConst.ID_YAXIS_3);
		idY2 = getPxWithRespectToDip(GraphConst.ID_YAXIS_2);
		idY1 = getPxWithRespectToDip(GraphConst.ID_YAXIS_1);
		idY0 = getPxWithRespectToDip(GraphConst.ID_YAXIS_0_0);
		
		/**
		 * Outdoor
		 * */
		odY500 = getPxWithRespectToDip(GraphConst.OD_YAXIS500);
		odY400 = getPxWithRespectToDip(GraphConst.OD_YAXIS400);
		odY300 = getPxWithRespectToDip(GraphConst.OD_YAXIS300);
		odY200 = getPxWithRespectToDip(GraphConst.OD_YAXIS200);
		odY150 = getPxWithRespectToDip(GraphConst.OD_YAXIS150);
		odY100 = getPxWithRespectToDip(GraphConst.OD_YAXIS100);
		odY50 = getPxWithRespectToDip(GraphConst.OD_YAXIS50);
		odY0 = getPxWithRespectToDip(GraphConst.OD_YAXIS0);
		odX0  = getPxWithRespectToDip(GraphConst.OD_XAXIS0);
		odRectMarginLeft= getPxWithRespectToDip(GraphConst.OD_YAXIS_RECT_MARGIN_LEFT);
		odRectWidth= getPxWithRespectToDip(GraphConst.OD_YAXIS_RECT_WIDTH);
		odPaddingRight = getPxWithRespectToDip(GraphConst.OD_PADDING_RIGHT);
		
	}

	public float getX1() {
		return x1;
	}

	public float getY1() {
		return y1;
	}
	
	public float getXlabelPaddLast() {
		return xlabelPaddLast;
	}

	public float getxLabelPadd() {
		return xLabelPadd;
	}
	
	public float getMultiplyConst() {
		return multiplyConst;
	}

	public float getPowerY1() {
		return powerY1;
	}

	public float getPowerY2() {
		return powerY2;
	}

	public float getPowerRect1() {
		return powerRect1;
	}

	public float getPowerRect2() {
		return powerRect2;
	}

	public float getPowerLabelPadd() {
		return powerLabelPadd;
	}

	public float getOuterlineDist() {
		return outerlineDist;
	}

	public float getOuterCirclePadd() {
		return outerCirclePadd;
	}

	public float getOuterIndexPadd() {
		return outerIndexPadd;
	}
	
	public float getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Indoor
	 * */
	public float getIdGraphHeight() {
		return idGraphHeight;
	}

	public float getIdPaddingRight() {
		return idPaddingRight;
	}

	public float getIdRectMarginLeft() {
		return idRectMarginLeft;
	}

	public float getIdRectWidth() {
		return idRectWidth;
	}

	public float getIdY10_0() {
		return idY10_0;
	}

	public float getIdY5_5() {
		return idY5_5;
	}

	public float getIdY4_5() {
		return idY4_5;
	}

	public float getIdY3_5() {
		return idY3_5;
	}

	public float getIdY2_5() {
		return idY2_5;
	}

	public float getIdY1_5() {
		return idY1_5;
	}

	public float getIdY0_0() {
		return idY0_0;
	}

	public float getIdX0() {
		return idX0;
	}

	public float getIdRadius() {
		return idRadius;
	}

	public float getIdYxLabelPadding() {
		return idYxLabelPadding;
	}

	public float getIdYxTopLabelPadding() {
		return idYxTopLabelPadding;
	}

	public float getIdTxtSize() {
		return idTxtSize;
	}
	

	/**
	 * Outdoor
	 * */
	public float getOdX0() {
		return odX0;
	}

	public float getOdY500() {
		return odY500;
	}

	public float getOdY400() {
		return odY400;
	}

	public float getOdY300() {
		return odY300;
	}

	public float getOdY200() {
		return odY200;
	}

	public float getOdY150() {
		return odY150;
	}

	public float getOdY100() {
		return odY100;
	}

	public float getOdY50() {
		return odY50;
	}

	public float getOdY0() {
		return odY0;
	}

	public float getOdRectMarginLeft() {
		return odRectMarginLeft;
	}

	public float getOdRectWidth() {
		return odRectWidth;
	}

	public float getOdPaddingRight() {
		return odPaddingRight;
	}
	
	
	public float getIdY10() {
		return idY10;
	}
	public float getIdY9() {
		return idY9;
	}
	public float getIdY8() {
		return idY8;
	}
	public float getIdY7() {
		return idY7;
	}
	public float getIdY6() {
		return idY6;
	}
	public float getIdY5() {
		return idY5;
	}
	public float getIdY4() {
		return idY4;
	}
	public float getIdY3() {
		return idY3;
	}
	public float getIdY2() {
		return idY2;
	}
	public float getIdY1() {
		return idY1;
	}
	public float getIdY0() {
		return idY0;
	}
	
	public float getPxWithRespectToDip(float dip) {
		return context.getResources().getDisplayMetrics().density * dip;
	}

}
