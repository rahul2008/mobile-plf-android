package com.philips.cl.di.dev.pa.constants;

import com.philips.cl.di.dev.pa.pureairui.MainActivity;

import android.graphics.Color;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConstants.
 */
public class AppConstants {
	public static final String EMPTY_STRING = "" ;
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
	public static String LABEL_HOME = "Home";

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
	public static final long UPDATE_INTERVAL = 3 * 1000;
	
	public static final long UPDATE_INTERVAL_CPP = 30  * 1000 ;

	/** The Constant URL. */

	/** The Constant URL_CURRENT. */
	public static final String URL_CURRENT = "http://%s/di/v1/products/1/air";
	
	public static final String WEATHER_SERVICE_URL = "http://ixuanwu.com.cn/app/weather.php?q=%s" ;

	
	/** The Constant defaultIPAddress. */
	public static final String defaultIPAddress = "192.168.10.198";

	/** The Constant MESSAGE_INCORRECT_IP. */
	public static final String MESSAGE_INCORRECT_IP = "Incorrect IP Address";

	/** The Constant MESSAGE_OK. */
	public static final String MESSAGE_OK = "OK";

	
	/** The Constant HOME. */
	public static final int HOME = 1;

	/** The Constant MYCITIES. */
	public static final int MYCITIES = 2;

	/** The Constant ABOUTAQI. */
	public static final int ABOUTAQI = 3;

	/** The Constant PRODUCTREG. */
	public static final int PRODUCTREG = 4;

	/** The Constant HELP. */
	public static final int HELP = 5;

	/** The Constant SETTINGS. */
	public static final int SETTINGS = 6;

	/** Server Request Type **/
	public static final int GET_SENSOR_DATA_REQUEST_TYPE = 1;

	/** The Constant TABLENAME. */
	public static final String TABLENAME = "CityDetails";
	
	public static final String TABLE_AIRPURIFIER_EVENT = "AirPurifierEvent" ;
	
	public static final String LAST_SYNC_DATETIME = "lastsyncdatetime" ;

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

	public static final String AQI_TABLE = "AQITable" ;
	public static final String ID = "id" ;
	public static final String OUTDOOR_AQI = "aqi" ;
	public static final String LOG_DATETIME = "date" ;
	public static final String CITY_ID = "cityId" ;
	
	/** The Constant DB_VERS. */
	public static final int DB_VERS = 1;

	public static String sCityNameQuery = "Select distinct "
			+ AppConstants.KEY_CITY + " from " + AppConstants.TABLENAME;
	
	public static String airPurifierEventQuery = "Select * from " + AppConstants.TABLE_AIRPURIFIER_EVENT ;
	
	public static String selectLatestOutdoorAQI = "Select * from " + AppConstants.AQI_TABLE + " where aqi > 0 and cityID = %s order by " + LOG_DATETIME + " DESC"  ;
	
	public static String selectOutdoorAQIOnLogDateTime = "Select * from " + AppConstants.AQI_TABLE + " where " + AppConstants.LOG_DATETIME + "= '%s' and " + AppConstants.CITY_ID + "= %s" ;
	
	
	public static final String INDOOR_AQI = "aqi" ;

	public static final float SWIPE_THRESHOLD = 100;

	public static final float SWIPE_VELOCITY_THRESHOLD = 100;

	public static final long DURATION = 1000;
	
	public static final long FADEDURATION = 600;
	
	public static final long FADEDELAY = 100;

	public static final float MINIMUNFILTER = 0;
	public static final float MAXIMUMFILTER = 1000;

	
	public static final float  DAYWIDTH = 680;

	public static final int OUTDOOR_AQI_UPDATE_DURATION = 60 * 60 * 1000 ;

	public static final String WARNING_VGOOD = "warning_vgood";
	public static final String WARNING_GOOD = "warning_good";
	public static final String WARNING_FAIR = "warning_fair";
	public static final String WARNING_BAD = "warning_bad";
	
	public static final float HOURS = 24;
	public static final  float MAX_AQI = 500;

	public static final String DATABASE = "purair.db";
	
	public static final String QUERY_AQIVALUES = "Select aqi from aqitable where cityid= %s  and date like '"+"%s"+" %' ";

	public static final String CITYNAME = "city_name";

	public static final String LAST_UPDATED_TIME = "last_updated_time";

	public static final String LAST_UPDATED_DAY = "last_updated_day";

	
	public static final String SHANGHAI_OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=shanghai" ;
	
	public static final String INDOOR = "indoor";

	public static final String OUTDOOR = "outdoor";
	
	
	public static final String EVENT_DATA = "" ;
	
	
	public static final String DI_COMM_REQUEST = "DICOMM-REQUEST" ;
	public static final String DI_ACTION_PUTPROPS = "PUTPROPS" ;
	public static final String DI_ACTION_GETPROPS = "GETPROPS" ;
	
	
	public static final String ACT_NOW = "Act Now" ;
	public static final String ACT_SOON = "Act Soon" ;
	public static final String NORMAL_OPERATION = "Normal Operation" ;
	public static final String FILTER_LOCK = "Filter Lock" ;
	public static final String CLEAN_SOON = "Clean Soon" ;
	public static final String CLEAN_NOW = "Clean Now" ;
	
	
	
	public static final float PRE_FILTER_MAX_VALUE = 112;
	public static final float MULTI_CARE_FILTER_MAX_VALUE = 960;
	public static final float ACTIVE_CARBON_FILTER_MAX_VALUE = 2880;
	public static final float HEPA_FILTER_MAX_VALUE = 2880;
	
	public static final float MAXWIDTH = MainActivity.getScreenWidth() * 0.655f;
	
	// Colors for each category
	public static final int COLOR_NA= Color.rgb(192, 192, 192);
	public static final int COLOR_VGOOD = Color.rgb(0, 169, 231); // Not used
	public static final int COLOR_GOOD = Color.rgb(43, 166, 81);
	public static final int COLOR_FAIR = Color.rgb(225, 138, 53);
	public static final int COLOR_BAD = Color.rgb(209, 37, 49);
	
	
	//Fan speeds
	public static final String FAN_SPEED_SILENT = "s";
	public static final String FAN_SPEED_TURBO = "t";
	public static final String FAN_SPEED_AUTO = "a";
	public static final String FAN_SPEED_ONE = "1";
	public static final String FAN_SPEED_TWO = "2";
	public static final String FAN_SPEED_THREE = "3";
}
