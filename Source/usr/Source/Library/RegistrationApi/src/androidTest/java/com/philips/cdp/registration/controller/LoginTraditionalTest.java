package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


public class LoginTraditionalTest extends RegistrationApiInstrumentationBase {
    Context context;
    LoginTraditional loginTraditional;
    @Before
    public void setUp() throws Exception {
        context =  getInstrumentation()
                .getTargetContext();
        SocialLoginProviderHandler traditionalSocialLoginProviderHandler = new SocialLoginProviderHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }

            @Override
            public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {

            }

            @Override
            public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {

            }

            @Override
            public void onContinueSocialProviderLoginSuccess() {

            }

            @Override
            public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

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
        loginTraditional = new LoginTraditional(traditionalSocialLoginProviderHandler,context,updateUserRecordHandler,"sample@sample.com","sample");
        synchronized(this){//synchronized block
        }
    }

//    @Test
//    public void testUpdateUIBasedOnConsentStatus(){
//        Method method = null;
//        String email="email@email.com" ;
//        String password="1234455";
//        try {
//            method =LoginTraditional.class.getDeclaredMethod("loginTraditionally",String.class,String.class);;
//            method.setAccessible(true);
//            method.invoke(loginTraditional,email,password);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
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