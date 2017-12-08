package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class RegisterTraditionalTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    RegisterTraditional mRegisterTraditional;

    @Before
    public void setUp() throws Exception {
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