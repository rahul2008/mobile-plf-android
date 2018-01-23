/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSSDKUserErrorTest {
    THSSDKUserError mThssdkUserError;
    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkUserError = new THSSDKUserError();
    }

    @Test
    public void getErrorMessage() throws Exception {
        final String errorMessage = mThssdkUserError.getErrorMessage(context);
        assertNotNull(errorMessage);
        assert errorMessage == THSConstants.THS_GENERIC_USER_ERROR;
    }

    @Test
    public void validate_parameter_missing() throws Exception {
        final boolean validate = mThssdkUserError.validate(SDKErrorReason.VALIDATION_REQ_PARAM_MISSING);
        assert validate == true;
    }

    @Test
    public void validate_access_missing() throws Exception {
        final boolean validate = mThssdkUserError.validate(SDKErrorReason.AUTH_ACCESS_DENIED);
        assert validate == true;
    }

    @Test
    public void validate_account_not_enrolled() throws Exception {
        final boolean validate = mThssdkUserError.validate(SDKErrorReason.AUTH_ACCOUNT_NOT_ENROLLED);
        assert validate == true;
    }

    @Test
    public void validate_account_locked() throws Exception {
        final boolean validate = mThssdkUserError.validate(SDKErrorReason.AUTH_ACCOUNT_LOCKED);
        assert validate == true;
    }

    @Test
    public void validate_not_user_error() throws Exception {
        final boolean validate = mThssdkUserError.validate(SDKErrorReason.CHECK_IN_LATE);
        assert validate == false;
    }

}