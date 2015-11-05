package com.philips.cl.di.dev.pa.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDetailFragment;

public class DrawTrend {

	private DrawTrendPaths mPathDraw;
	private float xCoordinates[];
	private HashMap<Integer, float[]> yCoordinatesMap;
	private String xLabels[];
	private float xAsixStep;
	private int noOffxCoordinate;
	private HashMap<Integer, float[]> yAxisValuesMap;
	float[] yAxisValue;
	private Coordinates coordinates;
	private float displayWidth;
	private float y0;
	private boolean isOutdoor;
	private boolean multipleTrend;

	/**
	 * Outdoor
	 * @param context
	 * @param graphWidh
	 * @param yAxisValuesMap
	 * @param multipleTrend
	 */
	@SuppressLint("UseSparseArrays")
	public DrawTrend(Context context, float graphWidh, HashMap<Integer, float[]> yAxisValuesMap, boolean multipleTrend) {
		this.yAxisValuesMap = yAxisValuesMap;
		this.coordinates = Coordinates.getInstance(context);
		this.displayWidth = graphWidh;
		this.multipleTrend = multipleTrend;
		yCoordinatesMap = new HashMap<Integer, float[]>();
		
		isOutdoor = true;
		Set<Integer> keysSet= yAxisValuesMap.keySet();
		if (keysSet != null && !keysSet.isEmpty()) {
			y0 = coordinates.getOdY0();
			noOffxCoordinate = yAxisValuesMap.get(keysSet.toArray()[0]).length;
			xCoordinates = new float[noOffxCoordinate];

			mPathDraw = new DrawTrendPaths(coordinates, isOutdoor, multipleTrend);

			addXLabelToArry(noOffxCoordinate, displayWidth, context);

			addDIPValueToArray(multipleTrend);
		}
	}

	/**
	 * Indoor
	 * @param context
	 * @param displayWidth
	 * @param yAxisValuesMap
	 */
	@SuppressLint("UseSparseArrays")
	public DrawTrend(Context context, float displayWidth, HashMap<Integer, float[]> yAxisValuesMap) {
		this.yAxisValuesMap = yAxisValuesMap;
		this.coordinates = Coordinates.getInstance(context);
		this.displayWidth = displayWidth;
		yCoordinatesMap = new HashMap<Integer, float[]>();
		isOutdoor = false;
		multipleTrend = false;
		
		Set<Integer> keysSet= yAxisValuesMap.keySet();
		if (keysSet != null && !keysSet.isEmpty()) {
			if (!isOutdoor) {
				/**
				 * Indoor
				 */
				y0 = coordinates.getIdY0();
				noOffxCoordinate = yAxisValuesMap.get(keysSet.toArray()[0]).length;
				xCoordinates = new float[noOffxCoordinate];
			} 
			mPathDraw = new DrawTrendPaths(coordinates, isOutdoor, multipleTrend);

			addXLabelToArry(noOffxCoordinate, displayWidth, context);

			addDIPValueToArray(multipleTrend);
		}
	}


	/** 
	 * X axis coordinates.
	 * */
	private void addDIPValueToArray(boolean multipleTrend) {
		addDIPValuesOfXAxis();

		/** 
		 * Y axis coordinates
		 * */
		if (isOutdoor) {
			/** Y axis coordinate outdoor.*/
			for (Integer key : yAxisValuesMap.keySet()) {
				float[] yAxisValue = yAxisValuesMap.get(key);
				float yCoordinatesTemp[] = new float[yAxisValue.length];
				for (int i = 0; i < yAxisValue.length; i++) {
					yCoordinatesTemp[i] = mPathDraw.getOutdoorYcoordinate(yAxisValue[i]);
				}
				yCoordinatesMap.put(key, yCoordinatesTemp);
			}
		} else {
			/** Y axis coordinate indoor.*/
			for (Integer key : yAxisValuesMap.keySet()) {
				float[] yAxisValue = yAxisValuesMap.get(key);
				float yCoordinatesTemp[] = new float[yAxisValue.length];
				for (int i = 0; i < yAxisValue.length; i++) {
					yCoordinatesTemp[i] = mPathDraw.getIndoorYcoordinate(yAxisValue[i]);
				}
				yCoordinatesMap.put(key, yCoordinatesTemp);
			}
			
		}
	}
	
