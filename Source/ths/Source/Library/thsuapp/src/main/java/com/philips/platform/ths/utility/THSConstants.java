/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

public class THSConstants {
    public static final String THS_APPLICATION_ID = "ths";
    public static final String THS_SDK_SERVICE_ID = "Telehealth.sdkURL";
    public static final String PTH_BASE_SERVICE_URL = "https://ec2-54-172-152-160.compute-1.amazonaws.com";
    public static final String PTH_API_KEY = "3c0f99bf";
    public static final String WELCOME_FRAGMENT = "Welcome Fragment";
    public static final String PRACTICE_FRAGMENT = "Practice Fragment";
    public static final String THS_PROVIDER_INFO = "ths_providerInfo";
    public static final String THS_PROVIDER_ENTITY = "ths_provider_entity";
    public static final String THS_PRACTICE_INFO = "ths_practice_info";
    public static final String THS_PROVIDER = "ths_provider";

    public static final String THS_DATE = "ths_date";
    public static final String THS_ON_DEMAND = "ths_on_demand";
    public static final int MEDICATION_ON_ACTIVITY_RESULT = 5555;
    public static final String THS_AVAILABLE_PROVIDER_LIST = "ths_available_provider_List";
    public static final String THS_IS_DETAILS = "ths_is_details";

    public static final String DATE_FORMATTER = "EEEE, MMM d, yyyy";
    public static final String DATE_TIME_FORMATTER = "EEEE, MMM d, yyyy h:mm a";
    public static final String TIME_FORMATTER = "h:mm a";


    public static final String WEB_AVAILABLE = "WEB_AVAILABLE";
    public static final String PROVIDER_OFFLINE = "OFFLINE";
    public static final String PROVIDER_WEB_BUSY = "WEB_BUSY";
    public static final int REQUEST_VIDEO_VISIT = 6060;

    public static final int PROVIDER_SEARCH_CONSTANT = 1001;
    public static final int PHARMACY_SEARCH_CONSTANT = 1003;
    public static final String SEARCH_CONSTANT_STRING = "SEARCH_CONSTANT_STRING";
    public static final int MEDICATION_SEARCH_CONSTANT = 1002;
    public static final String THS_VISIT_REPORT_DETAIL = "THS_VISIT_REPORT_DETAIL";
    public static final String THS_VISIT_REPORT = "THS_VISIT_REPORT";

    public static final String IS_INSURANCE_AVAILABLE_KEY = "IS_INSURANCE_AVAILABLE_KEY";
    public static final String IS_PAYMENT_METHOD_AVAILABLE_KEY = "IS_PAYMENT_METHOD_AVAILABLE_KEY";
    public static final String IS_LAUNCHED_FROM_COST_SUMMARY = "IS_LAUNCHED_FROM_COST_SUMMARY";
    public static final String CVV_HELP_TEXT = "CVV_HELP_TEXT";
    public static final String VISIT_UNSUCCESSFUL = "VISIT_UNSUCCESSFUL";

    public static final int SET_REMINDER_DIALOG_ON_ACTIVITY_RESULT = 7070;
    public static final String THS_SET_REMINDER_EXTRA_KEY = "THS_SET_REMINDER_EXTRA_KEY";

    public static final String THS_NO_REMINDER_STRING = "no reminder";
    public static final String THS_15_MINS_REMINDER = "15 minutes";
    public static final String THS_ONE_HOUR_REMINDER = "one Hour";
    public static final String THS_FOUR_HOURS_REMINDER = "4 hours";
    public static final String THS_EIGHT_HOURS_REMINDER = "8 hours";
    public static final String THS_ONE_DAY_REMINDER = "one day";
    public static final String THS_ONE_WEEK_REMINDER = "one week";
    public static final String THS_VISIT_ARGUMENT_KEY = "ths_visit_argument_key";

    public static final String THS_SAVE_UPLOAD_IMAGE_KEY = "ths_upload_image_key";
    public static final String THS_SEARCH_PHARMACY_SELECTED = "selectedPharmacy";
    public static final String THS_MAP_FRAGMENT_TAG = "mapFragment";
    public static final String THS_PROVIDER_DETAIL_ALERT = "thsProviderDetailAlert";
    public static final String THS_COST_SUMMARY_ALERT = "thsCostSummaryAlert";
    public static final String THS_LAUNCH_INPUT = "THS_LAUNCH_INPUT";
    public static final int THS_PRACTICES = 0;
    public static final int THS_SCHEDULED_VISITS = 1;
    public static final int THS_VISITS_HISTORY = 2;

