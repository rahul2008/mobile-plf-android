package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.error.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.model.ProdRegRegisteredDataResponse;
import com.philips.cdp.model.ProdRegRegisteredResults;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        mContext = getInstrumentation().getContext();
        prodRegHelper = new ProdRegHelper();
    }

    public void testRegistrationWhenUserNotSignedIn() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegManager prodRegManager = mock(ProdRegManager.class);
        User user = mock(User.class);
        when(user.isUserSignIn(mContext)).thenReturn(false);
        when(user.getEmailVerificationStatus(mContext)).thenReturn(false);
        when(prodRegManager.isUserSignedIn(user, mContext)).thenReturn(false);
        when(prodRegManager.isValidaDate("2016-3-22")).thenReturn(true);
        prodRegHelper.processForReg(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.USER_NOT_SIGNED_IN, errorType);
            }
        }, prodRegManager, user);
    }

    public void testRegistrationWhenEnteredInvalidDate() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegManager prodRegManager = mock(ProdRegManager.class);
        User user = mock(User.class);
        when(user.isUserSignIn(mContext)).thenReturn(true);
        when(user.getEmailVerificationStatus(mContext)).thenReturn(true);
        when(prodRegManager.isUserSignedIn(user, mContext)).thenReturn(true);
        when(prodRegManager.isValidaDate("2016-3-22")).thenReturn(false);
        prodRegHelper.processForReg(mContext, prodRegRequestInfo, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.MISSING_DATE, errorType);
            }
        }, prodRegManager, user);
    }

    public void testGettingRegistrationListener() throws Exception {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        assertTrue(prodRegHelper.getRegisteredProductsListener(mContext, prodRegRequestInfo, listener) instanceof ProdRegListener);
    }

    public void testGetRegisteredProductsListener() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        when(prodRegRequestInfo.getCtn()).thenReturn("HD8967/09");
        ProdRegListener getRegisteredProductsListener = prodRegHelper.
                getRegisteredProductsListener(mContext, prodRegRequestInfo, listener);
        ProdRegRegisteredDataResponse responseMock = mock(ProdRegRegisteredDataResponse.class);
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        when(responseMock.getResults()).thenReturn(results);
        getRegisteredProductsListener.onProdRegSuccess(responseMock);
        verify(listener).onProdRegFailed(ErrorType.PRODUCT_ALREADY_REGISTERED);
        getRegisteredProductsListener.onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
        verify(listener).onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
    }

    public void testInvocationToMetadata() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        ProdRegManager prodRegManager = mock(ProdRegManager.class);
        when(prodRegRequestInfo.getCtn()).thenReturn("HD8970/09");
        ProdRegListener getRegisteredProductsListener = prodRegHelper.
                getRegisteredProductsListener(mContext, prodRegRequestInfo, listener);
        ProdRegRegisteredDataResponse responseMock = mock(ProdRegRegisteredDataResponse.class);
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        when(responseMock.getResults()).thenReturn(results);
        getRegisteredProductsListener.onProdRegSuccess(responseMock);
        verify(prodRegManager).processMetadata(mContext, prodRegRequestInfo, listener);
    }

   /* public void testInvocation() {
        ProdRegManager prodRegManager = mock(ProdRegManager.class);
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        UserProduct userProduct = mock(UserProduct.class);
        when((prodRegManager.getUserProduct())).thenReturn(userProduct);
        prodRegHelper.getRegisteredProduct(mContext, prodRegRequestInfo, prodRegListener, prodRegManager);
        verify(userProduct).getRegisteredProducts(mContext, prodRegRequestInfo, prodRegListener);
    }*/

}