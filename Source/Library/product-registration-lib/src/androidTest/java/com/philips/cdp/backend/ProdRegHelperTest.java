package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.error.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.User;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ProdRegHelperTest extends MockitoTestCase {

    ProdRegHelper prodRegHelper;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        prodRegHelper = new ProdRegHelper();
        mContext = getInstrumentation().getContext();
    }

    @Test
    public void testRegistrationWhenUserNotSignedIn() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        Validator validator = mock(Validator.class);
        User user = mock(User.class);
        when(user.isUserSignIn(mContext)).thenReturn(false);
        when(user.getEmailVerificationStatus(mContext)).thenReturn(false);
        when(validator.isUserSignedIn(user, mContext)).thenReturn(false);
        when(validator.isValidaDate("2016-3-22")).thenReturn(true);
        prodRegHelper.processForReg(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.USER_NOT_SIGNED_IN, errorType);
            }
        }, validator, user);
    }

    @Test
    public void testRegistrationWhenEnteredInvalidDate() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        Validator validator = mock(Validator.class);
        User user = mock(User.class);
        when(user.isUserSignIn(mContext)).thenReturn(true);
        when(user.getEmailVerificationStatus(mContext)).thenReturn(true);
        when(validator.isUserSignedIn(user, mContext)).thenReturn(true);
        when(validator.isValidaDate("2016-3-22")).thenReturn(false);
        prodRegHelper.processForReg(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_DATE, errorType);
            }
        }, validator, user);

    }
}