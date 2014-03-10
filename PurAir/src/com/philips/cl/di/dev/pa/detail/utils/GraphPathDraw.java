package com.philips.cl.di.dev.pa.detail.utils;

import java.util.ArrayList;
import java.util.Collections;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;


public class GraphPathDraw {
	private float yIndoorCoordinates[];
	private float yOutdoorCoordinates[]; 
	private Coordinates coordinates;
	public String testStr="";
	
	public GraphPathDraw (Coordinates coordinates, boolean isOutdoor) {
		this.coordinates = coordinates;
		if (!isOutdoor) {
			/**
			 * Indoor
			 * */
			yIndoorCoordinates = new float[11];
			yIndoorCoordinates[0] = coordinates.getIdY0();
			yIndoorCoordinates[1] = coordinates.getIdY1();
			yIndoorCoordinates[2] = coordinates.getIdY2();
			yIndoorCoordinates[3] = coordinates.getIdY3();
			yIndoorCoordinates[4] = coordinates.getIdY4();
			yIndoorCoordinates[5] = coordinates.getIdY5();
			yIndoorCoordinates[6] = coordinates.getIdY6();
			yIndoorCoordinates[7] = coordinates.getIdY7();
			yIndoorCoordinates[8] = coordinates.getIdY8();
			yIndoorCoordinates[9] = coordinates.getIdY9();
			yIndoorCoordinates[10] = coordinates.getIdY10();
		} else {
			/**
			 * Outdoor
			 * */
			yOutdoorCoordinates = new float[8];
			yOutdoorCoordinates[0] = coordinates.getOdY0();
			yOutdoorCoordinates[1] = coordinates.getOdY50();
			yOutdoorCoordinates[2] = coordinates.getOdY100();
			yOutdoorCoordinates[3] = coordinates.getOdY150();
			yOutdoorCoordinates[4] = coordinates.getOdY200();
			yOutdoorCoordinates[5] = coordinates.getOdY300();
			yOutdoorCoordinates[6] = coordinates.getOdY400();
			yOutdoorCoordinates[7] = coordinates.getOdY500();
		}
		
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
			cPaint.setColor(getColorIndoor(yColor));
			testStr = "Not Gray";
		} else {
			cPaint.setColor(Color.GRAY);
			testStr = "Gray";
		}
		
		canvas.drawPath(circle, cPaint);
	}
	
	/**Get color for indoor*/
	public int getColorIndoor (float y) {
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
	public int getColorOutdoor(float y) {
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
			cPaint.setColor(getColorOutdoor(y));
		} else {
			cPaint.setColor(getColorIndoor(y)); 
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
        	paint.setColor(getColorOutdoor(y));
        	testStr = "outdoor";
        } else {
        	paint.setColor(getColorIndoor(y)); 
        	testStr = "indoor";
        }
        
		paint.setStrokeWidth(coordinates.getStrokeWidth());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
        
		canvas.drawPath(path, paint);
	}
	
	/**The method to find the y axis pixel corresponding value for indoor.*/
	public float getIndoorYcoordinate(float y) {
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
	public float getOutdoorYcoordinate(float y) {
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
	public void upDownPath(float x1, float y1, 
			float x2, float y2, Canvas canvas, int upDown, Paint paint, boolean isOutdoor) {
		
		float yCoordinates[] = null;
		if (isOutdoor) {
			yCoordinates = yOutdoorCoordinates;
		} else {
			yCoordinates = yIndoorCoordinates;
		}
		
		ArrayList<Float> inBetweenYcoordines = new ArrayList<Float>();
		ArrayList<XYCoordinate> xyCoordinates = new ArrayList<XYCoordinate>();
		XYCoordinate xy;
		/**Finding in between x-coordinates.*/
		if (upDown == 1) {
			testStr = "upward";
			/** Four graph drawing upward.*/
			for (int i = 0; i < yCoordinates.length; i++) {
				if (yCoordinates[i] > y2 && yCoordinates[i] < y1) {
					inBetweenYcoordines.add(yCoordinates[i]);
				}
			}
		} else {
			testStr = "downward";
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
				float xx1 = getXcoordinateBetweenPoints(x1, y1, x2, y2, inBetweenYcoordines.get(i));
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
				drawOutdoorPaths(x1, y1, x2, y2, canvas, upDown, paint);
			} else {
				drawIndoorPaths(x1, y1, x2, y2, canvas, upDown, paint);
			}
			
			return;
		}
		
		/**Drawing all color paths*/
		for (int j = 0; j < xyCoordinates.size() - 1; j++) {
			if (isOutdoor) {
				drawOutdoorPaths(xyCoordinates.get(j).getxCoordinate(), 
						xyCoordinates.get(j).getyCoordinate(), xyCoordinates.get(j+1).getxCoordinate(), 
						xyCoordinates.get(j+1).getyCoordinate(), canvas, upDown, paint);
			} else {
				drawIndoorPaths(xyCoordinates.get(j).getxCoordinate(), 
						xyCoordinates.get(j).getyCoordinate(), xyCoordinates.get(j+1).getxCoordinate(), 
						xyCoordinates.get(j+1).getyCoordinate(), canvas, upDown, paint);
			}
			
		}
	}
	
	/** This method check condition graph path draw towards up or down for indoor and outdoor.*/
	public void yCoordinateConditions(float x1, float y1, float x2, 
			float y2, Canvas canvas, Paint paint, boolean isOutdoor) {
		if (y1 < 0) {
			testStr ="y1 less than zero";
			return;
		}else if (y2 < 0) {
			testStr ="y2 less than zero";
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
	public void drawIndoorPaths(float x1, float y1, float x2, 
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
	public void drawOutdoorPaths(float x1, float y1, float x2, 
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
	public float getXcoordinateBetweenPoints(float x1, float y1, 
			float x2, float y2, float y) {
		float x = 0;
		float m = (y2-y1)/(x2-x1);
		float ty = (y-y1)/m;
		x = ty+x1;
		return x;
	}
	
	
}
