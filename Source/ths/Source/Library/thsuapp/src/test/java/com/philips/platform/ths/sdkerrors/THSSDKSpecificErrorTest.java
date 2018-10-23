/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class THSSDKSpecificErrorTest {

    THSSDKSpecificError mThssdkSpecificError;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkSpecificError = new THSSDKSpecificError();
        mThssdkSpecificError.context = context;
    }

    @Test
    public void getErrorMessage() throws Exception {
        final String errorMessage = mThssdkSpecificError.getErrorMessage(context);
        assert errorMessage.equalsIgnoreCase("");
        assert errorMessage.isEmpty();
    }

    @Test
    public void validate_speciality_not_found() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.ONDEMAND_SPECIALTY_NOT_FOUND, context);
        assert validate == true;
    }

    @Test
    public void validate_disclaimer_missing() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PRIVACY_DISCLAIMER_MISSING, context);
        assert validate == true;
    }

    @Test
    public void validate_member_upgrade() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_CONSUMER_UNDERAGE, context);
        assert validate == true;
    }

    @Test
    public void validate_email_in_use() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_EMAIL_IN_USE, context);
        assert validate == true;
    }

    @Test
    public void validate_par_too_short() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_REQ_PARAM_TOO_SHORT, context);
        assert validate == true;
    }

    @Test
    public void validate_bad_format() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_BAD_COORDINATE_FORMAT, context);
        assert validate == true;
    }

    @Test
    public void validate_bad_integer() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_BAD_INTEGER_FORMAT, context);
        assert validate == true;
    }

    @Test
    public void validate_pharmacy_not_found() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CONSUMER_PRIMARY_PHARMACY_NOT_FOUND, context);
        assert validate == true;
    }

    @Test
    public void validate_credit_card_missing() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_MISSING, context);
        assert validate == true;
    }

    @Test
    public void validate_invalid_coupon() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_INVALID_COUPON, context);
        assert validate == true;
    }

    @Test
    public void validate_provider_not_available() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PROVIDER_NOT_AVAILABLE, context);
        assert validate == true;
    }

    @Test
    public void validate_room_access_denied() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.WAITING_ROOM_ACCESS_DENIED, context);
        assert validate == true;
    }

    @Test
    public void validate_room_license_denied() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PROVIDER_NOT_LICENSED_FOR_CONSUMER_STATE, context);
        assert validate == true;
    }

    @Test
    public void validate_validation_failed() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_VALIDATION_ERROR, context);
        assert validate == true;
    }

    @Test
    public void validate_incorrect_cvv() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_INCORRECT_CVV, context);
        assert validate == true;
    }

    @Test
    public void validate_card_declined_error() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_DECLINED_ERROR, context);
        assert validate == true;
    }

    @Test
    public void validate_invalid_zip() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_INVALID_ZIP, context);
        assert validate == true;
    }

    @Test
    public void validate_check_failed() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_RESIDENCY_CHECK_FAILED, context);
        assert validate == true;
    }

    @Test
    public void validate_scheduling_failure() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.ENG_SCHEDULING_FAILURE, context);
        assert validate == true;
    }

    @Test
    public void validate_not_matched() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CHECK_IN_LATE, context);
        assert validate == false;
    }

    @Test
    public void validation_req_param_invalid_matched() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_REQ_PARAM_INVALID,context);
        assertTrue(validate);
    }

    @Test
    public void validation_req_param_invalid_not_matched() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CHECK_IN_LATE,context);
        assertFalse(validate);
    }
}