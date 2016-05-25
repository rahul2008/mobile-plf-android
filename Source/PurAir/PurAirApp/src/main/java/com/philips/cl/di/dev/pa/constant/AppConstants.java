package com.philips.cl.di.dev.pa.constant;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Environment;

import com.philips.cl.di.dev.pa.activity.MainActivity;

@SuppressLint("SimpleDateFormat")
public class AppConstants {
	
	/** CONSTANTS FOR THE ICPCLIENT */
	public static final String BOOT_STRAP_PRODUCT_ID = "AIR_KPSPROV";
	public static final String CMA_APP_ID_1 = "MDI4M2" ;
	
	public static final String APP_ID = "1_com.philips.cl.di.air";
	public static final String APP_TYPE="AC4373APP";
	public static final String COMPONENT_ID = "AC4373-AND";
	
	public static final String DEVICE_PORT_URL="https://www.bat.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx";
	
	/** URLS FOR WEATHER DATA. */	
	public static final String WEATHER_SERVICE_URL = "http://ixuanwu.com.cn/app/weather.php?q=%s" ;
	public static final String OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=%s" ;
	public static final String OUTDOOR_CITIES_URL = "http://ixuanwu.com.cn/app/citys.php";
	public static final String SHANGHAI_OUTDOOR_AQI_URL = "http://ixuanwu.com.cn/app/city-hourly-gov.php?key=shanghai" ;
	public static final String URL_WINNING_LIST = "http://philipsair.sinaapp.com/award/";

	/** CONSTANTS NECESSARY FOR THE DATABASE */
	public static final String PURIFIERDB_NAME = "smart_air.db";
	public static final int PURIFIERDB_VERSION = 12;
	public static final String KEY_ID = "_id";

	// City detail table
	public static final String TABLE_CITYDETAILS = "CityDetails";
	public static final String KEY_AREA_ID = "AREA_ID";
	public static final String KEY_CITY = "CITY";
	public static final String KEY_CITY_CN = "CITY_CN";
	public static final String KEY_CITY_TW = "CITY_TW";
	public static final String KEY_DISTRICT = "DISTRICT";
	public static final String KEY_PROVINCE = "PROVINCE";
	public static final String KEY_COUNTRY = "COUNTRY";
	public static final String KEY_STATION_TYPE = "STATION_TYPE";
	public static final String KEY_SHORTLIST = "SHORTLIST";
	public static final String KEY_LONGITUDE = "LONGITUDE";
	public static final String KEY_LATITUDE = "LATITUDE";
	public static final String KEY_NOTIFY_PERMISSION = "NOTIFY_PERMISSION";
	
	//City data provider table
	public static final String TABLE_USER_SELECTED_CITY = "UserSelectedCity";
	public static final String KEY_DATA_PROVIDER = "DATA_PROVIDER";// 0 CMA and 1 US Embassy
 	
	public static final String SQL_SELECTION_GET_SHORTLIST_ITEMS = AppConstants.KEY_SHORTLIST + " = '1' ";
	public static final String SQL_SELECTION_GET_SHORTLIST_ITEMS_EXCEPT_SELECTED = AppConstants.KEY_SHORTLIST + " != '1' ";
	
	// AirPurifier event table
	public static final String TABLE_AIRPURIFIER_EVENT = "AirPurifierEvent";
	public static final String KEY_INDOOR_AQI = "aqi" ;
	public static final String KEY_LAST_SYNC_DATETIME = "lastsyncdatetime" ;
	
	@Deprecated
	public static final String TABLE_AIRPUR_INFO = "device_info"; // AirPurifier table (< DB version 11)
	public static final String TABLE_AIRPUR_DEVICE = "AirPurifierDevice"; // AirPurifierDevice table (=> DB version 11)
	public static final String KEY_AIRPUR_USN = "usn";
	public static final String KEY_AIRPUR_CPP_ID = "cppid";
	public static final String KEY_AIRPUR_BOOT_ID = "bootid";
	public static final String KEY_AIRPUR_KEY = "airpur_key";
	public static final String KEY_AIRPUR_DEVICE_NAME = "dev_name";
	public static final String KEY_AIRPUR_LASTKNOWN_NETWORK = "lastknown_network";
	public static final String KEY_AIRPUR_IS_PAIRED="is_paired";
	public static final String KEY_AIRPUR_LAST_PAIRED = "last_paired";
	
	public static final String KEY_DATE = "DATE";
	public static final String KEY_AQI = "AQI";
	public static final String KEY_TIME = "TIME";

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
	public static final String POWER_STATUS_C = "C";
	public static final String POWER_STATUS_E = "E";
	
	//Fan speeds
	public static final String FAN_SPEED_SILENT = "s";
	public static final String FAN_SPEED_TURBO = "t";
	public static final String FAN_SPEED_AUTO = "a";
	public static final String FAN_SPEED_ONE = "1";
	public static final String FAN_SPEED_TWO = "2";
	public static final String FAN_SPEED_THREE = "3";
	public static final String FAN_SPEED_OFF = "0";
	
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
	
	public static final int LOCAL_SUBSCRIPTIONTIME = 300; // IN SEC
	public static final int CPP_SUBSCRIPTIONTIME = 5; // IN MIN
	
	/** CONSTANTS FOR NOTIFICATIONS */
	public static final String NOTIFICATION_PROVIDER_GOOGLE="gcma";
	public static final String NOTIFICATION_PROVIDER_JPUSH="jpush";
	
