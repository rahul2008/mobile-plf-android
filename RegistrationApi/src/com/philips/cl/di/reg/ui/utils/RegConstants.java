/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : Saeco Avanti
----------------------------------------------------------------------------*/

package com.philips.cl.di.reg.ui.utils;

public class RegConstants {

	public static final String SERIALIZED_RECIPE_CONTAINER_FILE = "recipe_container.obj";

	public static final String INTENT_BT_STATE_CHANGE = "android.bluetooth.adapter.action.STATE_CHANGED";

	public static final String PREFS_NAME = "SAECO_PREFERENCE";

	public static final int DRAWABLE_FILENAME_INDEX = 14;

	public static final String CLASSIC_COFFEE_NAME_PRE_STRING = "classic_";

	public static final String WHITE_SPACE = " ";

	public static final String RESOURCE_RECIPE_EXTRA = "RESOURCE_FROM";

	public static final String RESOURCE_COFFEE_DETAIL_OPENED_FROM_EXTRA = "RESOURCE_COFFEE_DETAIL_OPENED_FROM_EXTRA";

	public static final String RESOURCE_SHOWPROGRESS_EXTRA = "RESOURCE_TAG_EXTRA";

	public static final String HEART_SYMBOL = "\u2661\u00A0";

	public static final int VERTICAL = 1;

	public static final String WATER = "Water";

	/** milk recipe identification */
	public static final String MILK = "Milk";

	/** milk recipe identification */
	public static final String COFFEEMILK = "CoffeeMilk";

	/** Dialog Ids */
	public static final int BREW_DIALOG = 1;

	/** Dialog Id for favorite */
	public static final int FAVORITE_DIALOG = 2;

	/** fw version key */
	public static final String FW_VERSION = "FW_VERSION";

	/** machine type key */
	public static final String MACHINE_TYPE = "MACHINE_TYPE";

	/** Asset path */
	public static final String ASSET_PATH = "file:///android_asset/";

	/** Term & Condition */
	public static final String BTS_TERMS = "term";

	/** Bluetooth Support button click */
	public static final String BTS_SUPPORT_BTN = "support";

	/** Bluetooth Register button click */
	public static final String BTS_REGISTER_BTN = "register";

	/** Bluetooth MyCoffee Button Click */
	public static final String BTS_MY_COFFEE_BTN = "mycoffee";

	/** To check about setup done */
	public static final String SETUP_DONE = "SETUP_DONE";

	/** To check about setup done */
	public static final String WATER_FILTER_ENABLE = "WATER_FILTER_ENABLE";

	/** To get the Device Name for which setup done once */
	public static final String DEVICE_NAME = "DEVICE_NAME";

	/** To get the known preferred Device Name for which setup is not done at all */
	public static final String KNOWN_PREF_DEVICE_NAME = "KNOWN_PREF_DEVICE_NAME";

	/** To get the Device Pin for which setup done once */
	public static final String DEVICE_PIN = "DEVICE_PIN";

	/** Key for Descaling required cup left */
	public static final String DESCALE_CUPS_LEFT = "DESCALE_CUPS_LEFT";

	/** Key for Descale Percentage */
	public static final String DESCALE_PERCENTAGE = "DESCALE_PERCENTAGE";

	/** Key for water filter change in cup lefts */
	public static final String WATER_FILTER_CUPS_LEFT = "WATER_FILTER_CUPS_LEFT";

	/** Key for water filter change in percentage */
	public static final String WATER_FILTER_PERCENTAGE = "WATER_FILTER_PERCENTAGE";

	/** Key for last updated connection with device */
	public static final String LAST_UPDATED_RECEIVED = "LAST_UPDATED_RECEIVED";

	/** Key for last updated connection with device */
	public static final String LAST_UPDATED_CONNECTION = "LAST_UPDATED_CONNECTION";

	/** Help and manual for no additional items required */
	public static final String HM_NONE = "none";

	/** Help and manual page type */
	public static final String HM_TYPE = "HELP_AND_MANUAL";

	/** Help and manual phase */
	public static final String HM_STEP_INFO = "HM_HELP_INFO";

	/** Help and manual step progress */
	public static final String HM_HELP_STEP = "HM_HELP_STEP";

	/** Help and manual step position extra */
	public static final String HM_HELP_STEP_POSITION = "HM_HELP_STEP_POSITION";

	/** Help and manual step progress */
	public static final String HM_HELP_PROGRESS_PERCENTAGE = "HM_HELP_PROGRESS_PERCENTAGE";

	/** Is device is connected to Internet raise this event */
	public static final String IS_ONLINE = "IS_ONLINE";