    // start of tagging constants
    public static final String THS_SEND_DATA = "sendData";
    public static final String THS_SPECIAL_EVENT = "specialEvents";
    public static final String THS_IN_APP_NOTIFICATION = "inAppNotification";
    public static final String THS_IN_APP_NOTIFICATION_RESPONSE = "inAppNotificationResponse";
    public static final String THS_USER_ERROR = "UserError";
    public static final String THS_SERVER_ERROR = "TechnicalError";

    public static final String THS_START = "start";


    // start of tags
    public static final String THS_INIT_PAGE = "TH_00_00a Welcome";
    public static final String THS_WELCOME = "TH_00_00 Welcome";
    public static final String THS_ADD_DETAILS = "TH_00_01 Add details";
    public static final String THS_CONFIRM_T_AND_C = "TH_00_02 Confirm T&C";
    public static final String THS_TERMS_AND_CONDITION = "TH_00_06 Terms & Conditions";
    public static final String THS_SELECT_PATIENT = "TH_00_03 Select patient";

    public static final String THS_PRACTICE_PAGE = "TH_01 _01 Home with practices";
    public static final String THS_SCHEDULE_VISITS = "TH_00_04 Scheduled visits";
    public static final String THS_VISIT_HISTORY_LIST = "TH_00_05 Visit history";
    public static final String THS_VISIT_HISTORY = "TH_10_02b Wrap up - history";
    public static final String THS_HIPPA = "TH_10_04 HIPAA";
    public static final String THS_HOW_IT_WORKS = "TH_00_07 How it works";
    public static final String THS_HOW_IT_WORKS_DETAIL = "TH_00_08 How it works - detail";
    public static final String THS_NOTICE = "TH_00_09 TeleHealth Notice";


    public static final String THS_PROVIDER_LIST = "TH_01_03a Start DOD";
    public static final String THS_PROVIDER_SEARCH_PAGE = "TH_01_03b Search Provider";
    public static final String THS_PROVIDER_DETAIL_PAGE = "TH_02_01a Provider detail";
    public static final String THS_WELCOME_BACK = "TH_03 _08 Appointment start";

    public static final String THS_VISIT_STEPS = "TH_01 _05 Steps";
    public static final String THS_SYMPTOMS_PAGE = "TH_04_03 Symptoms";
    public static final String THS_ADD_VITALS_PAGE = "TH_04_04 add vitals";
    public static final String THS_MEDICATION_PAGE = "TH_04_05 Medication";
    public static final String THS_MEDICATION_SEARCH_PAGE = "TH_04_05a Search Medication";
    public static final String THS_CONDITION_PAGE = "TH_04_06 Previous conditions";
    public static final String THS_FOLLOW_UP_PAGE = "TH_04_07 add phone & confirm";
    public static final String THS_NOPP_PAGE = "TH_04_08 NOPP";

    public static final String THS_SEARCH_PHARMACY = "TH_05_01 Search pharmacy"; // first screen for new user where search box is in body
    public static final String THS_PHARMACY_MAP = "TH_05_02 Pharmacy map";
    public static final String THS_PHARMACY_SEARCH = "TH_05_02a Search pharmacy";

    public static final String THS_PHARMACY_SUMMARY = "TH_05_07 Pharmacy summary";
    public static final String THS_SHIPPING_ADDRESS = "TH_05_06 Shipping address";


    public static final String THS_INSURANCE_CONFIRM = "TH_06_01 Insurance";
    public static final String THS_INSURANCE_DETAIL = "TH_06_02a Insurance details";
    public static final String THS_COST_SUMMARY = "TH_06_05 Cost summary";
    public static final String THS_PAYMENT_METHOD = "TH_07_01 Payment method";
    public static final String THS_BILLING_ADDRESS = "TH_07_03 Billing address";

    public static final String THS_WAITING = "TH_08_03d Waiting";
    public static final String THS_VIDEO_CALL = "TH_09_01 Video consult";
    public static final String THS_VISIT_SUMMARY = "TH_10_02 Wrap up summary";

    public static final String THS_SCHEDULE_APPOINTMENT_PICK_PROVIDER = "TH_03_02 Available provider";
    public static final String THS_SCHEDULE_APPOINTMENT_PICK_TIME = "TH_03_03a Pick a time";
    public static final String THS_SCHEDULE_APPOINTMENT_CONFIRMED = "TH_03_06b Appointment confirmed";

