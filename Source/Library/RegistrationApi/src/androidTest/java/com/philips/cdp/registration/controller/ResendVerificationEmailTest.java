package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class ResendVerificationEmailTest extends InstrumentationTestCase{

    Context context;
    @Mock
    ResendVerificationEmail mResendVerificationEmail;
    ResendVerificationEmailHandler mResendVerificationEmailHandler;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
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
                RegistrationHelper.getInstance().setAppInfraInstance( new AppInfra.Builder().build(context));
                mResendVerificationEmail.resendVerificationMail("emailAddress@sample.com");

            }catch(Exception e){System.out.println(e);}
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

    }
}