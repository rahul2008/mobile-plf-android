package com.philips.cl.di.dev.pa.util;

import com.philips.cl.di.dev.pa.pureairui.MainActivity;

import android.graphics.Color;

public class AppConstants {
	
	
	//Filter status bar constants
	public static final float MINIMUNFILTER = 0;
	public static final float MAXIMUMFILTER = 1000;
	
	public static final int PRE_FILTER_MAX_VALUE = 112;
	public static final int MULTI_CARE_FILTER_MAX_VALUE = 960;
	public static final int ACTIVE_CARBON_FILTER_MAX_VALUE = 2880;
	public static final int HEPA_FILTER_MAX_VALUE = 2880;
	
	//TODO : ?Move to strings.xml?
	public static final String ACT_NOW = "Act Now" ;
	public static final String ACT_SOON = "Act Soon" ;
	public static final String NORMAL_OPERATION = "Normal Operation" ;
	public static final String FILTER_LOCK = "Filter Lock" ;
	public static final String CLEAN_SOON = "Clean Soon" ;
	public static final String CLEAN_NOW = "Clean Now" ;
	
	public static final float MAXWIDTH = MainActivity.getScreenWidth() * 0.655f;
	
	// Colors for each category
	public static final int COLOR_NA= Color.rgb(192, 192, 192);
	public static final int COLOR_VGOOD = Color.rgb(0, 169, 231); // Not used
	public static final int COLOR_GOOD = Color.rgb(43, 166, 81);
	public static final int COLOR_FAIR = Color.rgb(225, 138, 53);
	public static final int COLOR_BAD = Color.rgb(209, 37, 49);
	
	//Fling detection constants
	public static final float SWIPE_THRESHOLD = 100;
	public static final float SWIPE_VELOCITY_THRESHOLD = 100;
	
	//Power mode
	public static final String POWER_ON = "1";
	public static final String POWER_OFF = "0";
	
	//Fan speeds
	public static final String FAN_SPEED_SILENT = "s";
	public static final String FAN_SPEED_TURBO = "t";
	public static final String FAN_SPEED_AUTO = "a";
	public static final String FAN_SPEED_ONE = "1";
	public static final String FAN_SPEED_TWO = "2";
	public static final String FAN_SPEED_THREE = "3";
	
	//Child lock and indicator light status.
	public static final int ON = 1;
	public static final int OFF = 0;

}
