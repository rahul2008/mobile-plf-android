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
    private Context mContext;

    public void testUser() throws Exception {
        super.setUp();
        prodRegHelper = new ProdRegHelper();
        mContext = getInstrumentation().getContext();
    }

    @Test
    public void testRegistrationWhenUserNotSignedIn() {
        ProdRegHelper helper = getProductHelper();
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        helper.registerProduct(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.USER_NOT_SIGNED_IN, errorType);
            }
        });
    }

    @Test
    public void testRegistrationWhenUserEnteredInvalidDate() {
        ProdRegHelper helper = getProductHelper2();
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        helper.registerProduct(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_DATE, errorType);
            }
        });
    }

    private ProdRegHelper getProductHelper() {
        return new ProdRegHelper() {
            protected Validator getValidator() {
                Validator validator = mock(Validator.class);
                User mUser = mock(User.class);
                when(validator.isUserSignedIn(mUser, mContext)).thenReturn(false);
                when(validator.isValidaDate("2016-3-22")).thenReturn(true);
                return validator;
            }
        };
    }

    private ProdRegHelper getProductHelper2() {
        return new ProdRegHelper() {
            protected Validator getValidator() {
                Validator validator = mock(Validator.class);
                User mUser = mock(User.class);
                when(validator.isUserSignedIn(mUser, mContext)).thenReturn(true);
                when(validator.isValidaDate("2016-3-22")).thenReturn(false);
                return validator;
            }
        };
    }
}