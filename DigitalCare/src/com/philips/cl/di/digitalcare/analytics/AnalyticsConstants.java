package com.philips.cl.di.digitalcare.analytics;

public interface AnalyticsConstants {

	/* General */
	String TAG = "DigitalCare:Analytics";

	/* PAGE CONSTANTS */
	String PAGE_HOME = "digitalcare:home";
	String PAGE_PRODCUT_DETAILS = "digitalcare:productdetails";
	String PAGE_FAQ = "digitalcare:faq";
	String PAGE_CONTACT_US = "digitalcare:contactus";
	String PAGE_FIND_PHILIPS_NEAR_BY = "digitalcare:findphilips";
	String PAGE_RATE_THIS_APP = "digitalcare:ratethisapp";
	String PAGE_PRODUCT_REGISTRATION = "digitalcare:productregistration";

	String PAGE_CONTACTUS_TWITTER = "digitalcare:contactus:twitter";
	String PAGE_CONTACTUS_FACEBOOK = "digitalcare:contactus:facebook";
	String PAGE_CONTACTUS_CHATNOW = "digitalcare:contactus:chatnow";
	String PAGE_CONTACTUS_LIVECHAT = "digitalcare:contactus:livechat";
	String PAGE_CONTACTUS_EMAIL = "digitalcare:contactus:email";

	/* ACTION KEY CONSTANTS */
	/***************** Page Context Data start **************/
	String ACTION_KEY_APPNAME = "app.name";
	String ACTION_KEY_VERSION = "app.version";
	String ACTION_KEY_OS = "app.os";
	String ACTION_KEY_LANGUAGE = "locale.language";
	String ACTION_KEY_CURRENCY = "locale.currency";
	String ACTION_KEY_COUNTRY = "locale.country";
	String ACTION_KEY_TIME_STAMP = "timestamp";
	String ACTION_KEY_APP_ID = "appsId";
	/***************** Page Context Data End **************/

	String ACTION_KEY_PAGE_EVENT = "pageEvents";
	String ACTION_KEY_OPTION_DETAILS = "optionDetail";
	String ACTION_KEY_VIDEO_NAME = "videoName";
	String ACTION_KEY_CONTROL_CONNECTION_TYPE = "controlConnectionType";
	String ACTION_KEY_MACHINE_ID = "machineId";
	String ACTION_KEY_USER_ERROR = "userError";
	String ACTION_KEY_SET_ERROR = "setError";
	String ACTION_KEY_TECHNICAL_ERROR = "technicalError";
	String ACTION_KEY_EXIT_LINK = "exit link";
	String ACTION_KEY_FILE_NAME = "fileName";
	String ACTION_KEY_DOWNLOAD = "download";
	String ACTION_KEY_PRODUCT_MODEL = "productModel";
	String ACTION_KEY_DATE_OF_PURCHASE = "dateOfPurchase";
	String ACTION_KEY_RECEIPT_PHOTO = "receiptPhoto";
	String ACTION_KEY_REGISTRATION_CHANNEL = "registrationChennel";
	String ACTION_KEY_SET_USER_REGISTRATION = "setUserRegistration";
	String ACTION_KEY_LOGIN_CHANNEL = "loginChannel";
	String ACTION_KEY_SET_LOGIN = "setLogin";
	String ACTION_KEY_APP_STATUS = "appStatus";
	String ACTION_KEY_SET_APP_STATUS = "setAppStatus";
	String ACTION_KEY_SOCIAL_SHARE = "socialShare";
	String ACTION_KEY_SOCIAL_TYPE = "socialType";
	String ACTION_KEY_SERVICE_CHANNEL = "serviceChannel";
	String ACTION_KEY_SERVICE_REQUEST = "serviceRequest";
	String ACTION_KEY_SERVICE_SATISFACTION = "serviceSatisfaction";
	String ACTION_KEY_VIDEO_START = "videoStart";
	String ACTION_KEY_VIDEO_END = "videoEnd";

	/* ACTION KEY FOR MAP */
	String MAP_KEY_EXIT_LINK = "exitLinkName";

	/* ACTION VALUE CONSTANTS */
	String ACTION_VALUE_APPNAME = "DigitalCare";
	String ACTION_VALUE_ANDROID = "Android ";
	String ACTION_VALUE_DEFAULT_CURRENCY = "EUR";
	String ACTION_VALUE_START_USER_REG = "startUserRegistration";
	String ACTION_VALUE_SUCCESS_USER_REG = "successUserRegistration";
	String ACTION_VALUE_FACEBOOK = "Facebook";
	String ACTION_VALUE_SUCCESS_LOGIN = "successLogin";
	String ACTION_VALUE_START_CONNECTION = "startConnection";
	String ACTION_VALUE_SUCCESS_CONNECTION = "successConnection";
	String ACTION_VALUE_BACKGROUND = "Background";
	String ACTION_VALUE_FAQ = "faq";
	String ACTION_VALUE_NOT_USEFUL = "not useful";
}
