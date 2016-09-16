package com.philips.cdp.registration.settings;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.localematch.BuildConfig;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.events.NetworkStateHelper;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.RLog;

import org.junit.Before;

import java.util.Locale;

import static org.junit.Assert.*;

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

    public void testRegistrationHelperGetCountryCode() {


        mRegistrationHelper.getCountryCode();
     }
    public void testRegistrationHelperRegister() {

   mRegistrationHelper.registerUserRegistrationListener(
                mUserRegistrationListener);
        mRegistrationHelper.unRegisterUserRegistrationListener(
                mUserRegistrationListener);
    }
    public void testRegistrationHelperRegistrationListener() {


        mRegistrationHelper.getUserRegistrationListener();
    }
    public void testRegistrationHelperRegistrationEventListener() {
        mRegistrationHelper.setUserRegistrationEventListener
                (mUserRegistrationListener);
        mRegistrationHelper.getUserRegistrationEventListener();
    }
    public void testRegistrationHelperNetworkStateListener() {
        mRegistrationHelper.registerNetworkStateListener(mNetworStateListener);
        mRegistrationHelper.unRegisterNetworkListener(mNetworStateListener);
   }
    public void testRegistrationHelperRegistrationApiVersion() {
    mRegistrationHelper.getRegistrationApiVersion();
    }
    public void testRegistrationHelper() {


        mRegistrationHelper.getCountryCode();
        mRegistrationHelper.registerUserRegistrationListener(
                mUserRegistrationListener);
        mRegistrationHelper.unRegisterUserRegistrationListener(
                mUserRegistrationListener);
        mRegistrationHelper.getUserRegistrationListener();
        mRegistrationHelper.setUserRegistrationEventListener
                (mUserRegistrationListener);
        mRegistrationHelper.getUserRegistrationEventListener();
        mRegistrationHelper.registerNetworkStateListener(mNetworStateListener);
        mRegistrationHelper.unRegisterNetworkListener(mNetworStateListener);
        mRegistrationHelper.getNetworkStateListener();
//        mRegistrationHelper.getLocale(mContext);
        mRegistrationHelper.getRegistrationApiVersion();
    }
}