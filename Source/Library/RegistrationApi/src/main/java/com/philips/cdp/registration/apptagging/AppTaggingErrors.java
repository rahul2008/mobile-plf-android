
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.apptagging;



public class AppTaggingErrors {

    private static final String FAILEDLOGIN = "failedlogin";

    private static final String FAILURE_USERCREATION = "failureUsercreation";

    private static final String EMAIL_ALREADY_IN_USE = "email already in use";

    private static final String INVALID_INPUT_FIELDS = "incorrect email or password";

    private static final String EMAIL_IS_NOT_VERIFIED = "email is not verified";

    private static final String WE_RE_HAVING_TROUBLE_LOGINING_USER = "login network error";

    private static final String WE_RE_HAVING_TROUBLE_REGISTRING_USER = "registration network error";

    private static final String EMAIL_ADDRESS_NOT_EXIST_ERROR = "no account with this email address";

    private static final String RESEND_VERIFICATION_NETWORK_ERROR = "resend verification network error";

    private static final String FAILURE_FORGOT_PASSWORD = "failureForgotPassword";

    private static final String FAILURE_FORGOT_PASSWORD_ERROR = "forgot password network error";

    private static final String FAILURE_RESEND_VERIFICATION = "failureResendEmailVerification";

    private final static int NETWORK_ERROR_CODE = 111;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private final static int INVALID_INPUT_FIELDS_CODE = 210;

    private final static int EMAIL_NOT_VERIFIED_CODE = 112;

    private final static int FORGOT_PASSWORD_FAILURE_ERROR_CODE = 212;

    public static void trackActionRegisterError(int errorCode) {

        switch (errorCode) {
            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_REGISTRING_USER);
                break;
            case EMAIL_ADDRESS_ALREADY_USE_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, EMAIL_ALREADY_IN_USE);
                break;
            case INVALID_INPUT_FIELDS_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, INVALID_INPUT_FIELDS);
                break;
            default:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILURE_USERCREATION);
                break;
        }
    }

    public static void trackActionLoginError(int errorCode) {
        switch (errorCode) {
            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_LOGINING_USER);
                break;
            case EMAIL_NOT_VERIFIED_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, EMAIL_IS_NOT_VERIFIED);
                break;
            case INVALID_INPUT_FIELDS_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, INVALID_INPUT_FIELDS);
                break;
            default:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILEDLOGIN);
                break;
        }
    }

    public static void trackActionForgotPasswordFailure(int errorCode) {
        switch (errorCode) {

            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD_ERROR);
                break;
            case FORGOT_PASSWORD_FAILURE_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, EMAIL_ADDRESS_NOT_EXIST_ERROR);
                break;

            default:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD);
                break;
        }
    }

    public static void trackActionResendNetworkFailure(int errorCode) {
        switch (errorCode) {
            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, RESEND_VERIFICATION_NETWORK_ERROR);
                break;

            default:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILURE_RESEND_VERIFICATION);
                break;
        }
    }

    private static void trackActionForErrorMapping(String sendData, String technicalError,
                                                   String technicalRegistrationError) {
        AppTagging.trackAction(sendData, technicalError, technicalRegistrationError);
    }
}
