package com.philips.cl.di.dev.pa.detail.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.screens.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.utils.Utils;

public class DetailsAIQ {
	
	private static final String TAG = DetailsAIQ.class.getSimpleName();
	private Context mContext;
	private GraphPathDraw mPathDraw;
	private float xCoordinates[];
	private float yCoordinates[];
	private String xLabels[];
	private float xAsixStep;
	private int yCoordinateLen;
	private int powerOnFlgs[];
	private List<float[]> yAxisValues;
	private Coordinates coordinates;
	private float displayWidth;
	private float y0;
	private int position;
	private boolean isOutdoor;
	
	/**
	 * 
	 * @param context
	 * @param displayWidth
	 * @param yAxisVal
	 * @param yAxisValues
	 * @param powerOnFlgs
	 * @param coordinates
	 * @param position
	 * @param isOutdoor
	 */
	public DetailsAIQ(Context context, float displayWidth, float yAxisVal[], List<float[]> yAxisValues,
			int powerOnFlgs[], Coordinates coordinates, int position, boolean isOutdoor) {
		this.yAxisValues = yAxisValues;
		this.powerOnFlgs = powerOnFlgs;
		this.coordinates = coordinates;
		this.displayWidth = displayWidth;
		this.position = position;
		this.isOutdoor = isOutdoor;
		mContext = context;
		Log.i(TAG, "Is outdoor " + isOutdoor);
		if (coordinates != null ) {
			if (!isOutdoor) {
				/**
				 * Indoor
				 */
				y0 = coordinates.getIdY0();
				yCoordinateLen = yAxisValues.get(position).length;
				xCoordinates = new float[yCoordinateLen];
				yCoordinates = new float[yCoordinateLen];
			} else {
				/**
				 * Outdoor
				 */
				y0 = coordinates.getOdY0();
				yCoordinateLen = yAxisVal.length;
				xCoordinates = new float[yCoordinateLen];
				yCoordinates = new float[yCoordinateLen];
			}
			
			mPathDraw = new GraphPathDraw(context, coordinates);
			
			addXLabelToArry(yCoordinateLen, displayWidth);
			
			addDIPValueToArray(yAxisVal);
			
			/** X axis coordinates.*//*
			for (int i = 0; i < xCoordinates.length; i++) {
				if (i == 0) {
					if (isOutdoor) {
						*//**
						 * Outdoor x0
						 * *//*
						xCoordinates[i] = coordinates.getOdX0();
					} else {
						*//**
						 * Indoor x0
						 * *//*
						xCoordinates[i] = coordinates.getIdX0();
					}
					
				} else {
					*//**
					 * Rest outdoor and indoor x0
					 * *//*
					xCoordinates[i] = xCoordinates[i-1] + xAsixStep;
				}
			}
			
			*//** 
			 * Y axis coordinates
			 * *//*
			if (isOutdoor) {
				*//** Y axis coordinate outdoor.*//*
				for (int i = 0; i < yAxisVal.length; i++) {
					yCoordinates[i] = mPathDraw.getODYaxis(yAxisVal[i]);
				}
			} else {
				*//** Y axis coordinate indoor.*//*
				for (int i = 0; i < yAxisValues.get(position).length; i++) {
					yCoordinates[i] = mPathDraw.getYaxis(yAxisValues.get(position)[i]);
				}
			}*/
		}
	}
	
	/** 
	 * X axis coordinates.
	 * */
	private void addDIPValueToArray(float yAxisVal[]) {
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
			for (int i = 0; i < yAxisVal.length; i++) {
				yCoordinates[i] = mPathDraw.getODYaxis(yAxisVal[i]);
			}
		} else {
			/** Y axis coordinate indoor.*/
			for (int i = 0; i < yAxisValues.get(position).length; i++) {
				yCoordinates[i] = mPathDraw.getYaxis(yAxisValues.get(position)[i]);
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
								mPathDraw.getYaxis(tempYs[i]), canvas, paint, true, 
								yCoordinates[len - 1 - index]);
						paint.setColor(mPathDraw.getColor(yCoordinates[len - 1 - index]));
						paint.setStrokeWidth(coordinates.getStrokeWidth());
					}
					
