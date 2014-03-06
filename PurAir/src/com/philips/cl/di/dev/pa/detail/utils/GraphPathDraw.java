package com.philips.cl.di.dev.pa.detail.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;


public class GraphPathDraw {
	private float yINCoordinates[];
	private float yODCoordinates[]; 
	private Context mContext;
	//private UtilGraph uGraph;
	private Coordinates coordinates;
	
	public GraphPathDraw (Context context, Coordinates coordinates) {
		mContext = context;
		this.coordinates = coordinates;
		//uGraph = new UtilGraph(context);
		
		/**
		 * Indoor
		 * */
		yINCoordinates = new float[11];
		yINCoordinates[0] = coordinates.getIdY0();
		yINCoordinates[1] = coordinates.getIdY1();
		yINCoordinates[2] = coordinates.getIdY2();
		yINCoordinates[3] = coordinates.getIdY3();
		yINCoordinates[4] = coordinates.getIdY4();
		yINCoordinates[5] = coordinates.getIdY5();
		yINCoordinates[6] = coordinates.getIdY6();
		yINCoordinates[7] = coordinates.getIdY7();
		yINCoordinates[8] = coordinates.getIdY8();
		yINCoordinates[9] = coordinates.getIdY9();
		yINCoordinates[10] = coordinates.getIdY10();
		
		/**
		 * Outdoor
		 * */
		yODCoordinates = new float[8];
		yODCoordinates[0] = coordinates.getOdY0();
		yODCoordinates[1] = coordinates.getOdY50();
		yODCoordinates[2] = coordinates.getOdY100();
		yODCoordinates[3] = coordinates.getOdY150();
		yODCoordinates[4] = coordinates.getOdY200();
		yODCoordinates[5] = coordinates.getOdY300();
		yODCoordinates[6] = coordinates.getOdY400();
		yODCoordinates[7] = coordinates.getOdY500();
		
				
		
	}
	
	/** The method to draw circle.*/
	public void drawOuterCircle(
			float x, float y, Canvas canvas, Paint paint, boolean isColor, float yColor) {
		Path circle = new Path(); 
		circle.addCircle(x, y, coordinates.getIdTxtSize(), Direction.CW);
		Paint cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStyle(Paint.Style.FILL);
		//cPaint.setStrokeWidth(3);
		if (isColor) {
			cPaint.setColor(getColor(yColor));
		} else {
			cPaint.setColor(Color.GRAY);
		}
		
		canvas.drawPath(circle, cPaint);
	}
	
	/**Get color for indoor*/
	public int getColor (float y) {
		int color = Color.RED;
		if (y > coordinates.getIdY2()) {
			/**Blue color circle*/
			color = GraphConst.COLOR_STATE_BLUE; 
		} else if (y > coordinates.getIdY3() && y <= coordinates.getIdY2()) {
			/**Navy color circle*/
			color = GraphConst.COLOR_MIDNIGHT_BLUE;
		} else if (y > coordinates.getIdY4() && y <= coordinates.getIdY3()) {
			/**Purple color circle*/
			color = GraphConst.COLOR_PURPLE;
		} else {
			/**Red color circle*/
			color = GraphConst.COLOR_RED; 
		}
		return color;
	}
	
	
	/**Get color for outdoor*/
	public int getColorOD(float y) {
		int color = Color.GRAY;
		if (y > coordinates.getOdY50()) {
			/**Blue color circle*/
			color = GraphConst.COLOR_DEEPSKY_BLUE; 
		} else if (y > coordinates.getOdY100() && y <= coordinates.getOdY50()) {
			/**Navy color circle*/
			color = GraphConst.COLOR_ROYAL_BLUE;
		} else if (y > coordinates.getOdY150() && y <= coordinates.getOdY100()) {
			/**Purple color circle*/
			color = GraphConst.COLOR_INDIGO;
		} else if (y > coordinates.getOdY200() && y <= coordinates.getOdY150()) {
			/**Purple color circle*/
			color = GraphConst.COLOR_PURPLE;
		} else if (y > coordinates.getOdY300() && y <= coordinates.getOdY200()) {
			/**Purple color circle*/
			color = GraphConst.COLOR_DEEP_PINK;
		} else {
			/**Red color circle*/
			color = GraphConst.COLOR_RED; 
		}
		return color;
	}
	
	/** The method to draw circle for indoor.*/
	public void drawPoint(float x, float y, Canvas canvas, Paint paint, boolean isOutdoor) {
		Path circle = new Path(); 
		circle.addCircle(x, y, coordinates.getIdRadius(), Direction.CW);
		Paint cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStyle(Paint.Style.FILL);
		if (isOutdoor) {
			cPaint.setColor(getColorOD(y));
		} else {
			cPaint.setColor(getColor(y)); 
		}
		canvas.drawPath(circle, cPaint);
	}
	
