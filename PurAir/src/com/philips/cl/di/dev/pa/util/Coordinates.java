package com.philips.cl.di.dev.pa.util;

import android.content.Context;

public class Coordinates {
	/**
	 * Indoor and outdoor
	 * */
	private float x1;
	private float y1;
	private float xlabelPaddLast;
	private float xLabelPadd;
	private float multiplyConst;
	private float powerY1;
	private float powerY2;
	private float powerRect1;
	private float powerRect2;
	private float powerLabelPadd;
	private float outerlineDist;
	private float outerCirclePadd;
	private float outerIndexPadd;
	private float strokeWidth;
	
	/**
	 * Indoor
	 * */
	private float idGraphHeight;
	private float idPaddingRight;
	private float idRectMarginLeft;
	private float idRectWidth;  
	private float idY10Point0;
	private float idY5Point5;
	private float idY4Point5; 
	private float idY3Point5;
	private float idY2Point5;
	private float idY1Point5;
	private float idY0Point0;
	
	private float idY10;
	private float idY9;
	private float idY7P5;
	private float idY7;
	private float idY5P5;
	private float idY5;
	private float idY3P5;
	private float idY2P3;
	private float idY1P4;
	private float idY1;
	private float idY0;
	
	private float idX0;
	private float idRadius;
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
	
	
	private static Coordinates coordinates;
	
	private Coordinates (Context ctx) {
		/**
		 * Initialize
		 * */
		x1 = 0;
		y1 = 0;
		xlabelPaddLast = getPxWithRespectToDip(ctx, GraphConst.XLABEL_PADD_LAST);
		xLabelPadd = getPxWithRespectToDip(ctx, GraphConst.XLABEL_PADD);
		multiplyConst = getPxWithRespectToDip(ctx, GraphConst.MULTIPLY_CONST);
		powerY1 = getPxWithRespectToDip(ctx, GraphConst.POWERY1);
		powerY2 = getPxWithRespectToDip(ctx, GraphConst.POWERY2);
		powerRect1 = getPxWithRespectToDip(ctx, GraphConst.POWER_RECT1);
		powerRect2 = getPxWithRespectToDip(ctx, GraphConst.POWER_RECT2);
		powerLabelPadd = getPxWithRespectToDip(ctx, GraphConst.POWER_LABEL_PADD);
		outerlineDist = getPxWithRespectToDip(ctx, GraphConst.OUTER_LINE_DIST);
		outerCirclePadd = getPxWithRespectToDip(ctx, GraphConst.OUTER_CIRCLE_PADD);
		outerIndexPadd = getPxWithRespectToDip(ctx, GraphConst.OUTER_INDEX_PADD);
		strokeWidth = getPxWithRespectToDip(ctx, GraphConst.STROKE_WIDTH);
		
		/**
		 * Indoor
		 * */
		idGraphHeight = getPxWithRespectToDip(ctx, GraphConst.GRAPH_BG_HEGHT);
		idPaddingRight = getPxWithRespectToDip(ctx, GraphConst.ID_PADDING_RIGHT);
		idRectMarginLeft = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_RECT_MARGIN_LEFT);
		idRectWidth = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_RECT_WIDTH);
		idY10Point0 = 0;
		idX0 = getPxWithRespectToDip(ctx, GraphConst.XAXIS0);
		idRadius = getPxWithRespectToDip(ctx, GraphConst.RADIUS);
		idYxLabelPadding = getPxWithRespectToDip(ctx, GraphConst.FIVE);
		idYxTopLabelPadding = getPxWithRespectToDip(ctx, GraphConst.TEN);
		idTxtSize = getPxWithRespectToDip(ctx, GraphConst.TEXT_SIZE);
		
		idY10 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_10);
		idY9 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_9);
		idY7P5 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_7_5);
		idY5P5 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_5_5);
		idY3P5 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_3_5);
		idY2P3 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_2_3);
		idY1P4 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_1_4);
		idY0 = getPxWithRespectToDip(ctx, GraphConst.ID_YAXIS_0_0);
		
		/**
		 * Outdoor
		 * */
		odY500 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS500);
		odY400 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS400);
		odY300 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS300);
		odY200 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS200);
		odY150 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS150);
		odY100 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS100);
		odY50 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS50);
		odY0 = getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS0);
		odX0  = getPxWithRespectToDip(ctx, GraphConst.OD_XAXIS0);
		odRectMarginLeft= getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS_RECT_MARGIN_LEFT);
		odRectWidth= getPxWithRespectToDip(ctx, GraphConst.OD_YAXIS_RECT_WIDTH);
		odPaddingRight = getPxWithRespectToDip(ctx, GraphConst.OD_PADDING_RIGHT);
		
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

	public float getIdY10Point0() {
		return idY10Point0;
	}

	public float getIdY5Point5() {
		return idY5Point5;
	}

	public float getIdY4Point5() {
		return idY4Point5;
	}

	public float getIdY3Point5() {
		return idY3Point5;
	}

	public float getIdY2Point5() {
		return idY2Point5;
	}

	public float getIdY1Point5() {
		return idY1Point5;
	}

	public float getIdY0Point0() {
		return idY0Point0;
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
		return idY7P5;
	}
	public float getIdY7() {
		return idY7;
	}
	public float getIdY6() {
		return idY5P5;
	}
	public float getIdY5() {
		return idY5;
	}
	public float getIdY4() {
		return idY3P5;
	}
	public float getIdY3() {
		return idY2P3;
	}
	public float getIdY2() {
		return idY1P4;
	}
	public float getIdY1() {
		return idY1;
	}
	public float getIdY0() {
		return idY0;
	}
	
	public static float getPxWithRespectToDip(Context context, float dip) {
		return context.getResources().getDisplayMetrics().density * dip;
	}
	
	public static Coordinates getInstance(Context ctx) {
		synchronized(Coordinates.class) {
			if ( null == coordinates ) {
				coordinates = new Coordinates(ctx) ;
			}
		}
		return coordinates ;
	}
	
	public static void reset() {
		coordinates = null ;
	}

}