					path.moveTo(xCoordinates[len - 1 - index], 
							mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
			        path.lineTo(displayWidth, 
			        		mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
					canvas.drawPath(path, paint);
					
					/** Path between circle and last point*/
					path = new Path();
					path.moveTo(displayWidth,
							mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
					
			        path.lineTo(displayWidth +
			        		coordinates.getOuterCirclePadd(), mPathDraw.getYaxis(tempYs[i]));
					canvas.drawPath(path, paint);
					
					/**Outer circle index*/
					paint.setTextSize(coordinates.getIdTxtSize());
					paint.setColor(Color.WHITE);
					canvas.drawText(""+(i+1), displayWidth + coordinates.getOuterIndexPadd(),
							mPathDraw.getYaxis(tempYs[i] - 0.1F), paint);
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
			/*
			Rect rect = new Rect();
			int k = 2;
			float tempYs[] = {8.5F, 7.0F, 5.5F, 4.0F, 2.5F, 1.0F, 0.0F};
			for (int i = 0; i < yAxisValues.size(); i++) {
				Path path;
				
				paint.setColor(Color.GRAY);
		        paint.setStrokeWidth(2);
				paint.setAntiAlias(true);
				paint.setStyle(Paint.Style.STROKE);
				
				*//**Straight path after graph*//*
				path = new Path();
				int len = yAxisValues.get(i).length;
				for (int index = 0; index < len; index++) {
					
					float tempF = yAxisValues.get(i)[len - 1 - index];
					if (tempF != -1) {
						
						*//**Outer circle*//*
						if (i == position) {
							mPathDraw.drawOuterCircle(displayWidth + coordinates.getOuterCirclePadd(),
									mPathDraw.getYaxis(tempYs[i]), canvas, paint, true, 
									yCoordinates[len - 1 - index]);
							paint.setColor(mPathDraw.getColor(yCoordinates[len - 1 - index]));
							paint.setStrokeWidth(coordinates.getStrokeWidth());
						}
						
						path.moveTo(xCoordinates[len - 1 - index], 
								mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
				        path.lineTo(displayWidth, 
				        		mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
						canvas.drawPath(path, paint);
						
						*//** Path between circle and last point*//*
						path = new Path();
						path.moveTo(displayWidth,
								mPathDraw.getYaxis(yAxisValues.get(i)[len - 1 - index]));
						
				        path.lineTo(displayWidth +
				        		coordinates.getOuterCirclePadd(), mPathDraw.getYaxis(tempYs[i]));
						canvas.drawPath(path, paint);
						
						*//**Outer circle index*//*
						paint.setTextSize(coordinates.getIdTxtSize());
						paint.setColor(Color.WHITE);
						canvas.drawText(""+(i+1), displayWidth + coordinates.getOuterIndexPadd(),
								mPathDraw.getYaxis(tempYs[i] - 0.1F), paint);
						break;
					}
				}
			}
		*/}
		
		for (int i = 0; i < xCoordinates.length - 1; i++) {
			mPathDraw.yAaxisConditions(xCoordinates[i], yCoordinates[i],
					xCoordinates[i + 1], yCoordinates[i + 1], canvas, paint, isOutdoor);
		}
		
		/** For drawing point x and y axis coordinate.*/
		for (int i = 0; i < xCoordinates.length; i++) {
			if (yCoordinates[i] != -1) {
				mPathDraw.drawPoint(xCoordinates[i], yCoordinates[i], canvas, paint, isOutdoor);
			}
			
			/**
			 * Draw path after last point
			 */
			if (i == xCoordinates.length - 1 && xCoordinates.length == 24) {
				if (yCoordinates[i] > 0) {
					mPathDraw.drawPathAfterLastPoint(xCoordinates[i], 
							yCoordinates[i], xCoordinates[i]
									+ xAsixStep, yCoordinates[i], canvas, paint, isOutdoor);
				}
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
	private void addXLabelToArry(int arrLen, float width) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		Log.i("outdoor", "hourOfDay=="+hourOfDay);
		String timeStr = OutdoorDetailsActivity.getCurrentCityTime();
		if (timeStr != null && timeStr.length() > 0) {
			try {
				hourOfDay = Integer.parseInt(timeStr.substring(11, 13));
			} catch (NumberFormatException e) {
				e.printStackTrace();
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
				
				String dayStr = Utils.getDayOfWeek(mContext, dayInt);
				
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
			
			xLabels[0] = mContext.getString(R.string.week1);
			xLabels[1] = mContext.getString(R.string.week2);
			xLabels[2] = mContext.getString(R.string.week3);
			xLabels[3] = mContext.getString(R.string.week4);
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
					tempHr = "0" + hourOfDay + ":00";
				} else {
					tempHr = hourOfDay + ":00";
				}
				xLabels[xLabels.length - 1 - j] = tempHr;
				hourOfDay = hourOfDay - 6;
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
		if (yCoordinateLen != 24) {
			
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
		} else {
			/** Drawing rect for power.*/
			if (!isOutdoor) {
				/**Indoor*/
				canvas.drawRect(coordinates.getIdX0(), coordinates.getIdY0() + 
						coordinates.getPowerRect2(), xCoordinates[xCoordinates.length - 1] + xAsixStep, 
						coordinates.getIdY0() + coordinates.getPowerRect1(), paint);
			} 
		}
			
		Rect rect = new Rect();
		/**Check condition for three mode of trend to draw line and x-label.*/
		if (yCoordinateLen == 7) {
			/**For last week*/
			drawLineLabelLastWeek(canvas, paint, rect);
		} else if (yCoordinateLen == 28) {
			/**For last four week*/
			drawLineLabelFourWeek(canvas, paint, rect);
		} else if (yCoordinateLen == 24) {
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
		for (int i = 0; i < xLabels.length; i++) {
			if (i == xLabels.length -1) {
				/**Draw last x-axis label*/
				canvas.drawText(xLabels[i], 
						xCoordinates[i] - coordinates.getXlabelPaddLast(), yMinus, paint);
			} else {
				/**Draw other x-axis label*/
				canvas.drawText(xLabels[i], 
						xCoordinates[i] - coordinates.getxLabelPadd(), yMinus, paint);
			}
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
		paint.setTextSize(coordinates.getIdTxtSize());
		float yMinus = y0 + coordinates.getXlabelPaddLast();
		int len = xLabels[0].length();
		int temp = 1;
		
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
			
			/**Drawing first x-axis power line*/
			canvas.drawLine(coordinates.getIdX0(), 
					coordinates.getIdY0() + coordinates.getPowerY1(), 
					coordinates.getIdX0(), coordinates.getIdY0() + coordinates.getPowerY2(), paint);
		} else {
			/**Drawing first x-axis line outdoor*/
			canvas.drawLine(coordinates.getOdX0(), 
					yAxisMinus, coordinates.getOdX0(), coordinates.getOdY500(), paint);
			
		}
		
		/**Drawing last x-axis line*/
		canvas.drawLine(xCoordinates[xCoordinates.length - 1] + xAsixStep, 
				yAxisMinus, xCoordinates[xCoordinates.length - 1] + xAsixStep, coordinates.getIdY10(), paint);
		if(!isOutdoor) {
			/**Drawing last x-axis power line*/
			canvas.drawLine(xCoordinates[xCoordinates.length - 1] + xAsixStep, 
					y0 + coordinates.getPowerY1(), xCoordinates[xCoordinates.length - 1] 
							+ xAsixStep, y0 + coordinates.getPowerY2(), paint);
		}
		
		/**Drawing other x-axis line*/
		for (int i = 1; i < xCoordinates.length; i++) {
			if (i == 6 || i == 12 || i == 18 ){
				paint.setColor(Color.GRAY);
				/**Drawing dark x-axis line*/
				canvas.drawLine(xCoordinates[i], 
						yAxisMinus, xCoordinates[i], coordinates.getIdY10(), paint);
				if(!isOutdoor) {
					/**Drawing dark x-axis power line*/
					canvas.drawLine(xCoordinates[i], 
							y0 + coordinates.getPowerY1(),
							xCoordinates[i], y0 + coordinates.getPowerY2(), paint);
				}
			} else {
				paint.setColor(Color.LTGRAY);
				/**Drawing light x-axis line*/
				canvas.drawLine(xCoordinates[i], 
						y0, xCoordinates[i], coordinates.getIdY10(), paint);
				if(!isOutdoor) {
					/**Drawing light x-axis power line*/
					paint.setColor(Color.LTGRAY);
					canvas.drawLine(xCoordinates[i], 
							y0 + coordinates.getPowerRect1() , xCoordinates[i], 
							y0 + coordinates.getPowerRect2(), paint);
				}
			}
		}
		
		drawXLineLabel(canvas, paint, rect);
		
		
	}
	
	
	private void drawXLineLabel(Canvas canvas, Paint paint, Rect rect) {
		/**Drawing  x-axis labels*/
		paint.setColor(Color.GRAY);
		paint.setTextSize(coordinates.getIdTxtSize());
		float yMinus = y0 + coordinates.getXlabelPaddLast();
		int len = xLabels[0].length();
		int temp = 0;
		for (int i = 0; i < xLabels.length; i++) {
			if (i == xLabels.length - 1) {
				/**Drawing  x-axis last label*/
				canvas.drawText(xLabels[i], xCoordinates[xCoordinates.length - 1] - 15, yMinus, paint);
			} else {
				/**Drawing  x-axis other label*/
				canvas.drawText(xLabels[i], xCoordinates[temp], yMinus, paint);
			}
	        paint.getTextBounds(xLabels[0], 0, len, rect);
	        temp = temp + 6;
		}
		
		drawPowerLine(canvas, paint, rect);
	}
	
	private void drawPowerLine(Canvas canvas, Paint paint, Rect rect) {
		if(!isOutdoor) {
			/**Drawing  powers labels*/
			canvas.drawText("Power", coordinates.getIdX0(), y0 + coordinates.getPowerLabelPadd(), paint);
			paint.getTextBounds(xLabels[0], 0, "Power".length(), rect);
		}
		if(!isOutdoor) {	
			/** Color power on period*/
			if (powerOnFlgs != null) {
				paint.setAntiAlias(true);
				for (int i = 0; i < xCoordinates.length; i++) {
					if (powerOnFlgs[i] == 1) {
						/**Power On*/
						if (i == xCoordinates.length - 1) {
							/**Color power  last time period*/
							/**Color power graph last time period*/
							//paint.setColor(Color.rgb(225, 232, 255));
							paint.setColor(Color.rgb(200, 200, 225));
							canvas.drawRect(xCoordinates[i] + 1, coordinates.getIdY10(), 
									xCoordinates[i] + xAsixStep - 1, y0, paint);
							
							/**Color power bottom last time period*/
							paint.setColor(GraphConst.COLOR_STATE_BLUE);
							canvas.drawRect(xCoordinates[i] + 1, y0 + coordinates.getPowerRect2(), 
									xCoordinates[i] + xAsixStep - 1, y0
									+ coordinates.getPowerRect1(), paint);
						} else {
							/**Color power other time period*/
							/**Color power graph other time period*/
							//paint.setColor(Color.rgb(225, 232, 255));
							paint.setColor(Color.rgb(200, 200, 225));
							canvas.drawRect(xCoordinates[i] + 1, coordinates.getIdY10(), 
									xCoordinates[i + 1] - 1, y0, paint);
							
							/**Color power bottom other time period*/
							//paint.setColor(Color.rgb(65, 105, 225));
							paint.setColor(GraphConst.COLOR_STATE_BLUE);
							canvas.drawRect(xCoordinates[i] + 1, y0 + coordinates.getPowerRect2(), 
									xCoordinates[i + 1] - 1, y0 + coordinates.getPowerRect1(), paint);
						}
					}else {
						//power off
					}
				}
			}
		}
	}
}
