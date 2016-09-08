package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class HsdpUserTest extends InstrumentationTestCase {

    HsdpUser mHsdpUser;

    Context context;
    LogoutHandler logoutHandler;
    RefreshLoginSessionHandler refreshLoginSessionHandler;

    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mHsdpUser = new HsdpUser(context);
        mHsdpUser=new HsdpUser(getInstrumentation().getContext());
        logoutHandler= new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {

            }

            @Override
            public void onLogoutFailure(final int responseCode, final String message) {

            }
        };

    }
    public void testHsdpUser(){
        assertNotNull(mHsdpUser);
        assertFalse(mHsdpUser.isHsdpUserSignedIn());
    }
    @Test
    public void testLogOut(){
        mHsdpUser.logOut(logoutHandler);
        NetworkUtility.isNetworkAvailable(getInstrumentation().getContext());

    }
    @Test
    public void testRefresh(){
        refreshLoginSessionHandler= new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {

            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int error) {

            }

            @Override
            public void onRefreshLoginSessionInProgress(final String message) {

            }
        };
        mHsdpUser.refreshToken(refreshLoginSessionHandler);
    }

    @Test
    public void testIsHsdpUserSignedIn(){
        boolean result =mHsdpUser.isHsdpUserSignedIn();
        assertFalse(result);
     }
}