package com.philips.cdp.registration.controller;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 8/26/2016.
 */
public class RefreshUserSessionTest extends InstrumentationTestCase {

    RefreshUserSession mRefreshSession;


    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
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