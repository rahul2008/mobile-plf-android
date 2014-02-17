package com.philips.cl.di.dev.pa.detail.utils;

import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR_SKIES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN_AT_TIMES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_DRIZZLE;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MIST;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PARTLY_CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SNOW;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SUNNY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.TORRENTIAL_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animRotation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.SessionDto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

public class GraphConst {
	
	/** The Graph constants.*/
 	
	public static final int COLOR_DEEPSKY_BLUE = Color.rgb(95, 187, 235);
	public static final int COLOR_ROYAL_BLUE = Color.rgb(58, 111, 182);
	public static final int COLOR_INDIGO = Color.rgb(101, 66, 148);
	public static final int COLOR_PURPLE = Color.rgb(149, 28, 128);
	public static final int COLOR_DEEP_PINK = Color.rgb(192, 21, 81);
	public static final int COLOR_RED = Color.rgb(219, 16, 23);
	public static final int COLOR_DODLE_BLUE = Color.rgb(30, 144, 255);
	public static final int COLOR_STATE_BLUE = Color.rgb(39, 80, 159);
	public static final int COLOR_MIDNIGHT_BLUE = Color.rgb(74,	54,	141);
	
	
	public static final float GRAPH_BG_START_XAXIS = 0;
	public static final float GRAPH_BG_START_YAXIS = 0;
	public static final float GRAPH_BG_HEGHT = 280;
	public static final float ID_PADDING_RIGHT = 80;
	public static final float ID_YAXIS_RECT_MARGIN_LEFT = 20;
	public static final float ID_YAXIS_RECT_WIDTH = 25;
	public static final float ID_YAXIS_10 = 0;
	public static final float ID_YAXIS_5_5 = 40;
	public static final float ID_YAXIS_4_5 = 80;
	public static final float ID_YAXIS_3_5 = 120;
	public static final float ID_YAXIS_2_5 = 160;
	public static final float ID_YAXIS_1_5 = 200;
	public static final float ID_YAXIS_0 = 260;
	
	public static final float ID_YAXIS_9 = 25;
	public static final float ID_YAXIS_8 = 50;
	public static final float ID_YAXIS_7 = 75;
	public static final float ID_YAXIS_6 = 100;
	public static final float ID_YAXIS_5 = 125;
	public static final float ID_YAXIS_4 = 150;
	public static final float ID_YAXIS_3 = 175;
	public static final float ID_YAXIS_2 = 200;
	public static final float ID_YAXIS_1 = 225;
	public static final float ID_YAXIS_0_0 = 250;
	
	public static final float OD_YAXIS_RECT_MARGIN_LEFT = 30;
	public static final float OD_YAXIS_RECT_WIDTH = 35;
	public static final float OD_PADDING_RIGHT = 50;
	public static final float OD_YAXIS0 = 250;
	public static final float OD_YAXIS50 = 225;
	public static final float OD_YAXIS100 = 200;
	public static final float OD_YAXIS150 = 175;
	public static final float OD_YAXIS200 = 150;
	public static final float OD_YAXIS300 = 100;
	public static final float OD_YAXIS400 = 50;
	public static final float OD_YAXIS500 = 0;
	public static final float OD_XAXIS0 = 40;
	
	public static final float XAXIS0 = 30;
	public static final float RADIUS = 3;
	public static final float FIVE = 5;
	public static final float TEN = 10;
	public static final float XLABEL_PADD = 6;
	public static final float XLABEL_PADD_LAST = 15;
	public static final float TEXT_SIZE = 12;
	public static final float MULTIPLY_CONST = .5F;
	public static final float POWERY1 = 40;
	public static final float POWERY2 = 20;
	public static final float POWER_RECT1 = 35;
	public static final float POWER_RECT2 = 25;
	public static final float POWER_LABEL_PADD = 50;
	public static final float OUTER_LINE_DIST = 30;
	public static final float OUTER_CIRCLE_PADD = 30;
	public static final float OUTER_INDEX_PADD = 27;
	public static final float STROKE_WIDTH = 2;
	