	public static final String NOTIFICATION_SENDER_ID = "589734100886";
	
	public static final String NOTIFICATION_PREFERENCE_FILE_NAME = "GCMRegistrion";
	public static final String PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP = "is_registrationkey_sendtocpp";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_APP_LOCALE = "appLocale";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_NOTIFICATION_PROVIDER = "provider";

	/** OTHER CONSTANTS */
	public static final String PURIFIER_BUY_LINK = "http://shop.philips.com.cn/product/JY0002/detail.htm?origin=15_global_en_purifier-app_purifier-app";
	
	public static final String MODEL_NAME = "AirPurifier" ;

	public static final String INVALID_WIFI_SETTINGS = "invalid Wi-Fi settings" ;

	public static final float MAXWIDTH = MainActivity.getScreenWidth() * 0.655f;

	public static final String GETPROPS_ACTION = "{\"product\":\"1\",\"port\":\"air\"}" ;
	
	public static final String NO_PURIFIER_FLOW = "no_purifier_flow";
	public static final int MAX_PURIFIER_LIMIT = 20;
	
	/** DEMO MODE */
	public final static String DEMO_MODE_PREF = "demo_mode_pref";
	public final static String DEMO_MODE_ENABLE_KEY ="demo_mode_enable_key";
	public final static String DEMO_MODE_PURIFIER_KEY ="demo_mode_purifier_key";

	public static final String EMPTY_STRING = "" ;

	public static final String HOCKEY_APPID = "8b51a3b720bce7db6e6f9a6a9528b8b8";
	
	public static final int INDEX_0 = 0;
	public static final int INDEX_1 = 1;
	public static final int INDEX_2 = 2;
	public static final int INDEX_3 = 3;
	
	public static final String SHOW_HEADING = "show_heading";
	public static final String NO_OF_VISIT_PREF = "AIRPUR_PREFS";
	public static final String NO_OF_VISIT_PREF_KEY = "NoOfVisit";
	public static final String OUTDOOR_LOCATION_PREFS = "outdoor_location_prefs";
	public static final String START_FLOW_PREF = "StartFlowPreferences";
	public static final String START_FLOW_PREF_KEY = "FirstUse";
	public static final String CURR_PURAIR_PREF = "StartFlowPreferences";
	public static final String CURR_PURAIR_PREF_KEY = "eui64";
	public static final String FIRMWARE_VERSION = "firmwareVersion";
	public static final String FIRMWARE_VERSION_KEY = "firmwareversionKey";
	public static final String EXTRA_AREA_ID = "area_id";
	public static final String OUTDOOR_CITY_NAME = "city_name" ;
	public static final String OUTDOOR_AQI = "outdoor_aqi" ;
	public static final String OUTDOOR_DATAPROVIDER = "outdoor_dataprovider";
	public static final String ALLOW_GPS = "allow_gps";
	public static final String GPS_PREFERENCE_FILE_NAME = "GPS_status";
	public static final String DIABLED_DIALOG_SHOWN = "isGPSDisabledDialogShown";
	public static final String ENABLED_DIALOG_SHOWN = "isGPSEnabledDialogShown";
	
	public static final String SIMPLIFIED_CHINESE_LANGUAGE_CODE="ZH-HANS";
	public static final String TRADITIONAL_CHINESE_LANGUAGE_CODE="ZH-HANT";
	public static final int MAX_RETRY = 3;
	
	public static final String APP_UPDATE_DIRECTORY = "SmartAir";
	public static final String APP_UPDATE_FILE_NAME = "PurAir.apk";
	
	public static final String ACTIVITY="Activity";
	public static final int PRIVACY_POLICY_SCREEN=1;
	public static final int EULA_SCREEN=2;
	public static final int TERMS_AND_CONDITIONS_SCREEN=3;
	
	public static final int NUM_OFF_POINTS = 24;
	public static final String SHANGHAI_AREA_ID = "101020100";
	public static final String BEIJING_AREA_ID = "101010100";
	
	public static final String WECHAT_PAKAGE = "com.tencent.mm";
	public static final String WEIBO_PAKAGE = "com.sina.weibo";
	public static final String EMAIL_PAKAGE = "com.android.email";
	public static final String MMS_PAKAGE = "com.android.mms";
	public static final String FACEBOOK_PAKAGE = "com.facebook.android";
	public static final String TWITTER_PAKAGE = "com.twitter.android";
	
	public static final SimpleDateFormat TIME_FORMAT_HH_MM = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String CACHEDIR_IMG = Environment
			.getExternalStorageDirectory().getPath() + "/philips/air/imgs/";
	
	public static final String SHARE_DASHBOARD = "PhilipsAir.png";
	
	public static final  String TMP_OUTPUT_JPG = Environment
			.getExternalStorageDirectory().getPath() + "/philips/air/imgs/tmp_output.jpg";
	public static final String APP_DOWNLOAD_LINK = "www.philips-smartairpurifier.com";
	public static final String APP_DOWNLOAD_LINK_WITH_HTTP = "http://" + APP_DOWNLOAD_LINK;
	
	public static final String TMP_OUTPUT_CORP_JPG = Environment
			.getExternalStorageDirectory().getPath()
			+ "/philips/air/imgs/tmp_output_corp.jpg";
	public static final String URL_PRODUCT_SHARE = "http://philipsair.sinaapp.com/";
	
	public static final String APP_KEY      = "1253329329";
	public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
	
	public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
}
