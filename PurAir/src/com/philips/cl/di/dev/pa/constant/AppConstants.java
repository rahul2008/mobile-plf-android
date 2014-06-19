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
	
	/** CONSTANTS FOR THE ICPCLIENT */

	public static final String BOOT_STRAP_PRODUCT_ID = "AIR_KPSPROV";

	
	/** URLS FOR WEATHER DATA. */	
	public static final String WEATHER_SERVICE_URL = "http://ixuanwu.com.cn/app/weather.php?q=%s" ;
	public static final String OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=%s" ;
	public static final String OUTDOOR_CITIES_URL = "http://ixuanwu.com.cn/app/citys.php";
	public static final String SHANGHAI_OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=shanghai" ;


	/** CONSTANTS NECESSARY FOR THE DATABASE */
	public static final String PURIFIERDB_NAME = "smart_air.db";
	public static final int PURIFIERDB_VERSION = 2;
	public static final String KEY_ID = "id";

	// City table
	public static final String TABLE_CITYDETAILS = "CityDetails";
	public static final String KEY_CITY = "CITY";
	public static final String KEY_PROVINCE = "PROVINCE";
	public static final String KEY_DATE = "DATE";
	public static final String KEY_AQI = "AQI";
	public static final String KEY_TIME = "TIME";
	
	// AirPurifier event table
	public static final String TABLE_AIRPURIFIER_EVENT = "AirPurifierEvent";
	public static final String KEY_INDOOR_AQI = "aqi" ;
	public static final String KEY_LAST_SYNC_DATETIME = "lastsyncdatetime" ;
	
	// AirPurifier table
	public static final String TABLE_AIRPUR_INFO = "device_info";
	public static final String KEY_AIRPUR_USN = "usn";
	public static final String KEY_AIRPUR_CPP_ID = "cppid";
	public static final String KEY_AIRPUR_BOOT_ID = "bootid";
	public static final String KEY_AIRPUR_KEY = "airpur_key";
	public static final String KEY_AIRPUR_DEVICE_NAME = "dev_name";
	public static final String KEY_AIRPUR_LASTKNOWN_NETWORK = "lastknown_network";
	public static final String KEY_AIRPUR_IS_PAIRED="is_paired";
	public static final String KEY_AIRPUR_LAST_PAIRED = "last_paired";

	
	/** CONSTANTS FOR THE FILTERS */
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
	public static final int RUNNING_HRS=8; //it is assumed that the purifier is used 8 hours per day
	
	// Colors for each category
	public static final int COLOR_GOOD = Color.rgb(43, 166, 81);
	public static final int COLOR_FAIR = Color.rgb(225, 138, 53);
	public static final int COLOR_BAD = Color.rgb(209, 37, 49);
	
	
	/** CONSTANTS FOR EVENTS */
	//Power mode
	public static final String POWER_ON = "1";
	
	//Fan speeds
	public static final String FAN_SPEED_SILENT = "s";
	public static final String FAN_SPEED_TURBO = "t";
	public static final String FAN_SPEED_AUTO = "a";
	public static final String FAN_SPEED_ONE = "1";
	public static final String FAN_SPEED_TWO = "2";
	public static final String FAN_SPEED_THREE = "3";
	
	//Child lock and indicator light status.
	public static final int ON = 1;
	
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
	public static final String ADD_PROPS = "ADDPROPS" ;
	public static final String DEL_PROPS = "DELPROPS" ;
	public static final String DISCOVER = "DISCOVER" ;
	
	
	public static final String CLIENT_ID_RDCP = "Clientid=%s;datatype=airquality.1;" ;
	
	// Constants related to Subscription

	/** CONSTANTS RELATED TO SUBSCRIPTION */

	public static final String URL_BASEALLPORTS = "http://%s/di/v1/products/%s/%s";
	public static final String URL_GET_SCHEDULES = "http://%s/di/v1/products/0/schedules/%s" ;
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

	public static final String REQUEST_METHOD_POST = "POST" ;
	public static final String REQUEST_METHOD_DELETE = "DELETE" ;
	public static final String REQUEST_METHOD_PUT = "PUT" ;
	public static final String REQUEST_METHOD_GET = "GET" ;
	
	/** CONSTANTS FOR NOTIFICATIONS */
	public static final String NOTIFICATION_SERVICE_TAG="3pns";
	public static final String NOTIFICATION_PROTOCOL="push";
	public static final String NOTIFICATION_PROVIDER="gcma";
	public static final String NOTIFICATION_SENDER_ID = "589734100886";
	
	public static final String NOTIFICATION_PREFERENCE_FILE_NAME = "GCMRegistrion";
	public static final String PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP = "is_registrationkey_sendtocpp";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_REG_ID = "registration_id";
	
	
	/** PAIRING CONSTANTS */
	public static final int PAIRING_RELATIONSHIPDURATION_SEC = 480;  // 8 hours
	public static final int PAIRING_REQUESTTTL_MIN = 5; // ingored by cpp, because purifier already defined it
	public static final String PAIRING_REFERENCETYPE = "AC4373GENDEV";
	public static final String PAIRING_REFERENCEPROVIDER = "cpp";
	public static final String PAIRING_DI_COMM_RELATIONSHIP="DI-COMM";
	public static final String PAIRING_NOTIFY_RELATIONSHIP="NOTIFY";
	
	public static final List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Response", "Change"));
	public static final List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Push"));
	
	
	/** OTHER CONSTANTS */
	public static final String APP_TYPE="AC4373APP";
	public static final String MODEL_NAME = "AirPurifier" ;
	public static final String DI_COMM_REQUEST = "DICOMM-REQUEST" ;
	public static final String DISCOVERY_REQUEST = "DCS-REQUEST" ;

	public static final String INVALID_WIFI_SETTINGS = "invalid Wi-Fi settings" ;

	public static final float MAXWIDTH = MainActivity.getScreenWidth() * 0.655f;

	public static final String GETPROPS_ACTION = "{\"product\":\"1\",\"port\":\"air\"}" ;
	
	/** DEMO MODE */
	public final static String DEMO_MODE_PREF = "demo_mode_pref";
	public final static String DEMO_MODE_ENABLE_KEY ="demo_mode_enable_key";

	public static final String EMPTY_STRING = "" ;

	public static final String HOCKEY_APPID = "8b51a3b720bce7db6e6f9a6a9528b8b8";
	
}
