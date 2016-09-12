package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;

/**
 * Created by 310243576 on 8/30/2016.
 */
public class LoginTraditionalTest extends InstrumentationTestCase {
    Context context;
    LoginTraditional loginTraditional;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        context =  getInstrumentation()
                .getTargetContext();
        TraditionalLoginHandler traditionalLoginHandler = new TraditionalLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        UpdateUserRecordHandler updateUserRecordHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        loginTraditional = new LoginTraditional(traditionalLoginHandler,context,updateUserRecordHandler,"sample@sample.com","sample");
        synchronized(this){//synchronized block

            try{
                RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().build(context));
            }catch(Exception e){System.out.println(e);}
        }
    }

    public void testLoginTradional()
    {
        assertNotNull(loginTraditional);
//        loginTraditional.loginTraditionally("sample@sample.com","sample");
    }
}