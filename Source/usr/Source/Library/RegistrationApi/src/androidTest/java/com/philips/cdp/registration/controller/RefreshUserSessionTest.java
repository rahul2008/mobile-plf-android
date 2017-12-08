package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class RefreshUserSessionTest extends RegistrationApiInstrumentationBase {

    RefreshUserSession mRefreshSession;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        RefreshLoginSessionHandler refreshLoginSessionHandler = new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {

            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {

            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        };
        Context context = getInstrumentation().getTargetContext();
        mRefreshSession = new RefreshUserSession(refreshLoginSessionHandler,context);
    }


    @Test
    public void testRefreshUserSession(){
//        mRefreshSession.refreshUserSession();

//        mRefreshSession.onFlowDownloadSuccess() ;

//        mRefreshSession.onFlowDownloadFailure() ;

//        mRefreshSession.onRefreshLoginSessionSuccess();

//        mRefreshSession.onRefreshLoginSessionFailedWithError(1);;
//
//        mRefreshSession.onRefreshLoginSessionInProgress("message");
//        assertNotNull(mRefreshSession);
    }




}