	private void addDIPValuesOfXAxis() {
		/** X axis coordinates.*/
		for (int i = 0; i < xCoordinates.length; i++) {
			if (i == 0) {
				if (isOutdoor) {
					/**
					 * Outdoor x0
					 * */
					xCoordinates[i] = coordinates.getOdX0();
				} else {
					/**
					 * Indoor x0
					 * */
					xCoordinates[i] = coordinates.getIdX0();
				}

			} else {
				/**
				 * Rest outdoor and indoor x0
				 * */
				xCoordinates[i] = xCoordinates[i-1] + xAsixStep;
			}
		}
	}


	/**The method draw view*/
	public void draw(Canvas canvas, Paint paint) {

		drawVerticalLineXLable(canvas, paint);

		for (Integer key : yCoordinatesMap.keySet()) {
			paint.setColor(key);
			for (int i = 0; i < xCoordinates.length - 1; i++) {
				float y1 = yCoordinatesMap.get(key)[i];
				float y2 = yCoordinatesMap.get(key)[i + 1];
				float x1 = xCoordinates[i];
				float x2 = xCoordinates[i + 1];
				mPathDraw.yCoordinateConditions(x1, y1, x2, y2, canvas, paint, isOutdoor);
				
				drawPoint(x1,  y1, canvas, paint);
			}
			int lastIndex = xCoordinates.length - 1;
			drawPoint(xCoordinates[lastIndex],  yCoordinatesMap.get(key)[lastIndex], canvas, paint);
		}
	}
	
	private void drawPoint(float x, float y, Canvas canvas, Paint paint) {
		if (y != -1) {
			mPathDraw.drawPoint(x,  y, canvas, paint, isOutdoor);
		}
	}

	/**
	 * The method adding x-label into string array.
	 * */
	private void addXLabelToArry(int arrLen, float width, Context context) {

		Calendar cal = Calendar.getInstance();
		if (isOutdoor) {
			cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		} 

		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		Log.i("outdoor", "hourOfDay=="+hourOfDay);
		if (isOutdoor) {
			hourOfDay = OutdoorDetailFragment.getCurrentCityHourOfDay();
		} else {
			hourOfDay = Utils.getCurrentTimeHourOfDay();
		}
		/** X axis label.*/
		if (arrLen == 7) {
			/**
			 * For last week
			 * */
			xLabels = new String[7];
			int dayInt = cal.get(Calendar.DAY_OF_WEEK);
			if (isOutdoor) {
				dayInt = OutdoorDetailFragment.getCurrentCityDayOfWeek();
			} else {
				dayInt = Utils.getCurrentTimeDayOfWeek();
			}
			for (int j = 0; j < 7; j++) {

				String dayStr = Utils.getDayOfWeek(context, dayInt);

				xLabels[6 - j] = dayStr;
				dayInt--;
				if (dayInt <= 0) {
					dayInt = 7;
				}
			}

			/** Calculate x axis steps.*/
			if (isOutdoor) {
				xAsixStep = (width - coordinates.getOdX0()) / (float)6;
			} else {
				xAsixStep = (width - coordinates.getIdX0()) / (float)6;
			}

		} else if (arrLen == 28){
			/**
			 * For last four week
			 * */
			xLabels = new String[4];

			xLabels[0] = context.getString(R.string.week1);
			xLabels[1] = context.getString(R.string.week2);
			xLabels[2] = context.getString(R.string.week3);
			xLabels[3] = context.getString(R.string.week4);
			/** Calculate x axis steps.*/
			if (isOutdoor) {
				xAsixStep = (width - coordinates.getOdX0()) / (float)28;
			} else {
				xAsixStep = (width - coordinates.getIdX0()) / (float)28;
			}

		} else if (arrLen == 24){
			/**
			 * For last day
			 * */
			xLabels = new String[5];

			for (int j = 0; j < xLabels.length; j++) {
				String tempHr = null;
				if (hourOfDay < 10) {
					tempHr = context.getString(R.string.one_zero) + hourOfDay + context.getString(R.string.colon_2_zero);
				} else {
					tempHr = hourOfDay + context.getString(R.string.colon_2_zero);
				}

				xLabels[xLabels.length - 1 - j] = tempHr;
				hourOfDay = hourOfDay - 6;
				if (j == xLabels.length - 2) {
					hourOfDay = hourOfDay + 1;
				}
				if (hourOfDay < 0) {
					hourOfDay = 24 + hourOfDay;
				} 
			}

			/** Calculate x axis steps.*/
			if (isOutdoor) {
				xAsixStep = (width - coordinates.getOdX0()) / (float)25;
			} else {
				xAsixStep = (width - coordinates.getIdX0()) / (float)25;
			}
		}
	}

