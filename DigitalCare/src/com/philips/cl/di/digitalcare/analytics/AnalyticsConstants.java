package com.philips.cl.di.digitalcare.analytics;

/**
 * 
 * AnalyticsConstants is holding constant members used for TAGGING, Adobe
 * Analytics.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Mar 26, 2015
 */
public class AnalyticsConstants {

	/* PAGE CONSTANTS */
	public static String PAGE_HOME = "digitalcare:home";
	public static String PAGE_PRODCUT_DETAILS = "digitalcare:productdetails";
	public static String PAGE_CONTACT_US = "digitalcare:contactus";
	public static String PAGE_RATE_THIS_APP = "digitalcare:ratethisapp";
	public static String PAGE_PRODUCT_REGISTRATION = "digitalcare:productregistration";

	public static String PAGE_CONTACTUS_TWITTER = "digitalcare:contactus:twitter";
	public static String PAGE_CONTACTUS_FACEBOOK = "digitalcare:contactus:facebook";
	public static String PAGE_CONTACTUS_CHATNOW = "digitalcare:contactus:chatnow";
	public static String PAGE_CONTACTUS_LIVECHAT = "digitalcare:contactus:livechat";
	public static String PAGE_CONTACTUS_EMAIL = "digitalcare:contactus:email";

	/* ACTION KEY CONSTANTS */
	/***************** Page Context Data start **************/
	public static String ACTION_KEY_APPNAME = "app.name";
	public static String ACTION_KEY_VERSION = "app.version";
	public static String ACTION_KEY_OS = "app.os";
	public static String ACTION_KEY_LANGUAGE = "locale.language";
	public static String ACTION_KEY_CURRENCY = "locale.currency";
	public static String ACTION_KEY_COUNTRY = "locale.country";
	public static String ACTION_KEY_TIME_STAMP = "timestamp";
	public static String ACTION_KEY_APP_ID = "appsId";
	/***************** Page Context Data End **************/

	public static String ACTION_KEY_USER_ERROR = "userError";
	public static String ACTION_KEY_SET_ERROR = "setError";
	public static String ACTION_KEY_TECHNICAL_ERROR = "technicalError";
	public static String ACTION_KEY_EXIT_LINK = "exit link";
	public static String ACTION_KEY_RECEIPT_PHOTO = "receiptPhoto";
	public static String ACTION_KEY_APP_STATUS = "appStatus";
	public static String ACTION_KEY_SET_APP_STATUS = "setAppStatus";
	public static String ACTION_KEY_SOCIAL_SHARE = "socialShare";
	public static String ACTION_KEY_SOCIAL_TYPE = "socialType";
	public static String ACTION_KEY_SERVICE_CHANNEL = "serviceChannel";
	public static String ACTION_KEY_SERVICE_REQUEST = "serviceRequest";
	public static String ACTION_KEY_PHOTO = "photo"; // TODO: Has to be
														// confirmed.

	/* ACTION KEY FOR MAP */
	public static String MAP_KEY_EXIT_LINK = "exitLinkName";

	/* ACTION VALUE CONSTANTS */
	public static String ACTION_VALUE_APPNAME = "DigitalCare";
	public static String ACTION_VALUE_ANDROID = "Android ";
	public static String ACTION_VALUE_FACEBOOK = "Facebook";
	public static String ACTION_VALUE_BACKGROUND = "Background";
	public static String ACTION_VALUE_FOREGROUND = "Foreground";
	public static String ACTION_VALUE_PHOTO_VALUE = "product image"; // TODO:
																		// Has
																		// to be
																		// confirmed.
}
