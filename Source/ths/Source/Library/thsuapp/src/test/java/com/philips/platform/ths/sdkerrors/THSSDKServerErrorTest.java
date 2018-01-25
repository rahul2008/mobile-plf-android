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

public class THSSDKServerErrorTest {

    THSSDKServerError mThssdkServerError;
    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkServerError = new THSSDKServerError();
    }

    @Test
    public void getErrorMessage() throws Exception {
        final String errorMessage = mThssdkServerError.getErrorMessage(context);
        assert errorMessage == THSConstants.THS_GENERIC_SERVER_ERROR;
    }

    @Test
    public void validate_generic_exception() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.GENERIC_EXCEPTION);
        assert validate == true;
    }


    @Test
    public void validate_scheduled_down_time() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.AUTH_SCHEDULED_DOWNTIME);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_auth_system_unstable() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.AUTH_SYSTEM_UNSTABLE);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_auth_account_inactive() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.AUTH_ACCOUNT_INACTIVE);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_member_not_found() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.MEMBER_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_sdk_configuration_error() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.SDK_CONFIGURATION_ERROR);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_sdk_member_not_found() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.MEMBER_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_auth_access_denied() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.AUTH_ACCESS_DENIED);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_bad_date_format() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.VALIDATION_BAD_DATE_FORMAT);
        assert validate == true;
    }

    @Test
    public void validate_scheduled_bad_time() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.VALIDATION_BAD_DATE);
        assert validate == true;
    }

    @Test
    public void validate_provider_not_found() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.PROVIDER_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_provider_not_enabled() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.PROVIDER_VIDYO_NOT_ENABLED);
        assert validate == true;
    }

    @Test
    public void validate_mismatch() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.ENG_INITIATOR_MISMATCH);
        assert validate == true;
    }

    @Test
    public void validate_scan_failed() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.FILE_VIRUS_SCAN_FAILED);
        assert validate == true;
    }

    @Test
    public void validate_name_not_found() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.ENG_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_null() throws Exception {
        final boolean validate = mThssdkServerError.validate(SDKErrorReason.CHECK_IN_LATE);
        assert validate == false;
    }
}