    public static final String ON_BOARDING_START = "TH_99_00 Onboarding";
    public static final String ON_BOARDING_PAGE_1 = "TH_99_01 Onboarding";
    public static final String ON_BOARDING_PAGE_2 = "TH_99_02 Onboarding";
    public static final String ON_BOARDING_PAGE_3 = "TH_99_03 Onboarding";
    public static final String ON_BOARDING_PAGE_4 = "TH_99_04 Onboarding";
    public static final String THS_CUSTOMER_SUPPORT = "TH_00_10 Costumer Support";

    // end of tags


    public static final String THS_VIDEO_CALL_ENDS = "videoVisitCompleted";


    public static final String THS_COST_SUMMARY_CREATE_VISIT_ERROR = "Cost_summary_create_visit_error";
    public static final String THS_EARLY_FOR_APPOINTMENT = "THS_EARLY_FOR_APPOINTMENT";
    public static final String THS_COST_SUMMARY_COUPON_CODE_ERROR = "Cost_summary_coupon_code_error";

    public static final String THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE = "createPaymentRequest.creditCardYear";
    public static final String THS_USER_NOT_LOGGED_IN = "THS_USER_NOT_LOGGED_IN";
    // end of tagging constants

    public static final String THS_CONSUMER = "THS_CONSUMER";

    public static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    public static final String KEY_COLOR_RANGE = "KEY_COLOR_RANGE";
    public static final String KEY_CONTENT_COLOR = "KEY_CONTENT_COLOR";
    public static final String KEY_NAVIGATION_COLOR = "KEY_NAVIGATION_COLOR";
    public static final String KEY_ACCENT_RANGE = "KEY_ACCENT_RANGE";
    public static final String KEY_ORIENTATION = "KEY_ORIENTATION";


    public static final String THS_GENERIC_USER_ERROR = "Oops! Something went wrong, please check your details and try again";
    public static final String THS_GENERIC_SERVER_ERROR = "Oops! Something went wrong, please try after sometime";

    //Faqs constants
    public static final String THS_FAQ_SERVICE_ID = "Telehealth.FAQURL";
    public static final String THS_FAQ_HEADER = "THS_FAQ_HEADER";
    public static final String THS_FAQ_ITEM = "THS_FAQ_ITEM";

    //Terms & Conditions
    public static final String THS_TERMS_AND_CONDITIONS = "TeleHealth.TermsConditionURL_small";
    public static final String THS_HIPPA_NOTICE = "TeleHealth.hipaaURL_small";


    //start inAppNotification tags
    public static final String THS_ANALYTICS_TOO_EARLY_FOR_VISIT = "You are early for visit";
    public static final String THS_ANALYTICS_NO_PROVIDER_FOR_PRACTICE = "No provider available for practice";
    public static final String THS_ANALYTICS_OPEN_SETTINGS_FOR_CAMERA = "Open setting";
    public static final String THS_ANALYTICS_CANCEL_APPOINTMENT = "Do you really want to cancel your appointment?";
    public static final String THS_ANALYTICS_CVV_EXPLAINATION = "What's this?";
    public static final String THS_ANALYTICS_HIPAA_PRIVACY_NOTICE = "Privacy notice";
    public static final String THS_ANALYTICS_CANCEL_VISIT = "Do you really want to cancel your visit?";
    public static final String THS_ANALYTICS_INSURANCE_VALIDATION = "Insurance validation failed";


    public static final String THS_ANALYTICS_RESPONSE_SETTINGS = "Setting";
    public static final String THS_ANALYTICS_RESPONSE_CANCEL = "Cancel";
    public static final String THS_ANALYTICS_RESPONSE_DONT_CANCEL_APPOINTMENT = "No";
    public static final String THS_ANALYTICS_RESPONSE_CANCEL_APPOINTMENT = "Yes, cancel my appointment";
    public static final String THS_ANALYTICS_RESPONSE_OK = "Ok";
    public static final String THS_ANALYTICS_RESPONSE_CANCEL_VISIT = "Yes, cancel my visit";
    public static final String THS_ANALYTICS_RESPONSE_DONT_CANCEL_VISIT = "No, continue waiting";
    //end inAppNotification tags

    //service discovery error
    public static final String THS_SERVICE_DISCOVERY_CANNOT_FIND_LOCALE = "ServiceDiscovery cannot find the locale";
}
