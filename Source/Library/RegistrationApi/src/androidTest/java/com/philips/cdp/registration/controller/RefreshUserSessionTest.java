package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/26/2016.
 */
public class RefreshUserSessionTest extends InstrumentationTestCase {

    RefreshUserSession mRefreshSession;


    @Before
    public void setUp() throws Exception {
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

        mRefreshSession.onRefreshLoginSessionFailedWithError(1);;

        mRefreshSession.onRefreshLoginSessionInProgress("message");
        assertNotNull(mRefreshSession);
    }




}