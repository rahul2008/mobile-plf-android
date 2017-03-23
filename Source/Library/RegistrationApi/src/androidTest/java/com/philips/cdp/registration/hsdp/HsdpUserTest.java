package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;

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
        MultiDex.install(getInstrumentation().getTargetContext());
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





}