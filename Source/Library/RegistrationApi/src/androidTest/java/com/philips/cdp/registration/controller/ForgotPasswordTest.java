package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


public class ForgotPasswordTest extends RegistrationApiInstrumentationBase {
    @Mock
    ForgotPassword mForgotPassword;
    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ForgotPasswordHandler forgotPaswordHandler = new ForgotPasswordHandler() {
            @Override
            public void onSendForgotPasswordSuccess() {

            }

            @Override
            public void onSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        context = getInstrumentation().getTargetContext();
        mForgotPassword = new ForgotPassword(context,forgotPaswordHandler);

    }

    @Test
    public void testForgotPassword() {
//        assertNotNull(mForgotPassword);
//        mForgotPassword.onSuccess();
////        AppInfraSingleton.setInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));
////        mForgotPassword.performForgotPassword("sample@emailAddress.com");
////        mForgotPassword.onFlowDownloadSuccess() ;
//        mForgotPassword.onFlowDownloadFailure();

    }
@Test
    public void testGetErrorMessage(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("sample");
        Method method = null;
        try {
            method = ForgotPassword.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(mForgotPassword,jsonArray);
            jsonArray = null;
            method.invoke(mForgotPassword,jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}