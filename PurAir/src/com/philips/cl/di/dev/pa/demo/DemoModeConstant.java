package com.philips.cl.di.dev.pa.demo;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;

public class DemoModeConstant {
	public static final String CMA_APP_ID_3 = "UwN2U5" ;
	public static final String CMA_PRIVATE_KEY_3 = "iYXBpX";
	public static final int DEMO_MODE_STEP_INTRO = 1;
	public static final int DEMO_MODE_STEP_SWITCHON = 20;
	public static final int DEMO_MODE_STEP_ONE = 21;
	public static final int DEMO_MODE_STEP_FINAL = 3;
	public static final int DEMO_MODE_STEP_SUPPORT = 4;
	public static final int DEMO_MODE_ERROR_NOT_IN_PHILIPS_SETUP = 5;
	public static final int DEMO_MODE_ERROR_DATA_RECIEVED_FAILED = 6;
	public static final int DEMO_MODE_TASK_DEVICE_GET = 7;
	public static final int DEMO_MODE_TASK_WIFI_GET = 8;
	public static final String DEMO = PurAirApplication.getAppContext().getString(R.string.demo);
	public static final int REQUEST_CODE = 2;
	public static final String EXTRA_WIFI_HELP = "wifi_help";
	public static final String EXTRA_SET_APMODE = "apmode";
}
