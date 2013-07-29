package com.philips.cl.di.dev.pa.constants;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConstants.
 */
public class AppConstants {
	// Icon for left menu
	/** The icon home. */
	public static String ICON_HOME = "icon_home";

	/** The icon mycity. */
	public static String ICON_MYCITY = "icon_mycity";

	/** The icon cloud. */
	public static String ICON_CLOUD = "icon_cloud";

	/** The icon reg. */
	public static String ICON_REG = "icon_registration";

	/** The icon help. */
	public static String ICON_HELP = "icon_help";

	/** The icon setting. */
	public static String ICON_SETTING = "icon_setting";

	// Label for left menu
	/** The label home. */
	public static String LABEL_HOME = "HOME";

	/** The label mycity. */
	public static String LABEL_MYCITY = "My Cities";

	/** The label cloud. */
	public static String LABEL_CLOUD = "About Air Quality";

	/** The label reg. */
	public static String LABEL_REG = "Product Registration";

	/** The label help. */
	public static String LABEL_HELP = "Help & Documentation";

	/** The label setting. */
	public static String LABEL_SETTING = "Settings";

	// Font file -
	/** The font. */
	public static String FONT = "fonts/gillsans.ttf";

	// Drawable
	/** The drawable. */
	public static String DRAWABLE = "drawable";

	/** The scrolltoviewid. */
	public static int SCROLLTOVIEWID = 1;

	/** The scaleleftmenu. */
	public static float SCALELEFTMENU = .75f;

	// Network related data
	/** The Constant UPDATE_INTERVAL. */
	public static final long UPDATE_INTERVAL = 10 * 1000;

	/** The Constant URL. */
	public static final String URL = "http://%s/di/v1/status";

	/** The Constant URL_CURRENT. */
	public static final String URL_CURRENT = "http://%s/di/v1/status/current";

	/** The Constant URL_HISTORY. */
	public static final String URL_HISTORY = "http://%s/di/v1/activity";

	/** The Constant URL_FILTER_STATUS. */
	public static final String URL_FILTER_STATUS = "http://%s/di/v1/device/current";

	/** The Constant URL_FILTER. */
	public static final String URL_FILTER = "http://%s/di/v1/device";

	/** The Constant defaultIPAddress. */
	public static final String defaultIPAddress = "192.168.10.198";

	/** The Constant MESSAGE_INCORRECT_IP. */
	public static final String MESSAGE_INCORRECT_IP = "Incorrect IP Address";

	/** The Constant MESSAGE_OK. */
	public static final String MESSAGE_OK = "OK";

	/** The Constant MESSAGE_ENTER_IP_ADDRESS. */
	public static final String MESSAGE_ENTER_IP_ADDRESS = "Enter IP Address :";

	/** The Constant HOME. */
	public static final int HOME = 0;

	/** The Constant MYCITIES. */
	public static final int MYCITIES = 1;

	/** The Constant ABOUTAQI. */
	public static final int ABOUTAQI = 2;

	/** The Constant PRODUCTREG. */
	public static final int PRODUCTREG = 3;

	/** The Constant HELP. */
	public static final int HELP = 4;

	/** The Constant SETTINGS. */
	public static final int SETTINGS = 5;
	
	/** Server Request Type **/
	public static final int GET_SENSOR_DATA_REQUEST_TYPE = 1 ;
	

	/** The Constant TABLENAME. */
	public static final String TABLENAME = "CityDetails";

	// Contacts Table Columns names
	/** The Constant KEY_ID. */
	public static final String KEY_ID = "id";

	/** The Constant KEY_CITY. */
	public static final String KEY_CITY = "CITY";

	/** The Constant KEY_PROVINCE. */
	public static final String KEY_PROVINCE = "PROVINCE";

	/** The Constant KEY_DATE. */
	public static final String KEY_DATE = "DATE";

	/** The Constant KEY_AQI. */
	public static final String KEY_AQI = "AQI";

	/** The Constant KEY_TIME. */
	public static final String KEY_TIME = "TIME";

	/** The Constant DB_NAME. */
	public static final String DB_NAME = "City.db";

	/** The Constant DB_VERS. */
	public static final int DB_VERS = 1;

}