	/** The method to draw path after last point for outdoor.*/
	public void drawPathAfterLastPoint(
			float x, float y, float x1, float y1, Canvas canvas, Paint paint, boolean isOutdoor) {
		Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x1, y1);
        
        if (isOutdoor) {
        	paint.setColor(getColorOD(y));
        } else {
        	paint.setColor(getColor(y)); 
        }
        
		paint.setStrokeWidth(coordinates.getStrokeWidth());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
        
		canvas.drawPath(path, paint);
	}
	
	/**The method to find the y axis pixel corresponding value for indoor.*/
	public float getYaxis(float y) {
		float yFloat = coordinates.getIdY0();
		if (y == -1) return -1;
		if (y <= 0) {
			return yFloat;
		} else {
			yFloat = yFloat - (y*coordinates.getIdY9());
			if (yFloat < 0) {
				yFloat = 0;
			}
			return yFloat;
		}
		
	}
	
	/**The method to find the y axis pixel corresponding value for outdoor.*/
	public float getODYaxis(float y) {
		float yfloat = coordinates.getOdY0();
		if (y <= 0) {
			return yfloat;
		} else {
			yfloat = yfloat - (y * coordinates.getMultiplyConst());
			if (yfloat < 0) {
				yfloat = 0;
			}
			return yfloat;
		}
		
	}
	
	/**The method to find the x and y axis, when draw graph up and down for indoor.*/
	private void upDownPath(float x1, float y1, 
			float x2, float y2, Canvas canvas, int upDown, Paint paint, boolean isOutdoor) {
		
		float yCoordinates[] = null;
		if (isOutdoor) {
			yCoordinates = yODCoordinates;
		} else {
			yCoordinates = yINCoordinates;
		}
		
		ArrayList<Float> inBetweenYcoordines = new ArrayList<Float>();
		ArrayList<XYCoordinate> xyCoordinates = new ArrayList<XYCoordinate>();
		XYCoordinate xy;
		/**Finding in between x-coordinates.*/
		if (upDown == 1) {
			/** Four graph drawing upward.*/
			for (int i = 0; i < yCoordinates.length; i++) {
				if (yCoordinates[i] > y2 && yCoordinates[i] < y1) {
					inBetweenYcoordines.add(yCoordinates[i]);
				}
			}
		} else {
			/** Four graph drawing downward.*/
			for (int i = 0; i < yCoordinates.length; i++) {
				if (yCoordinates[i] > y1 && yCoordinates[i] < y2) {
					inBetweenYcoordines.add(yCoordinates[i]);
				}
			}
			Collections.reverse(inBetweenYcoordines);
			
		}
		
		/**Condition x-coordinates belong to in between.*/
		if (inBetweenYcoordines.size() > 0) {
			xy = new XYCoordinate();
			xy.setxCoordinate(x1);
			xy.setyCoordinate(y1);
			xyCoordinates.add(xy);
			
			for (int i = 0 ; i < inBetweenYcoordines.size() ; i++) {
				float xx1 = getXaxisBetween(x1, y1, x2, y2, inBetweenYcoordines.get(i));
				xy = new XYCoordinate();
				xy.setxCoordinate(xx1);
				xy.setyCoordinate(inBetweenYcoordines.get(i));
				xyCoordinates.add(xy);
			}
			
			xy = new XYCoordinate();
			xy.setxCoordinate(x2);
			xy.setyCoordinate(y2);
			xyCoordinates.add(xy);
			
		} else {
			/**Simple one color path drawing.*/
			if (isOutdoor) {
				drawODPaths(x1, y1, x2, y2, canvas, upDown, paint);
			} else {
				drawINPaths(x1, y1, x2, y2, canvas, upDown, paint);
			}
			
			return;
		}
		
		/**Drawing all color paths*/
		for (int j = 0; j < xyCoordinates.size() - 1; j++) {
			if (isOutdoor) {
				drawODPaths(xyCoordinates.get(j).getxCoordinate(), 
						xyCoordinates.get(j).getyCoordinate(), xyCoordinates.get(j+1).getxCoordinate(), 
						xyCoordinates.get(j+1).getyCoordinate(), canvas, upDown, paint);
			} else {
				drawINPaths(xyCoordinates.get(j).getxCoordinate(), 
						xyCoordinates.get(j).getyCoordinate(), xyCoordinates.get(j+1).getxCoordinate(), 
						xyCoordinates.get(j+1).getyCoordinate(), canvas, upDown, paint);
			}
			
		}
	}
	
	/** This method check condition graph path draw towards up or down for indoor.*/
	public void yAaxisConditions(float x1, float y1, float x2, 
			float y2, Canvas canvas, Paint paint, boolean isOutdoor) {
		if (y1 < 0) {
			return;
		}else if (y2 < 0) {
			return;
		}else {
			if (y1 >= y2) {
				/**The graph path draw towards up.*/
				upDownPath(x1, y1, x2, y2, canvas, 1, paint, isOutdoor);
			}else {
				/**The graph path draw towards down.*/
				upDownPath(x1, y1, x2, y2, canvas, 0, paint, isOutdoor);
			}
		}
		
	}
	
	/** The method drawing path for indoor.*/
	public void drawINPaths(float x1, float y1, float x2, 
			float y2, Canvas canvas, int upDown, Paint paint) {
		Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
		if (upDown == 1) {	
			/**The conditions for color, to draw graph upward.*/
			if (y1 > coordinates.getIdY2() && y1 <= coordinates.getIdY0()) {
				/**Blue color*/
    	        paint.setColor(GraphConst.COLOR_STATE_BLUE);
			} else if (y1 > coordinates.getIdY3() && y1 <= coordinates.getIdY2()) {
				/**Navy color*/
    	        paint.setColor(GraphConst.COLOR_MIDNIGHT_BLUE);
				
			} else if (y1 > coordinates.getIdY4() && y1 <= coordinates.getIdY3()) {
				/**Purple color*/
    	        paint.setColor(GraphConst.COLOR_PURPLE);
			} else {
				/**Red color*/
    	        paint.setColor(GraphConst.COLOR_RED);
			}
		} else {
			/**The conditions for color, to draw graph down.*/
			if (y1 >= coordinates.getIdY2()) {
				/**Blue color*/
				paint.setColor(GraphConst.COLOR_STATE_BLUE);
			} else if (y1 >= coordinates.getIdY3() && y1 < coordinates.getIdY2()) {
				/**Navy color*/
				 paint.setColor(GraphConst.COLOR_MIDNIGHT_BLUE);
			}else if (y1 >= coordinates.getIdY4() && y1 < coordinates.getIdY2()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_PURPLE);
			}else if (y1 < coordinates.getIdY4()) {
				/**Red color*/
				paint.setColor(GraphConst.COLOR_RED);
			}
		}
		paint.setStrokeWidth(coordinates.getStrokeWidth());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
        
		canvas.drawPath(path, paint);
	}
	
	/** The method drawing path for indoor.*/
	public void drawODPaths(float x1, float y1, float x2, 
			float y2, Canvas canvas, int upDown, Paint paint) {
		Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
		if (upDown == 1) {	
			/**The conditions for color, to draw graph upward.*/
			if (y1 > coordinates.getOdY50() && y1 <= coordinates.getOdY0()) {
				/**Blue color*/
				paint.setColor(GraphConst.COLOR_DEEPSKY_BLUE);
			} else if (y1 > coordinates.getOdY100() && y1 <= coordinates.getOdY50()) {
				/**Navy color*/
				paint.setColor(GraphConst.COLOR_ROYAL_BLUE);
				
			} else if (y1 > coordinates.getOdY150() && y1 <= coordinates.getOdY100()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_INDIGO);
			}else if (y1 > coordinates.getOdY200() && y1 <= coordinates.getOdY150()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_PURPLE);
			} else if (y1 > coordinates.getOdY300() && y1 <= coordinates.getOdY200()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_DEEP_PINK);
			}else {
				/**Red color*/
    	        paint.setColor(GraphConst.COLOR_RED);
			}
		} else {
			/**The conditions for color, to draw graph down.*/
			if (y1 >= coordinates.getOdY50()) {
				/**Blue color*/
				paint.setColor(GraphConst.COLOR_DEEPSKY_BLUE);
			} else if (y1 >= coordinates.getOdY100() && y1 < coordinates.getOdY50()) {
				/**Navy color*/
				paint.setColor(GraphConst.COLOR_ROYAL_BLUE);
			} else if (y1 >= coordinates.getOdY150() && y1 < coordinates.getOdY100()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_INDIGO);
			} else if (y1 >= coordinates.getOdY200() && y1 < coordinates.getOdY150()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_PURPLE);
			} else if (y1 >= coordinates.getOdY300() && y1 < coordinates.getOdY200()) {
				/**Purple color*/
				paint.setColor(GraphConst.COLOR_DEEP_PINK);
			} else if (y1 < coordinates.getOdY300()) {
				/**Red color*/
				paint.setColor(GraphConst.COLOR_RED);
			}
		}
		paint.setStrokeWidth(coordinates.getStrokeWidth());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
        
		canvas.drawPath(path, paint);
	}
	
	/**The method to find the x axis between changing condition.*/
	private float getXaxisBetween(float x1, float y1, 
			float x2, float y2, float y) {
		float x = 0;
		float m = (y2-y1)/(x2-x1);
		float ty = (y-y1)/m;
		x = ty+x1;
		return x;
	}

}
