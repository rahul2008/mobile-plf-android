
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.app.tagging;


import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.settings.RegistrationHelper;

public class AppTaggingErrors {

    private static final String EMAIL_ALREADY_IN_USE = "email already in use";

    private static final String MOBILE_ALREADY_IN_USE = "mobile no already in use";

    private static final String EMAIL_IS_NOT_VERIFIED = "email is not verified";

    private static final String WE_RE_HAVING_TROUBLE_LOGINING_USER = "login network error";

    private static final String WE_RE_HAVING_TROUBLE_REGISTRING_USER = "registration network error";

    private static final String EMAIL_ADDRESS_NOT_EXIST_ERROR = "no account with this email address";

    private static final String RESEND_VERIFICATION_NETWORK_ERROR = "resend verification network error";

    private static final String FAILURE_FORGOT_PASSWORD_ERROR = "forgot password network error";

    private final static int NETWORK_ERROR_CODE = 111;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private final static int INVALID_CREDENTIALS_CODE = 210;

    private final static int EMAIL_NOT_VERIFIED_CODE = 112;

    private final static int FORGOT_PASSWORD_FAILURE_ERROR_CODE = 212;


    public static void trackActionForgotPasswordFailure(UserRegistrationFailureInfo userRegistrationFailureInfo,String flowType) {
        switch (userRegistrationFailureInfo.getErrorCode()) {

            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD_ERROR);
                break;
            case FORGOT_PASSWORD_FAILURE_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, EMAIL_ADDRESS_NOT_EXIST_ERROR);
                break;

            default:
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ERROR, AppTagingConstants.USER_REGISTRATION+":"+AppTagingConstants.FAILURE_FORGOT_PASSWORD+":"
                        +flowType+":"+userRegistrationFailureInfo.getErrorCode()+":"+userRegistrationFailureInfo.getErrorDescription());
                break;
        }
    }

    public static void trackActionResendNetworkFailure(UserRegistrationFailureInfo userRegistrationFailureInfo,String flowType) {
        switch (userRegistrationFailureInfo.getErrorCode()) {

            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, RESEND_VERIFICATION_NETWORK_ERROR);
                break;

            default:
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ERROR, AppTagingConstants.USER_REGISTRATION+":"+AppTagingConstants.FAILURE_RESEND_EMAIL+":"
                        +flowType+":"+userRegistrationFailureInfo.getErrorCode()+":"+userRegistrationFailureInfo.getErrorDescription());
                break;
        }
    }

    static void trackActionForErrorMapping(String sendData, String technicalError,
                                                   String technicalRegistrationError) {
        AppTagging.trackAction(sendData, technicalError, technicalRegistrationError);
    }

    public static void trackActionLoginError(UserRegistrationFailureInfo userRegistrationFailureInfo,String flowType) {

        switch (userRegistrationFailureInfo.getErrorCode()) {
            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_LOGINING_USER);
                break;
            case EMAIL_NOT_VERIFIED_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ERROR, EMAIL_IS_NOT_VERIFIED);
                break;
            default:
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ERROR, AppTagingConstants.USER_REGISTRATION+":"+AppTagingConstants.LOGIN_FAILED+":"
                        +flowType+":"+userRegistrationFailureInfo.getErrorCode()+":"+userRegistrationFailureInfo.getErrorDescription());
                break;
        }

    }

    public static void trackActionRegisterError(UserRegistrationFailureInfo userRegistrationFailureInfo, String flowType) {

        switch (userRegistrationFailureInfo.getErrorCode()) {
            case NETWORK_ERROR_CODE:
                trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, WE_RE_HAVING_TROUBLE_REGISTRING_USER);
                break;
            case EMAIL_ADDRESS_ALREADY_USE_CODE:
                if (RegistrationHelper.getInstance().isMobileFlow()){
                    trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                            AppTagingConstants.USER_ERROR, MOBILE_ALREADY_IN_USE);
                }else{
                    trackActionForErrorMapping(AppTagingConstants.SEND_DATA,
                            AppTagingConstants.USER_ERROR, EMAIL_ALREADY_IN_USE);
                }
                break;
            default:
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ERROR, AppTagingConstants.USER_REGISTRATION+":"+AppTagingConstants.REGISTRATION_FAILED+":"
                        +flowType+":"+userRegistrationFailureInfo.getErrorCode()+":"+userRegistrationFailureInfo.getErrorDescription());
                break;
        }

    }
}
