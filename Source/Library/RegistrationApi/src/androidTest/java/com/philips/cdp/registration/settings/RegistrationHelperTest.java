package com.philips.cdp.registration.settings;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;

import org.junit.Before;

/**
 * Created by 310243576 on 8/31/2016.
 */
public class RegistrationHelperTest extends InstrumentationTestCase{

    Context mContext;
    RegistrationHelper mRegistrationHelper;
    UserRegistrationListener mUserRegistrationListener;
    NetworStateListener mNetworStateListener;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext =  getInstrumentation()
                .getTargetContext();
        mRegistrationHelper = mRegistrationHelper.getInstance();
        mUserRegistrationListener = new UserRegistrationListener() {
            @Override
            public void onUserLogoutSuccess() {

            }

            @Override
            public void onUserLogoutFailure() {

            }

            @Override
            public void onUserLogoutSuccessWithInvalidAccessToken() {

            }
        };
        mNetworStateListener = new NetworStateListener() {
            @Override
            public void onNetWorkStateReceived(boolean isOnline) {

            }
        };
    }






}