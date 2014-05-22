package com.philips.cl.di.dev.pa.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;

import com.philips.cl.di.dev.pa.activity.MainActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConstants.
 */
public class AppConstants {
	
	public static final String BOOT_STRAP_ID = "000000fff0000002";
	public static final String BOOT_STRAP_KEY = "5b6c580330e12b28f179815a3808b475";
	public static final String BOOT_STRAP_PRODUCT_ID = "AIR_KPSPROV";
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

	// Network related data
	/** The Constant UPDATE_INTERVAL. */
	public static final long UPDATE_INTERVAL = 3 * 1000;
	
	public static final long UPDATE_INTERVAL_CPP = 15  * 1000 ;

	/** The Constant URL. */

	/** The Constant URL_CURRENT. */	
	public static final String WEATHER_SERVICE_URL = "http://ixuanwu.com.cn/app/weather.php?q=%s" ;
	public static final String OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=%s" ;
	public static final String OUTDOOR_CITIES_URL = "http://ixuanwu.com.cn/app/citys.php";
	
	/** The Constant defaultIPAddress. */
	public static final String DEFAULT_PURIFIERID = "none";

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
	public static final String LOG_DATETIME = "date" ;
	public static final String CITY_ID = "cityId" ;
	
	/** The Constant DB_VERS. */
	public static final int DB_VERS = 2;
	
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
	public static final String AIRPUR_LASTKNOWN_NETWORK = "lastknown_network";
	public static final String IS_PAIRED="is_paired";
	public static final String LAST_PAIRED = "last_paired";

	public static final String INDOOR_AQI = "aqi" ;

	public static final float SWIPE_THRESHOLD = 100;

	public static final float SWIPE_VELOCITY_THRESHOLD = 100;

	public static final String DATABASE = "purair.db";
	
	public static final String SHANGHAI_OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=shanghai" ;
	
	public static final String DI_COMM_REQUEST = "DICOMM-REQUEST" ;
	
	
	public static final String ACT_NOW = "Act Now" ;
	public static final String ACT_SOON = "Act Soon" ;
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
	public static final int COLOR_GOOD = Color.rgb(43, 166, 81);
	public static final int COLOR_FAIR = Color.rgb(225, 138, 53);
	public static final int COLOR_BAD = Color.rgb(209, 37, 49);
	
	//Power mode
	public static final String POWER_ON = "1";
	
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
	public static final String PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER = "Patchy light rain in area with thunder";
	public static final String MODERATE_OR_HEAVY_RAIN_SHOWER = "Moderate or heavy rain shower";
	public static final String TORRENTIAL_RAIN_SHOWER = "Torrential rain shower";
	public static final String HEAVY_RAIN = "Heavy rain";
	public static final String HEAVY_RAIN_AT_TIMES = "Heavy rain at times";
	public static final String MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER = "Moderate or heavy	rain in area with thunder";
	public static final String MIST = "Mist";
	public static final String CLOUDY = "Cloudy";
	public static final String PARTLY_CLOUDY = "Partly Cloudy";
	public static final String CLEAR_SKIES = "Clear Skies";
	public static final String SNOW = "Snow";
	public static final String CLEAR = "Clear";
	
	public static final String UNICODE_DEGREE = "\u2103" ;
	
	
	public static final String PRODUCT = "product";
	public static final String STATUS = "status" ;
	
	// Constants related to ICP client
	public static final String PUT_PROPS = "PUTPROPS" ;
	public static final String GET_PROPS = "GETPROPS" ;

	public static final String GETPROPS_ACTION = "{\"product\":\"1\",\"port\":\"air\"}" ;
	public static final String MODEL_NAME = "AirPurifier" ;
	
	public static final String CLIENT_ID_RDCP = "Clientid=%s;datatype=airquality.1;" ;
	
	// Constants related to Subscription
	public static final String URL_BASEALLPORTS = "http://%s/di/v1/products/%s/%s";
	public enum Port {
		AIR("air",1), WIFI("wifi",0), WIFIUI("wifiui",1), FIRMWARE("firmware",0), DEVICE("device",1), PAIRING("pairing",0), 
		SECURITY("security",0), LOG("log",0), SCHEDULES("schedules",0);
		
		public final String urlPart;
		public final int port ;
		Port (String urlPart,int port) {
			this.urlPart = urlPart;
			this.port = port ;
		}
	};
	
	public static final String SUBSCRIBE = "SUBSCRIBE";
	public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
	public static final String EVENTSUBSCRIBER_KEY = "subscriber";
		
	//Child lock and indicator light status.
	public static final int ON = 1;
	
	public static final String INVALID_WIFI_SETTINGS = "invalid Wi-Fi settings" ;
	public static final String APP_TYPE="AC4373APP";
	
	/** Activity request codes*/
	public static final int EWS_REQUEST_CODE = 101;
	public static final int FIRMWARE_REQUEST_CODE = 102;
	
	public static final List<String> PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Response", "Change"));
	public static final String DI_COMM_RELATIONSHIP="DI-COMM";
	public static final String NOTIFY_RELATIONSHIP="NOTIFY";
	public static final List<String> NOTIFY_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Request"));
	public static final List<String> PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Push"));
	
	/** Activity intent key strings*/
	//Firmware upgrade intent keys
	public static final String UPGRADE_VERSION = "upgradeVersion";
	public static final String CURRENT_VERSION = "currentVersion";
	public static final String PURIFIER_NAME = "purifierName";
	
	public static final String REQUEST_METHOD_POST = "POST" ;
	public static final String REQUEST_METHOD_DELETE = "DELETE" ;
	
	/** Notification */
	public static final String SERVICE_TAG="3pns";
	public static final String PROTOCOL="push";
	public static final String PROVIDER="gcma";
}
