package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;


public class ResendVerificationEmailTest extends RegistrationApiInstrumentationBase {

    Context context;
    @Mock
    ResendVerificationEmail mResendVerificationEmail;
    ResendVerificationEmailHandler mResendVerificationEmailHandler;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mResendVerificationEmailHandler = new ResendVerificationEmailHandler() {
            @Override
            public void onResendVerificationEmailSuccess() {

            }

            @Override
            public void onResendVerificationEmailFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        assertNotNull(mResendVerificationEmailHandler);
        mResendVerificationEmail = new ResendVerificationEmail(context,mResendVerificationEmailHandler);
        assertNotNull(mResendVerificationEmail);

    }
    @Test
    public void testmResendVerificationEmail(){
        CaptureApiError error = new CaptureApiError();
        mResendVerificationEmail.onFailure(error);
        assertSame(mResendVerificationEmail.mResendVerificationEmail,mResendVerificationEmailHandler );
    }
    public void testResendVerificationMail(){

        synchronized(this){//synchronized block

            try{
                mResendVerificationEmail.resendVerificationMail("emailAddress@sample.com");

            }catch(Exception ignored){}
        }

        Method method = null;
        String s= "";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("hdll");
        try {
            method = ResendVerificationEmail.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(mResendVerificationEmail, jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
         jsonArray = null;
        try {
            method = ResendVerificationEmail.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(mResendVerificationEmail, jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}