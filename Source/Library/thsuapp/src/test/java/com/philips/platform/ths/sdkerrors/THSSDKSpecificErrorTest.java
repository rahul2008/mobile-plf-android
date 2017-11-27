/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKErrorReason;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSSDKSpecificErrorTest {

    THSSDKSpecificError mThssdkSpecificError;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkSpecificError = new THSSDKSpecificError();
    }

    @Test
    public void getErrorMessage() throws Exception {
        final String errorMessage = mThssdkSpecificError.getErrorMessage();
        assert errorMessage.equalsIgnoreCase("");
        assert errorMessage.isEmpty();
    }

    @Test
    public void validate_speciality_not_found() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.ONDEMAND_SPECIALTY_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_disclaimer_missing() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PRIVACY_DISCLAIMER_MISSING);
        assert validate == true;
    }

    @Test
    public void validate_member_upgrade() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_MEMBER_UNDERAGE);
        assert validate == true;
    }

    @Test
    public void validate_email_in_use() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_EMAIL_IN_USE);
        assert validate == true;
    }

    @Test
    public void validate_par_too_short() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_REQ_PARAM_TOO_SHORT);
        assert validate == true;
    }

    @Test
    public void validate_bad_format() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_BAD_COORDINATE_FORMAT);
        assert validate == true;
    }

    @Test
    public void validate_bad_integer() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_BAD_INTEGER_FORMAT);
        assert validate == true;
    }

    @Test
    public void validate_pharmacy_not_found() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.MEMBER_PRIMARY_PHARMACY_NOT_FOUND);
        assert validate == true;
    }

    @Test
    public void validate_credit_card_missing() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_MISSING);
        assert validate == true;
    }

    @Test
    public void validate_invalid_coupon() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.VALIDATION_INVALID_COUPON);
        assert validate == true;
    }

    @Test
    public void validate_provider_not_available() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PROVIDER_NOT_AVAILABLE);
        assert validate == true;
    }

    @Test
    public void validate_room_access_denied() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.WAITING_ROOM_ACCESS_DENIED);
        assert validate == true;
    }

    @Test
    public void validate_room_license_denied() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.PROVIDER_NOT_LICENSED_FOR_MEMBER_STATE);
        assert validate == true;
    }

    @Test
    public void validate_validation_failed() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_VALIDATION_ERROR);
        assert validate == true;
    }

    @Test
    public void validate_incorrect_cvv() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_INCORRECT_CVV);
        assert validate == true;
    }

    @Test
    public void validate_card_declined_error() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_DECLINED_ERROR);
        assert validate == true;
    }

    @Test
    public void validate_invalid_zip() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_INVALID_ZIP);
        assert validate == true;
    }

    @Test
    public void validate_check_failed() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CREDIT_CARD_RESIDENCY_CHECK_FAILED);
        assert validate == true;
    }

    @Test
    public void validate_scheduling_failure() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.ENG_SCHEDULING_FAILURE);
        assert validate == true;
    }

    @Test
    public void validate_not_matched() throws Exception {
        final boolean validate = mThssdkSpecificError.validate(SDKErrorReason.CHECK_IN_LATE);
        assert validate == false;
    }

}