package com.philips.cl.di.dev.pa.constants;

import android.graphics.Color;

import com.philips.cl.di.dev.pa.pureairui.MainActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConstants.
 */
public class AppConstants {
	
	public static final int SUCCESS = 0 ;
	
	public static final String EMPTY_STRING = "" ;
	// Icon for left menu
	/** The icon home. */
	public static final String ICON_HOME = "icon_home";

	/** The icon mycity. */
	public static final String ICON_MYCITY = "icon_mycity";

	/** The icon cloud. */
	public static final String ICON_CLOUD = "icon_cloud";

	/** The icon reg. */
	public static final String ICON_REG = "icon_registration";

	/** The icon help. */
	public static final String ICON_HELP = "icon_help";

	/** The icon setting. */
	public static final String ICON_SETTING = "icon_setting";

	// Label for left menu
	/** The label home. */
	public static final String LABEL_HOME = "Home";

	/** The label mycity. */
	public static final String LABEL_MYCITY = "My Cities";

	/** The label cloud. */
	public static final String LABEL_CLOUD = "About Air Quality";

	/** The label reg. */
	public static final String LABEL_REG = "Product Registration";

	/** The label help. */
	public static final String LABEL_HELP = "Help & Documentation";

	/** The label setting. */
	public static final String LABEL_SETTING = "Settings";

	// Font file -
	/** The font. */
	public static final String FONT = "fonts/gillsans.ttf";

	// Drawable
	/** The drawable. */
	public static final String DRAWABLE = "drawable";

	/** The scrolltoviewid. */
	public static final int SCROLLTOVIEWID = 1;

	/** The scaleleftmenu. */
	public static final float SCALELEFTMENU = .75f;

	// Network related data
	/** The Constant UPDATE_INTERVAL. */
	public static final long UPDATE_INTERVAL = 3 * 1000;
	
	public static final long UPDATE_INTERVAL_CPP = 15  * 1000 ;
	public static final int DCS_TIMEOUT = 45 * 1000 ;

	/** The Constant URL. */

	/** The Constant URL_CURRENT. */
	public static final String URL_CURRENT = "http://%s/di/v1/products/1/air";
	
	public static final String WEATHER_SERVICE_URL = "http://ixuanwu.com.cn/app/weather.php?q=%s" ;
	
	public static final String OUTDOOR_LOCATIONS_URL = "http://ixuanwu.com.cn/app/citys.php";

	
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
	public static final String DB_NAME = "smart_air.db";

	public static final String AQI_TABLE = "AQITable" ;
	public static final String ID = "id" ;
	public static final String OUTDOOR_AQI = "aqi" ;
	public static final String LOG_DATETIME = "date" ;
	public static final String CITY_ID = "cityId" ;
	
	/** The Constant DB_VERS. */
	public static final int DB_VERS = 1;
	
	/**
	 * Device information table name
	 */
	public static final String AIRPUR_INFO_TABLE = "device_info";
	/**
	 * Table fields
	 */
	public static final String AIRPUR_USN = "usn";
	public static final String AIRPUR_CPP_ID = "cppid";
	public static final String AIRPUR_BOOT_ID = "bootid";
	public static final String AIRPUR_KEY = "airpur_key";
	public static final String AIRPUR_DEVICE_NAME = "dev_name";

	public static final String sCityNameQuery = "Select distinct "
			+ AppConstants.KEY_CITY + " from " + AppConstants.TABLENAME;
	
	public static final String airPurifierEventQuery = "Select * from " + AppConstants.TABLE_AIRPURIFIER_EVENT ;
	
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
	
	public static final String CITIES_URL = "http://ixuanwu.com.cn/app/citys.php" ;
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
	public static final String GOOD = "Good";
	
	public static final int PRE_FILTER_MAX_VALUE = 112;
	public static final int MULTI_CARE_FILTER_MAX_VALUE = 960;
	public static final int ACTIVE_CARBON_FILTER_MAX_VALUE = 2880;
	public static final int HEPA_FILTER_MAX_VALUE = 2880;
	
	public static final float MAXWIDTH = MainActivity.getScreenWidth() * 0.655f;
	
	// Colors for each category
	public static final int COLOR_NA= Color.rgb(192, 192, 192);
	public static final int COLOR_VGOOD = Color.rgb(0, 169, 231); // Not used
	public static final int COLOR_GOOD = Color.rgb(43, 166, 81);
	public static final int COLOR_FAIR = Color.rgb(225, 138, 53);
	public static final int COLOR_BAD = Color.rgb(209, 37, 49);
	
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
	
	//Weather constants
	public static final String SUNNY = "Sunny";
	public static final String LIGHT_RAIN_SHOWER = "Light rain shower";
	public static final String LIGHT_DRIZZLE = "Light drizzle";
	public static final String PATCHY_RAIN_NEARBY = "Patchy rain nearby";
	public static final String PATCHY_LIGHT_DRIZZLE = "Patchy light drizzle";
	public static final String PATCHY_LIGHT_RAIN = "Patchy light rain";
	public static final String PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER = "Patchy light rain in area with thunder";
	public static final String MODERATE_OR_HEAVY_RAIN_SHOWER = "Moderate or heavy rain shower";
	public static final String TORRENTIAL_RAIN_SHOWER = "Torrential rain shower";
	public static final String HEAVY_RAIN = "Heavy rain";
	public static final String HEAVY_RAIN_AT_TIMES = "Heavy rain at times";
	public static final String MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER = "Moderate or heavy	rain in area with thunder";
	public static final String MIST = "Mist";
	public static final String CLOUDY = "Cloudy";
	public static final String PARTLY_CLOUDY = "Partly Cloudy";
	public static final String DAY_TIME = "Day time";
	public static final String CLEAR_SKIES = "Clear Skies";
	public static final String SNOW = "Snow";
	public static final String CLEAR = "Clear";
	
	
	public static final String PRODUCT = "product";
	
	public static final int CONNECTED = 1 ;
	public static final int NOT_CONNECTED = 0 ;
	public static final int CONNECTED_VIA_PHILIPS = 2 ;
	
	// Constants related to ICP client
	public static final String DICOMM_REQUEST = "DICOMM-REQUEST" ;
	public static final String PUT_PROPS = "PUTPROPS" ;
	public static final String GET_PROPS = "GETPROPS" ;
		
	public static final String URL_SECURITY = "http://%s/di/v1/products/0/security" ;

	public static final String GETPROPS_ACTION = "{\"product\":\"1\",\"port\":\"air\"}" ;
	public static final String MODEL_NAME = "AirPurifier" ;
	
	public static final String CLIENT_ID_RDCP = "Clientid=%s;datatype=airquality.1;" ;
		
	//Child lock and indicator light status.
	public static final int ON = 1;
	public static final int OFF = 0;
	
	public static final String URL_PORT = "http://%s/di/v1/products/1/";
	public static final String URL_FIRMWARE_PORT = "http://%s/di/v1/products/0/firmware";
	
	public static String DEVICEID = "dev001" ;
	
	public static final String INVALID_WIFI_SETTINGS = "invalid Wi-Fi settings" ;
}
