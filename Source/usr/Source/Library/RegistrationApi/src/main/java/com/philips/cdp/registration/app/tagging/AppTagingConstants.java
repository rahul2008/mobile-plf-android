/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.app.tagging;

public class AppTagingConstants {

	public static final String DEFAULT_COUNTRY = "global";

	public static final String DEFAULT_LANGUAGE = "en";

	public static final String DEFAULT_CURRENCY = "EUR";

	public static final String CP_KEY = "sector";

	public static final String APPNAME_KEY = "app.name";

	public static final String VERSION_KEY = "app.version";

	public static final String OS_KEY = "app.os";

	public static final String LANGUAGE_KEY = "locale.language";

	public static final String APPSID_KEY = "appsId";

	public static final String CURRENCY_KEY = "locale.currency";

	public static final String TIMESTAMP_KEY = "timestamp";

	public static final String CP_VALUE = "CP";

	public static final String APPNAME_VALUE = "registration";

	public static final String OS_ANDROID = "android";

	public static final String SEND_DATA = "sendData";

	public static final String REGISTRATION_CHANNEL = "registrationChannel";

	public static final String SPECIAL_EVENTS = "specialEvents";

	public static final String STATUS_NOTIFICATION = "statusNotification";

	public static final String MY_PHILIPS = "myphilips";

	public static final String START_USER_REGISTRATION = "startUserRegistration";

	public static final String  LOGIN_START = "loginStart";

	public static final String LOGIN_CHANNEL = "loginChannel";

	public static final String SUCCESS_USER_CREATION = "successUserCreation";

	public static final String SUCCESS_USER_REGISTRATION = "successUserRegistration";

	public static final String SUCCESS_SOCIAL_MERGE = "successSocialMerge";

	public static final String REMARKETING_OPTION_IN = "remarketingOptIn";

	public static final String ACCEPT_TERMS_OPTION_IN = "termsAndConditionsOptIn";

	public static final String ACCEPT_TERMS_OPTION_OUT = "termsAndConditionsOptOut";

	public static final String REMARKETING_OPTION_OUT = "remarketingOptOut";

	public static final String SUCCESS_LOGIN = "successLogin";

	public static final String START_SOCIAL_MERGE = "startSocialMerge";

	public static final String PREVIOUS_PAGE_NAME = "previousPageName";

	public static final String TECHNICAL_ERROR = "error";

	public static final String USER_ERROR = "error";

	public static final String RESET_PASSWORD_SUCCESS = "A link is sent to your email to reset the password of your Philips Account";

	public static final int NETWORK_ERROR_CODE = 111;

	public static final int EMAIL_NOT_VERIFIED_CODE = 112;

	public static final String COUNTRY_KEY = "locale.country";

	public static final String SIGN_OUT = "signOut";

	public static final String LOGOUT_BTN_SELECTED = "logoutButtonSelected";

	public static final String LOGOUT_SUCCESS = "logoutSuccess";

	public static final String SUCCESS_RESEND_EMAIL_VERIFICATION = "successResendEmailVerification";
	public static final String SUCCESS_RESEND_SMS_VERIFICATION = "successResendSMSVerification";


	public static final String RESEND_VERIFICATION_MAIL_LINK_SENT = "We have sent an email to your email address to reset your password";

	public static final String ALREADY_SIGN_IN_SOCIAL = "you have logged in with a social provider previously";

	public static final String TOTAL_TIME_CREATE_ACCOUNT = "totalTimeInCreateAccount";

	public static final String USER_ALERT = "userAlert";

	public static final String INVALID_EMAIL = "Invalid email address";

	public static final String INVALID_MOBILE = "Invalid mobile number";

	public static final String WRONG_PASSWORD = "The password does not follow the password guidelines below.";

	public static final String SHOW_PASSWORD = "showPassword";

	public static final String FIELD_CANNOT_EMPTY_NAME = "firsName : Field cannot be empty";

	public static final String FIELD_CANNOT_EMPTY_EMAIL = "emailAddress : Field cannot be empty";

	public static final String FIELD_CANNOT_EMPTY_PASSWORD = "password : Field cannot be empty";

    public static final String REGISTRATION_SPLIT_SIGN_UP = "registration1:Splitsign-up";

    public static final String REGISTRATION_CONTROL = "registration1:control";

    public static final String REGISTRATION_SOCIAL_PROOF = "registration1:Socialproof";

    public static final String REGISTRATION_ACTIVATION_SMS = "registration:accountactivationbysms";

    public static final String ACTIVATION_NOT_VERIFIED = "sms is not verified";

	public static final String EMAIL_NOT_VERIFIED  = "email is not verified";

	public static final String MOBILE_NUMBER_NOT_VERIFIED  = "mobile is not verified";

    public static final String MOBILE_RESEND_EMAIL_VERFICATION = "successResendEmailVerification";

    public static final String MOBILE_INAPPNATIFICATION = "inAppNotification ";

    public static final String MOBILE_RESEND_SMS_VERFICATION = "successResendSMSVerification";

    public static final String MOBILE_RESEND_SMS_VERFICATION_FAILURE = "failureResendSMSVerification";

    public static final String AB_TEST = "abtest";

	public static final String USER_REGISTRATION = "UR";

	public static final String LOGIN_FAILED = "failedLogin";

	public static final String REGISTRATION_FAILED = "failureUserCreation";

	public static final String JANRAIN = "Janrain";

	public static final String HSDP = "HSDP";

	public static final String EMAIL_VERIFICATION = "Please verify your email address through the activation link sent to your email account";

	public static final String NETWORK_ERROR = "Network Error";

	public static final String FAILURE_RESEND_EMAIL = "failureResendEmailVerification";

	public static final String FAILURE_FORGOT_PASSWORD = "failureForgotPassword";

	public static final String INVALID_CREDENTIALS="UR:failedLogin:Janrain:210:Incorrect username or password. Try again.";

    public static final String FAILURE_SERVICEDISCOVERY = "UR:RegistrationConfigurationFailed:ServiceDiscovery:";

	public static final String REG_JAN_RAIN_SERVER_CONNECTION_FAILED = "UR:Failed to connect to the server, Please try again after some time.";

	public static final String REG_NO_NETWORK_CONNECTION = "UR:Registration is not available because you are offline";
	
	public static final String REG_TRADITIONAL_SIGN_IN_FORGOT_PWD_SOCIAL_ERROR = "UR:No worries! You do not need a Philips password. You have logged in with a social provider previously.";

}