	public static final List<Integer> outdoorAQIPercentageList = new ArrayList<Integer>();
	
	public String getDayOfWeek (Context contex, int dayInt) {
		switch (dayInt) {
		case 1:
			return contex.getString(R.string.l7d_xaxis_label1);
		case 2:
			return contex.getString(R.string.l7d_xaxis_label2);
		case 3:
			return contex.getString(R.string.l7d_xaxis_label3);
		case 4:
			return contex.getString(R.string.l7d_xaxis_label4);
		case 5:
			return contex.getString(R.string.l7d_xaxis_label5);
		case 6:
			return contex.getString(R.string.l7d_xaxis_label6);
		case 7:
			return contex.getString(R.string.l7d_xaxis_label7);
		}
		return null;
	}
	
	
	public Drawable getOutdoorTemperatureImage(Context contex,String weatherDesc, String isDayTime) {
		Drawable weatherImage = null;
		if(weatherDesc == null || weatherDesc.equals("")) {
			return null;
		}
		
		if((weatherDesc.compareToIgnoreCase(SUNNY)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
		} else if ((weatherDesc.compareToIgnoreCase(MIST)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.mist);
		} else if ((weatherDesc.compareToIgnoreCase(CLOUDY)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.cloudy);
		}else if ((weatherDesc.compareToIgnoreCase(PARTLY_CLOUDY)) == 0) {
			
			if(isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(R.drawable.partly_cloudy);
			else
				weatherImage = contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
			//weatherImage = contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR_SKIES)) == 0) {
			if(isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(R.drawable.clear_sky_night);
			//weatherImage = contex.getResources().getDrawable(R.drawable.clear_sky_night);
		} else if ((weatherDesc.compareToIgnoreCase(SNOW)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.snow);
		} else if ((weatherDesc.compareToIgnoreCase(LIGHT_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(LIGHT_DRIZZLE) == 0)) {
			weatherImage = contex.getResources().getDrawable(R.drawable.light_rain_shower);
		} else if ((weatherDesc.compareToIgnoreCase(PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.light_rain_with_thunder);
		} else if ((weatherDesc.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(TORRENTIAL_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(HEAVY_RAIN) == 0)) {
			weatherImage = contex.getResources().getDrawable(R.drawable.heavy_rain);
		} else if ((weatherDesc.compareToIgnoreCase(HEAVY_RAIN_AT_TIMES)) == 0) {
			//TODO : Replace with proper icon. Icon not found, replacing with heavy raind
			weatherImage = contex.getResources().getDrawable(R.drawable.heavy_rain);
		} else if ((weatherDesc.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.moderate_rain_with_thunder);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR)) == 0) {
			if(isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(R.drawable.clear_sky_night);
		} else {
			weatherImage = contex.getResources().getDrawable(R.drawable.light_rain_shower);
		}	
		
		return weatherImage;
	}
	
	public void setOutdoorWeatherDirImg(
			Context contex, float windSpeed, String windDir,float degree, ImageView iv) {
		//System.out.println("SETTING Wind Direction: windSpeed= "+windSpeed+"  degree= "+degree);
		Drawable weatherImage = null;
		if((windDir == null || windDir.equals("")) || degree < 0) {
			return ;
		}
		//System.out.println("SETTING Wind Direction: windSpeed= "+windSpeed+"  degree= "+degree);
		if (windSpeed < 15) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_2x);
		} else if (windSpeed >= 15 && windSpeed < 25) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_2x);
		} else if (windSpeed >= 25){
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_2x);
		}
		
		iv.setImageDrawable(weatherImage);
		float fconst = 180;
		ObjectAnimator.ofFloat(iv, animRotation, 0, fconst - degree).setDuration(2000).start();
	}
	
	/*private void rotateAQICircle(int de, ImageView iv) {
		System.out.println("Rotate circlr by aqi = " + aqi + "    " +iv);
		float ratio = (float) (300.0/500.0);
		float roatation = 100 * (300.0f/500.0f);
		//ViewHelper.setPivotX(iv, iv.getWidth());
		//ViewHelper.setPivotY(iv, iv.getHeight());
		ObjectAnimator.ofFloat(iv, animRotation, 0, roatation).setDuration(2000).start();
	}*/
	
