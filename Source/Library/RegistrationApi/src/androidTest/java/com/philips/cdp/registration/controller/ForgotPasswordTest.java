package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.janrain.android.Jump.ForgotPasswordResultHandler.ForgetPasswordError;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.AppInfra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/26/2016.
 */
public class ForgotPasswordTest extends InstrumentationTestCase{
    @Mock
    ForgotPassword mForgotPassword;
    Context context;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
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
    public void testForgotPassword(){
     assertNotNull(mForgotPassword);
        mForgotPassword.onSuccess() ;
//        AppInfraSingleton.setInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));
//        mForgotPassword.performForgotPassword("sample@emailAddress.com");
//        mForgotPassword.onFlowDownloadSuccess() ;
        mForgotPassword.onFlowDownloadFailure() ;

                    }

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