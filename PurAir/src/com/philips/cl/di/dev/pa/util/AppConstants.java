package com.philips.cl.di.dev.pa.util;

import android.graphics.Color;

public class AppConstants {
	
	
	//Filter status bar constants
	public static final float MINIMUNFILTER = 0;
	public static final float MAXIMUMFILTER = 1000;
	
	public static final int MAXWIDTH = 516;
	
	// Colors for each category
	public static final int COLOR_NA= Color.rgb(192, 192, 192);
	public static final int COLOR_VGOOD = Color.rgb(0, 169, 231);
	public static final int COLOR_GOOD = Color.rgb(129, 107, 172);
	public static final int COLOR_FAIR = Color.rgb(222, 74, 138);
	public static final int COLOR_BAD = Color.rgb(255, 0, 0);
	
	//Fling detection constants
	public static final float SWIPE_THRESHOLD = 100;

	public static final float SWIPE_VELOCITY_THRESHOLD = 100;
	
	//Fan speeds
	public static final String FAN_SPEED_SILENT = "s";
	public static final String FAN_SPEED_TURBO = "t";
	public static final String FAN_SPEED_AUTO = "0";
	public static final String FAN_SPEED_ONE = "1";
	public static final String FAN_SPEED_TWO = "2";
	public static final String FAN_SPEED_THREE = "3";

}
