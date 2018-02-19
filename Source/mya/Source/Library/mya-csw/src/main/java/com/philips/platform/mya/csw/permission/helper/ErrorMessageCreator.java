/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.csw.permission.helper;


import android.content.Context;

import com.philips.platform.mya.csw.R;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_AUTHENTICATION_FAILURE;


public class ErrorMessageCreator {

    public static String getMessageErrorBasedOnErrorCode(Context context, int errorCode) {
        return errorCode == CONSENT_ERROR_AUTHENTICATION_FAILURE ? context.getString(R.string.csw_invalid_access_token_error_message) : context.getString(R.string.csw_problem_occurred_error_message, errorCode);
    }
}