	/** The drawLineLable( Canvas canvas) method.*/
	private void drawVerticalLineXLable( Canvas canvas, Paint paint) {

		/** Set Color and line width*/
		paint.setColor(Color.rgb(230, 230, 250));
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);
		//paint.setStyle(Paint.Style.STROKE);
		/** 
		 * Drawing rect for horizontal shadow.
		 * */

		if (!isOutdoor) {
			/**Indoor*/
			canvas.drawRect(coordinates.getIdX0(), coordinates.getIdY8(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getIdY6(), paint);
			canvas.drawRect(coordinates.getIdX0(), coordinates.getIdY4(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getIdY3(), paint);
			canvas.drawRect(coordinates.getIdX0(), coordinates.getIdY2(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getIdY0(), paint);
		} else {
			/**outdoor*/
			canvas.drawRect(coordinates.getOdX0(), coordinates.getOdY500(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getOdY400(), paint);
			canvas.drawRect(coordinates.getOdX0(), coordinates.getOdY300(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getOdY200(), paint);
			canvas.drawRect(coordinates.getOdX0(), coordinates.getOdY150(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getOdY100(), paint);
			canvas.drawRect(coordinates.getOdX0(), coordinates.getOdY50(), 
					xCoordinates[xCoordinates.length - 1], coordinates.getOdY0(), paint);
		}

		Rect rect = new Rect();
		/**Check condition for three mode of trend to draw line and x-label.*/
		if (noOffxCoordinate == 7) {
			/**For last week*/
			drawLineLabelLastWeek(canvas, paint, rect);
		} else if (noOffxCoordinate == 28) {
			/**For last four week*/
			drawLineLabelFourWeek(canvas, paint, rect);
		} else if (noOffxCoordinate == 24) {
			/**For last day*/
			drawLineLabelDay(canvas, paint, rect);
		}

	}

	/**The method draw vertical lines and x-label for last week*/
	private void drawLineLabelLastWeek(Canvas canvas, Paint paint, Rect rect) {
		paint.setColor(Color.GRAY);
		float yAxisMinus = y0 + coordinates.getIdYxLabelPadding();

		/**Drawing first y-axis line.*/
		if (!isOutdoor) {
			canvas.drawLine(coordinates.getIdX0(), 
					yAxisMinus, coordinates.getIdX0(), coordinates.getIdY10(), paint);
		} else {
			canvas.drawLine(coordinates.getOdX0(), 
					yAxisMinus, coordinates.getOdX0(), coordinates.getOdY500(), paint);
		}

		/**Drawing other y-axis line.*/
		for (int i = 1; i < xCoordinates.length; i++) {
			canvas.drawLine(xCoordinates[i], 
					yAxisMinus, xCoordinates[i], coordinates.getIdY10(), paint);
		}

		float yMinus = y0 + coordinates.getXlabelPaddLast();
		int len = xLabels[0].length();

		/**Drawing x-axis labels.*/
		paint.setTextSize(coordinates.getIdTxtSize());
		paint.setTextAlign(Paint.Align.CENTER);
		for (int i = 0; i < xLabels.length; i++) {
			canvas.drawText(xLabels[i], 
					xCoordinates[i] , yMinus, paint);
			paint.getTextBounds(xLabels[0], 0, len, rect);

		}
	}

	/**The method draw vertical lines and x-label for last four week*/
	private void drawLineLabelFourWeek(Canvas canvas, Paint paint, Rect rect) {
		paint.setColor(Color.GRAY);
		float yAxisMinus = y0 + coordinates.getIdYxLabelPadding();

		/**Drawing first y-axis line.*/
		if (!isOutdoor) {
			canvas.drawLine(coordinates.getIdX0(), 
					yAxisMinus, coordinates.getIdX0(), coordinates.getIdY10(), paint);
		} else {
			canvas.drawLine(coordinates.getOdX0(), 
					yAxisMinus, coordinates.getOdX0(), coordinates.getOdY500(), paint);
		}


		/**Drawing others y-axis line.*/
		for (int i = 1; i < xCoordinates.length; i++) {
			if (i == 6 || i == 13 || i == 20 || i == xCoordinates.length - 1) {
				/**Drawing dark y-axis line.*/
				paint.setColor(Color.GRAY);
				canvas.drawLine(xCoordinates[i], 
						yAxisMinus, xCoordinates[i], coordinates.getIdY10(), paint);
			} else {
				/**Drawing light y-axis line.*/
				paint.setColor(Color.LTGRAY);
				canvas.drawLine(xCoordinates[i], 
						y0, xCoordinates[i], coordinates.getIdY10(), paint);
			}

		}

		paint.setColor(Color.GRAY);
		paint.setTextAlign(Paint.Align.CENTER);
		float yMinus = y0 + coordinates.getXlabelPaddLast();
		int len = xLabels[0].length();
		int temp = 3;

		/**Drawing x-axis label*/
		paint.setTextSize(coordinates.getIdTxtSize());
		for (int i = 0; i < xLabels.length; i++) {
			canvas.drawText(xLabels[i], xCoordinates[temp], yMinus, paint);
			paint.getTextBounds(xLabels[0], 0, len, rect);
			temp = temp + 7;
		}
	}

	/**The method draw vertical lines and x-label for last day week*/
	private void drawLineLabelDay(Canvas canvas, Paint paint, Rect rect) {
		paint.setColor(Color.GRAY);


		float yAxisMinus = y0 + coordinates.getIdYxLabelPadding();
		if(!isOutdoor) {
			/**Drawing first x-axis line indoor*/
			canvas.drawLine(coordinates.getIdX0(), 
					yAxisMinus, coordinates.getIdX0(), coordinates.getIdY10(), paint);

		} else {
			/**Drawing first x-axis line outdoor*/
			canvas.drawLine(coordinates.getOdX0(), 
					yAxisMinus, coordinates.getOdX0(), coordinates.getOdY500(), paint);

		}


		/**Drawing other x-axis line*/
		for (int i = 1; i < xCoordinates.length; i++) {
			if (i == 5 || i == 11 || i == 17 ||  i == xCoordinates.length - 1){
				paint.setColor(Color.GRAY);
				/**Drawing dark x-axis line*/
				canvas.drawLine(xCoordinates[i], 
						yAxisMinus, xCoordinates[i], coordinates.getIdY10(), paint);
			} else {
				paint.setColor(Color.LTGRAY);
				/**Drawing light x-axis line*/
				canvas.drawLine(xCoordinates[i], 
						y0, xCoordinates[i], coordinates.getIdY10(), paint);
			}
		}

		drawXLineLabel(canvas, paint, rect);
	}


	private void drawXLineLabel(Canvas canvas, Paint paint, Rect rect) {
		/**Drawing  x-axis labels*/
		paint.setColor(Color.GRAY);
		paint.setTextSize(coordinates.getIdTxtSize());
		paint.setTextAlign(Paint.Align.CENTER);
		float yMinus = y0 + coordinates.getXlabelPaddLast();
		int len = xLabels[0].length();
		int temp = 0;
		for (int i = 0; i < xLabels.length; i++) {
			if (i== 1) {
				canvas.drawText(xLabels[i], xCoordinates[5], yMinus, paint);
			} else {
				canvas.drawText(xLabels[i], xCoordinates[temp], yMinus, paint);
			}

			paint.getTextBounds(xLabels[0], 0, len, rect);
			if (i == 1) {
				temp = temp + 5;
			} else {
				temp = temp + 6;
			}

		}

	}

}
