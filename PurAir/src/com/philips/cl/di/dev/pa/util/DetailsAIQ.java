package com.philips.cl.di.dev.pa.util;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;

public class DetailsAIQ {
	
	private static final String TAG = DetailsAIQ.class.getSimpleName();
	private GraphPathDraw mPathDraw;
	private float xCoordinates[];
	private float yCoordinates[];
	private String xLabels[];
	private float xAsixStep;
	private int noOffxCoordinate;
	private List<float[]> yAxisValues;
	float[] yAxisValue;
	private Coordinates coordinates;
	private float displayWidth;
	private float y0;
	private int position;
	private boolean isOutdoor;
	
	/**
	 * Outdoor
	 * @param context
	 * @param graphWidh
	 * @param yAxisValues
	 * @param coordinates
	 */
	public DetailsAIQ(Context context, float graphWidh, float[] yAxisValue, Coordinates coordinates) {
		this.yAxisValue = yAxisValue;
		this.coordinates = coordinates;
		this.displayWidth = graphWidh;
		this.isOutdoor = true;
		if (coordinates != null ) {
			y0 = coordinates.getOdY0();
			noOffxCoordinate = yAxisValue.length;
			xCoordinates = new float[noOffxCoordinate];
			yCoordinates = new float[noOffxCoordinate];
			
			mPathDraw = new GraphPathDraw(coordinates, true);
			
			addXLabelToArry(noOffxCoordinate, displayWidth, context);
			
			addDIPValueToArray();
		}
	}
	
	/**
	 * Indoor
	 * @param context
	 * @param displayWidth
	 * @param yAxisVal
	 * @param yAxisValues
	 * @param powerOnFlgs
	 * @param coordinates
	 * @param position
	 * @param isOutdoor
	 */
	public DetailsAIQ(Context context, float displayWidth, List<float[]> yAxisValues,
			int powerOnFlgs[], Coordinates coordinates, int position) {
		this.yAxisValues = yAxisValues;
		this.coordinates = coordinates;
		this.displayWidth = displayWidth;
		this.position = position;
		this.isOutdoor = false;
		Log.i(TAG, "Is outdoor " + isOutdoor);
		if (coordinates != null ) {
			if (!isOutdoor) {
				/**
				 * Indoor
				 */
				y0 = coordinates.getIdY0();
				noOffxCoordinate = yAxisValues.get(position).length;
				xCoordinates = new float[noOffxCoordinate];
				yCoordinates = new float[noOffxCoordinate];
			} 
			mPathDraw = new GraphPathDraw(coordinates, false);
			
			addXLabelToArry(noOffxCoordinate, displayWidth, context);
			
			addDIPValueToArray();
			
		}
	}
	
	
	/** 
	 * X axis coordinates.
	 * */
	private void addDIPValueToArray() {
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
		
		/** 
		 * Y axis coordinates
		 * */
		if (isOutdoor) {
			/** Y axis coordinate outdoor.*/
			for (int i = 0; i < yAxisValue.length; i++) {
				yCoordinates[i] = mPathDraw.getOutdoorYcoordinate(yAxisValue[i]);
			}
		} else {
			/** Y axis coordinate indoor.*/
			for (int i = 0; i < yAxisValues.get(position).length; i++) {
				yCoordinates[i] = mPathDraw.getIndoorYcoordinate(yAxisValues.get(position)[i]);
			}
		}
	}
	