	public float getPxWithRespectToDip(Context context, float dip) {
		return context.getResources().getDisplayMetrics().density * dip;
	}
	
	public static void calculateOutdoorAQIValues() {
		int tempG = 0;
		int tempC = 0;
		SessionDto sessionDto = null;
		try {
			sessionDto = SessionDto.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sessionDto != null && sessionDto.getOutdoorEventDto() != null){
			if (outdoorAQIPercentageList != null ) {
				outdoorAQIPercentageList.clear();
			}
			int idx[] = sessionDto.getOutdoorEventDto().getIdx();
			float[] lastDayAQIReadings = new float[24]; 
			float[] last7dayAQIReadings = new float[7]; 
			float[] last4weekAQIReadings = new float[28];
			/**last day days*/
			/**
			 * Adding last 24 data into lastDayReadings array, from index 696 t0 719 
			 */
			int lastDayHr = 24;
			if (idx != null) {
				tempG = 0;
				tempC = 0;
				for (int i = 0; i < lastDayAQIReadings.length; i++) {
					lastDayAQIReadings[i] = idx[lastDayHr - 1 - i];
					if (idx[lastDayHr - 1 - i] < 100) {
						tempG ++;
					}
					tempC ++;
				}
				percentage(tempG, tempC);
			}
			
			/**last 7 days*/
			/**
			 * Adding last 7 days data into last7dayReadings array, from index last to till 7 days
			 */
			Calendar calender = Calendar.getInstance();
			int hr = calender.get(Calendar.HOUR_OF_DAY);
			if (hr == 0) {
				hr = 24;
			}
			int last7dayHrs = (6*24) + hr;
			
			if (idx != null) {
				float sum = 0;
				float avg = 0;
				int j = 0;
				tempG = 0;
				tempC = 0;
				for (int i = 0; i < last7dayHrs; i++) {
					float x = idx[last7dayHrs - 1 - i];
					sum = sum + x;
					if (i == 23 || i == 47 || i == 71 
							|| i == 95 || i == 119 || i == 143) {
						avg = sum / (float)24;
						last7dayAQIReadings[j] = avg;
						if (avg < 100) {
							tempG ++;
						}
						tempC ++;
						
						j++;
						sum = 0;
						avg = 0;
					} else if (i == last7dayHrs - 1) {
						avg = sum / (float)hr;
						last7dayAQIReadings[j] = avg;
						if (avg < 100) {
							tempG ++;
						}
						tempC ++;
						
						sum = 0;
						avg = 0;
					}
				}
				percentage(tempG, tempC);
			}
	
			/**last 4 weeks*/
			/**
			 * TODO - Explain the logic in 3lines
			 */
			int last4WeekHrs = (3*7*24) + (6*24) + hr;
	
			if (idx != null) {
				int count = 1;
				float sum = 0;
				float avg = 0;
				int j = 0;
				tempG = 0;
				tempC = 0;
				for (int i = 0; i < last4WeekHrs; i++) {
	
					float x = idx[last4WeekHrs - 1 - i];
					sum = sum + x;
					if (count == 24 && j < 21) {
						avg = sum / (float)24;
						last4weekAQIReadings[j] = avg;
						if (avg < 100) {
							tempG ++;
						}
						tempC ++;
						j++;
						sum = 0;
						avg = 0;
						count = 0;
					} else if (j >= 21){
						for (int m = 0; m <last7dayAQIReadings.length; m++) {
							last4weekAQIReadings[j] = last7dayAQIReadings[m];
							if (last7dayAQIReadings[m] < 100) {
								tempG ++;
							}
							tempC ++;
							j++;
						}
						break;
					}
					count ++;
				}
				percentage(tempG, tempC);
			}
		}
	}
	
	private static void percentage(int goodAir, int totalAir) {
		int percent = 0;
		if (totalAir > 0) {
			percent = (goodAir * 100) / totalAir;
		}
		outdoorAQIPercentageList.add(percent);
	}
}
