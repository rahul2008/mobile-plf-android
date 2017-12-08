package com.philips.cdp.registration.controller;



import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


public class RegisterSocialTest extends RegistrationApiInstrumentationBase {
    Context mContext;
    RegisterSocial mRegisterSocial;

    @Before
    public void setUp() throws Exception {
           super.setUp();
        mContext = getInstrumentation().getTargetContext();
        SocialProviderLoginHandler socialProviderLoginHandler = new SocialProviderLoginHandler() {
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
        mRegisterSocial = new RegisterSocial(socialProviderLoginHandler,mContext,updateUserRecordHandler);
    }

    @Test
    public  void testRegisterSocial(){
//        assertNotNull(mRegisterSocial);
//
//        //mRegisterSocial.onSuccess();
//      mRegisterSocial.onFlowDownloadFailure();
//        mRegisterSocial.onCode("");
    }
    public void testGetErrorMessage(){
        JSONArray jsonArray = new JSONArray();
        Method method = null;
        try {
            method = RegisterSocial.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(mRegisterSocial,jsonArray);
            jsonArray = null;
            method.invoke(mRegisterSocial,jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testHandleOnLoginSuccess(){
        Method method = null;
        try {
            method = RegisterSocial.class.getDeclaredMethod("handleOnLoginSuccess");
            method.setAccessible(true);
            method.invoke(mRegisterSocial);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testRegisterNewUser(){
        Method method = null;
        final JSONObject user = new JSONObject();
         final String userRegistrationToken = "sample";
        try {
            method = RegisterSocial.class.getDeclaredMethod("registerNewUser",new Class[]{JSONObject.class,  String.class});
            method.setAccessible(true);
            method.invoke(mRegisterSocial,new Object[]{user, userRegistrationToken});

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}