	private void drawRectOutdoor(Canvas canvas, Paint paint) {
		float tempYs[] = {8.5F, 7.0F, 5.5F, 4.0F, 2.5F, 1.0F, 0.0F};
		for (int i = 0; i < yAxisValues.size(); i++) {
			Path path;
			
			paint.setColor(Color.GRAY);
	        paint.setStrokeWidth(2);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			
			/**Straight path after graph*/
			path = new Path();
			int len = yAxisValues.get(i).length;
			for (int index = 0; index < len; index++) {
				
				float tempF = yAxisValues.get(i)[len - 1 - index];
				if (tempF != -1) {
					
					/**Outer circle*/
					if (i == position) {
						mPathDraw.drawOuterCircle(displayWidth + coordinates.getOuterCirclePadd(),
								mPathDraw.getIndoorYcoordinate(tempYs[i]), canvas, paint, true, 
								yCoordinates[len - 1 - index]);
						paint.setColor(mPathDraw.getColorIndoor(yCoordinates[len - 1 - index]));
						paint.setStrokeWidth(coordinates.getStrokeWidth());
					}
					
					path.moveTo(xCoordinates[len - 1 - index], 
							mPathDraw.getIndoorYcoordinate(yAxisValues.get(i)[len - 1 - index]));
			        path.lineTo(displayWidth, 
			        		mPathDraw.getIndoorYcoordinate(yAxisValues.get(i)[len - 1 - index]));
					canvas.drawPath(path, paint);
					
					/** Path between circle and last point*/
					path = new Path();
					path.moveTo(displayWidth,
							mPathDraw.getIndoorYcoordinate(yAxisValues.get(i)[len - 1 - index]));
					
			        path.lineTo(displayWidth +
			        		coordinates.getOuterCirclePadd(), mPathDraw.getIndoorYcoordinate(tempYs[i]));
					canvas.drawPath(path, paint);
					
					/**Outer circle index*/
					paint.setTextSize(coordinates.getIdTxtSize());
					paint.setColor(Color.WHITE);
					paint.setTextAlign(Align.CENTER);
					canvas.drawText(""+(i+1), displayWidth + coordinates.getOuterCirclePadd(),
							mPathDraw.getIndoorYcoordinate(tempYs[i] - 0.1F), paint);
					break;
				}
			}
		}
	}
	
	/**The method draw view*/
	public void draw(Canvas canvas, Paint paint) {
		
		drawVerticalLineXLable(canvas, paint);
		
		if (!isOutdoor) {
			drawRectOutdoor(canvas, paint);
		}
		
		for (int i = 0; i < xCoordinates.length - 1; i++) {
			mPathDraw.yCoordinateConditions(xCoordinates[i], yCoordinates[i],
					xCoordinates[i + 1], yCoordinates[i + 1], canvas, paint, isOutdoor);
		}
		
		/** For drawing point x and y axis coordinate.*/
		for (int i = 0; i < xCoordinates.length; i++) {
			if (yCoordinates[i] != -1) {
				mPathDraw.drawPoint(xCoordinates[i], yCoordinates[i], canvas, paint, isOutdoor);
			}
			
		}
	}
	
	/**
	 * Set bottom index background for indoor
	 * */
	public void setIndexImgBg (ImageView indexBgImg) {
		int len = yCoordinates.length;
		for (int index = 0; index < len; index++) {
			float y = yCoordinates[len - 1 - index];
			if (y != -1) {
				if (y > coordinates.getIdY2()) {
					/**Blue color circle*/
					indexBgImg.setImageResource(R.drawable.aqi_pink_circle_2x);
				} else if (y > coordinates.getIdY3() && y <= coordinates.getIdY2()) {
					/**Navy color circle*/
					indexBgImg.setImageResource(R.drawable.aqi_bluedark_circle_2x);
				} else if (y > coordinates.getIdY4() && y <= coordinates.getIdY3()) {
					/**Purple color circle*/
					indexBgImg.setImageResource(R.drawable.aqi_purple_circle_2x);
				} else {
					/**Red color circle*/
					indexBgImg.setImageResource(R.drawable.aqi_red_circle_2x);
				}
				break;
			}
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
			String timeStr = OutdoorDetailsActivity.getCurrentCityTime();
			if (timeStr != null && timeStr.length() > 0) {
				try {
					hourOfDay = Integer.parseInt(timeStr.substring(11, 13));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		/** X axis label.*/
		if (arrLen == 7) {
			/**
			 * For last week
			 * */
			xLabels = new String[7];
			int dayInt = cal.get(Calendar.DAY_OF_WEEK);
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
		if (noOffxCoordinate != 24) {
			
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
			if (i == 7 || i == 14 || i == 21 || i == xCoordinates.length - 1) {
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
