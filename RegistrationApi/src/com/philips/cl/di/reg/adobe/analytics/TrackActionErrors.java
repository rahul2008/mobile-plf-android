
package com.philips.cl.di.reg.adobe.analytics;

public class TrackActionErrors {

	private static final String FAILEDLOGIN = "failedlogin";

	private static final String FAILURE_USERCREATION = "failureUsercreation";

	private static final String EMAIL_ALREADY_IN_USE = "email already in use";

	private static final String INVALID_INPUT_FIELDS = "invalid input fields";

	private static final String EMAIL_IS_NOT_VERIFIED = "email is not verified";

	private static final String WE_RE_HAVING_TROUBLE_LOGINING_USER = "we're having trouble logining user";

	private static final String WE_RE_HAVING_TROUBLE_REGISTRING_USER = "we're having trouble registring user";

	private final static int NETWORK_ERROR_CODE = 111;

	private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

	private final static int INVALID_INPUT_FIELDS_CODE = 210;

	private final static int EMAIL_NOT_VERIFIED_CODE = 112;

	public static void trackActionRegisterError(int errorCode) {
		
		switch (errorCode) {
			case NETWORK_ERROR_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_REGISTRING_USER);
				break;
			case EMAIL_ADDRESS_ALREADY_USE_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.USER_ERROR, EMAIL_ALREADY_IN_USE);
				break;
			case INVALID_INPUT_FIELDS_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.USER_ERROR, INVALID_INPUT_FIELDS);
				break;
			default:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.SPECIAL_EVENTS, FAILURE_USERCREATION);
				break;

		}
	}

	public static void trackActionLoginError(int errorCode) {
		switch (errorCode) {
			case NETWORK_ERROR_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_LOGINING_USER);
				break;
			case EMAIL_NOT_VERIFIED_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.USER_ERROR, EMAIL_IS_NOT_VERIFIED);
				break;
			case INVALID_INPUT_FIELDS_CODE:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.USER_ERROR, INVALID_INPUT_FIELDS);
				break;
			default:
				trackActionForErrorMapping(AnalyticsConstants.SEND_DATA,
				        AnalyticsConstants.SPECIAL_EVENTS, FAILEDLOGIN);
				break;
		}
	}

	private static void trackActionForErrorMapping(String sendData, String technicalError,
	        String technicalRegistrationError) {
		AnalyticsUtils.trackAction(sendData, technicalError, technicalRegistrationError);
	}
}
