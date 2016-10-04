package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        loginTraditional.onFailure(null);
//        loginTraditional.loginTraditionally("sample@sample.com","sample");
    }
    @Test
    public void testUpdateUIBasedOnConsentStatus(){
        Method method = null;
        String email="email@email.com" ;
        String password="1234455";
        try {
            method =LoginTraditional.class.getDeclaredMethod("loginTraditionally",String.class,String.class);;
            method.setAccessible(true);
            method.invoke(loginTraditional,email,password);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public void testGetErrorMessage(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("sample");
        Method method = null;
        try {
            method = LoginTraditional.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(loginTraditional,jsonArray);
            jsonArray = null;
            method.invoke(loginTraditional,jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}