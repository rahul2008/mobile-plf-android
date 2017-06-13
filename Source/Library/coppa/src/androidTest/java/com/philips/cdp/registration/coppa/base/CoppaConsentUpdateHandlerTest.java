package com.philips.cdp.registration.coppa.base;

import com.google.gson.JsonObject;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;


public class CoppaConsentUpdateHandlerTest extends RegistrationApiInstrumentationBase {
    CoppaConsentUpdateHandler mCoppaConsentUpdateHandler;
     CoppaConsentUpdateCallback mCoppaConsentUpdateCallback;


    @Before
    public void setUp() throws Exception {
        super.setUp();
       super.setUp();
         mCoppaConsentUpdateCallback = new CoppaConsentUpdateCallback() {
             @Override
             public void onSuccess() {

             }

             @Override
             public void onFailure(int message) {

             }
         };
        mCoppaConsentUpdateHandler = new CoppaConsentUpdateHandler(mCoppaConsentUpdateCallback);

    }

    public void testCoppaConsentUpdateHandler(){

        mCoppaConsentUpdateHandler.onFailure(new CaptureApiError());
        mCoppaConsentUpdateHandler.onSuccess();
        assertNotNull(mCoppaConsentUpdateHandler);
    }
    @Test
    public void testBuildConsentStatus() {
        Method method = null;

        JsonObject jsonObject=new JsonObject();
        Boolean coppaConsentStatus = true;
         try {
            method = CoppaConsentUpdateHandler.class.getDeclaredMethod("buildConsentStatus", new Class[] { Boolean.class, JsonObject.class });
            method.setAccessible(true);
            method.invoke(mCoppaConsentUpdateHandler,coppaConsentStatus,jsonObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
   /* @Test
    public void testBuildConsentConfirmation() {
        Method method = null;

        JsonObject jsonObject=new JsonObject();
        Boolean coppaConsentConfirmationStatus = true;
        try {
            method = CoppaConsentUpdateHandler.class.getDeclaredMethod("buildConsentConfirmation", new Class[] { Boolean.class, JsonObject.class });
            method.setAccessible(true);
            method.invoke(mCoppaConsentUpdateHandler,coppaConsentStatus,jsonObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*//**/

}