	/** Is device is connected to Internet raise this event */
	public static final String JANRAIN_INIT_SUCCESS = "JANRAIN_SUCCESS";

	public static final String JANRAIN_INIT_FAILURE = "JANRAIN_FAILURE";

	public static final String MAINTENANCE_ICON_TAP = "MAINTENANCE_TAP";

	/** Help and manual sub title */
	public static final String HM_SUB_TITLE = "null";

	/** For navigation to help and manual form right off canvas */
	public static final String HELP_AND_MAINTANANCE = "HELP_AND_MAINTANANCE";

	/** Help and manual daily */
	public static final String HM_DAILY = "Daily";

	/** Help and manual weekly */
	public static final String HM_WEEKLY = "Weekly";

	/** Help and manual montly */
	public static final String HM_MONTHLY = "Monthly";

	/** Developer key for you tube */
	public static final String YOUTUBE_DEVELOPER_KEY = "AIzaSyB5624Ksk4KwEiRstNqATStAUjeBu2O7qA";

	/** Make view 50% transparent */
	public static final int YT_CONTROL_ALPHA_HALF_TRANS_VALUE = 128;

	/** MAke view 100 opac */
	public static final int YT_CONTROL_ALPHA_OPC_VALUE = 255;

	public static final String RIGHTOFFCANVAS_WILLOPEN = "RIGHTOFFCANVAS_WILLOPEN";

	public static final String RIGHTOFFCANVAS_HASCLOSED = "RIGHTOFFCANVAS_HASCLOSED";

	/** En locale */
	public static final String DEFAULT_LOCALE = "en_";

	/** is error or warning message displayed */
	public static final String ERROR_NOT_DISPLAYED = "ERROR_NOT_DISPLAYED";

	public static final int SLIDINGDRAWER_ANIMATION_DURATION = 700;

	public static final int SLIDINGDRAWER_ANIMATION_DELAY_AFTERSLIDEIN = 600;

	public static final int LIST_UPDATE_DELAY = 280;

	public static final int ONE_SEC = 1000;

	public static final int FRAGMENT_REGISTRATION_DELAY = 600;

	public static final String HM_STEP_CHANGED = "HM_STEP_CHANGED";

	public static final int MAINTENANCE_STEP_CHANGE = 2000;

	public static final int MAINTENANCE_ERROR = 2001;

	public static final int BREW_ERROR = 2002;

	public static final int BREWING_DID_NOT_FINISH = 2003;

	public static final int BREW_FINISHED = 2004;

	public static final String DETAILS = "DETAILS";

	public static final String MOST_BREWED_RECIPE_PRODUCT_KEY = "MOST_BREWED_RECIPE_PRODUCT_KEY";

	public static final String HOCKEY_APPID = "a11392432ac05d2a482283283441c9e9";

	public static final String LOCALE = "locale";

	public static final String MODEL_NUMBER = "modelno";

	public static final String SERIAL_NUMBER = "serialno";

	public static final String CONTRACT_NO = "contract_no";

	public static final String PURCHASE_DATE = "purchaseDate";

	public static final String REGISTER_PREREGISTRATION_RECORD = "preregistrationRecord";

	public static final String REGISTER_SOCIAL_REGISTRATION_TOKEN = "socialRegistrationToken";

	public static final String REGISTER_SOCIAL_PROVIDER = "REGISTER_SOCIAL_PROVIDER";

	public static final String REGISTER_MERGE_TOKEN = "mergetoken";

	public static final String REGISTER_GIVEN_NAME = "givenName";

	public static final String REGISTER_DISPLAY_NAME = "displayName";

	public static final String REGISTER_FAMILY_NAME = "familyName";

	public static final String REGISTER_MY_PHILIPS_EXTRA = "myPhilipsExtra";

	public static final String EVAL_CLIENT_Id = "6v3yzffu6uxq4k9ctcw4jtd498k8zmtz";

	public static final String PROD_CLIENT_ID = "mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3";

	public static final String MICROSITE_ID = "81376";

	public static final String REGISTRATION_USE_EVAL = "REGISTRATION_USE_EVAL";

	public static final String REGISTRATION_USE_PROD = "REGISTRATION_USE_PRODUCTION";

	public static final String JANRAIN_INTIALIZATION_STATUS = "janrainIntializationStatus";

	public static final String LOGOUT_ACTION = "LOGOUT";

	public static final String SOCIAL_LOGIN = "SOCIAL_LOGIN";

	public static final String EXTRA_ANIMATIONTYPE = "com.philips.cl.di.saecoavanti.extraanimationtype";

	public static final String CUSTOM = "custom";

	public static final String CANCEL_SETUP = "CANCEL_SETUP";
}
