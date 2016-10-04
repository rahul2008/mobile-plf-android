package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.engage.JREngageError;
import com.janrain.android.engage.types.JRDictionary;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.janrain.android.Jump.SignInResultHandler.SignInError.FailureReason.CAPTURE_API_ERROR;
import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/31/2016.
 */
public class RegisterTraditionalTest extends InstrumentationTestCase {

    Context mContext;
    RegisterTraditional mRegisterTraditional;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        TraditionalRegistrationHandler traditionalRegistrationHandler = new TraditionalRegistrationHandler() {
            @Override
            public void onRegisterSuccess() {

            }

            @Override
            public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

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
        mRegisterTraditional = new RegisterTraditional(traditionalRegistrationHandler, mContext, updateUserRecordHandler);
    }


    @Test
    public  void testRegisterSocial(){
        mRegisterTraditional.onRegisterFailedWithFailure(null);

        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        mRegisterTraditional.onRegisterFailedWithFailure(userRegistrationFailureInfo);
        Jump.SignInResultHandler.SignInError.FailureReason reason = null;


        mRegisterTraditional.onFailure(null);

        //mRegisterSocial.onSuccess();
        //mRegisterTraditional.onFlowDownloadFailure();

        assertNotNull(mRegisterTraditional);

    }
    public void testGetErrorMessage(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("sample");
        Method method = null;
        try {
            method = RegisterTraditional.class.getDeclaredMethod("getErrorMessage", JSONArray.class);
            method.setAccessible(true);
            method.invoke(mRegisterTraditional,jsonArray);
            jsonArray = null;
            method.invoke(mRegisterTraditional,jsonArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}