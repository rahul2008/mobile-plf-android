package com.philips.cl.di.digitalcare.analytics;

/**
 * 
 * AnalyticsConstants is holding constant members used for TAGGING, Adobe
 * Analytics.
 * 
 * = author: ritesh.jha= philips.com = since: Mar 26, 2015
 */
public class AnalyticsConstants {

	/* PAGE CONSTANTS */
	public static final String PAGE_HOME = "digitalcare:home";
	public static final String PAGE_PRODCUT_DETAILS = "digitalcare:productdetails";
	public static final String PAGE_CONTACT_US = "digitalcare:contactus";
	public static final String PAGE_RATE_THIS_APP = "digitalcare:ratethisapp";
	public static final String PAGE_PRODUCT_REGISTRATION = "digitalcare:productregistration";
	public static final String PAGE_FIND_PHILIPS_NEAR_YOU = "digitalcare:findphilips";

	public static final String PAGE_CONTACTUS_TWITTER = "digitalcare:contactus:twitter";
	public static final String PAGE_CONTACTUS_FACEBOOK = "digitalcare:contactus:facebook";
	public static final String PAGE_CONTACTUS_CHATNOW = "digitalcare:contactus:chatnow";
	public static final String PAGE_CONTACTUS_LIVECHAT = "digitalcare:contactus:livechat";
	public static final String PAGE_CONTACTUS_EMAIL = "digitalcare:contactus:email";

	/* ACTION KEY CONSTANTS */
	/***************** Page Context Data start **************/
	public static final String ACTION_KEY_APPNAME = "app.name";
	public static final String ACTION_KEY_VERSION = "app.version";
	public static final String ACTION_KEY_OS = "app.os";
	public static final String ACTION_KEY_LANGUAGE = "locale.language";
	public static final String ACTION_KEY_CURRENCY = "locale.currency";
	public static final String ACTION_KEY_COUNTRY = "locale.country";
	public static final String ACTION_KEY_TIME_STAMP = "timestamp";
	public static final String ACTION_KEY_APP_ID = "appsId";
	/***************** Page Context Data End **************/

	public static final String ACTION_KEY_USER_ERROR = "userError";
	public static final String ACTION_KEY_SET_ERROR = "setError";
	public static final String ACTION_KEY_TECHNICAL_ERROR = "technicalError";
	public static final String ACTION_KEY_EXIT_LINK = "exit link";
	public static final String ACTION_KEY_RECEIPT_PHOTO = "receiptPhoto";
	public static final String ACTION_KEY_APP_STATUS = "appStatus";
	public static final String ACTION_KEY_SET_APP_STATUS = "setAppStatus";
	public static final String ACTION_KEY_SOCIAL_SHARE = "socialShare";
	public static final String ACTION_KEY_SOCIAL_TYPE = "socialType";
	public static final String ACTION_KEY_SERVICE_CHANNEL = "serviceChannel";
	public static final String ACTION_KEY_SERVICE_REQUEST = "serviceRequest";
	public static final String ACTION_KEY_PHOTO = "photo"; // TODO: Has to be
															// confirmed.

	/* ACTION KEY FOR MAP */
	public static final String MAP_KEY_EXIT_LINK = "exitLinkName";

	/* ACTION VALUE CONSTANTS */
	public static final String ACTION_VALUE_APPNAME = "DigitalCare";
	public static final String ACTION_VALUE_ANDROID = "Android ";
	public static final String ACTION_VALUE_FACEBOOK = "Facebook";
	public static final String ACTION_VALUE_BACKGROUND = "Background";
	public static final String ACTION_VALUE_FOREGROUND = "Foreground";
	public static final String ACTION_VALUE_PHOTO_VALUE = "productimage"; // TODO:
																			// Has
																			// to
																			// be
																			// confirmed.
	/* Error text */
	public static final String TECHNICAL_ERROR_NETWORK_CONNECITON = "Error connecting to network";
	public static final String TECHNICAL_ERROR_RESPONSE = "Error response from server";
	public static final String TECHNICAL_ERROR_LOADING = "Error in loading";
}
