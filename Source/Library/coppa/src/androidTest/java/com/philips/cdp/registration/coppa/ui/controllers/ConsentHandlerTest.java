package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ConsentHandlerTest extends InstrumentationTestCase{

    Context mContext;
    ConsentHandler mConfirmationHandler;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConsentHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
    }

    @Test
    public void testConsentHandler(){
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConsentHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
    }
    @Test
    public void testUpdateUiBasedOnConsentStatus(){
        Method method = null;
        try {
            method =ConsentHandler.class.getDeclaredMethod("updateUiBasedOnConsentStatus", CoppaStatus.class);;
            method.setAccessible(true);
            method.invoke(mConfirmationHandler,CoppaStatus.kDICOPPAConsentPending);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testHandleFailure(){
        Method method = null;
        try {
            method =ConsentHandler.class.getDeclaredMethod("handleFailure");;
            method.setAccessible(true);
            method.invoke(mConfirmationHandler);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCompleteConsentActions(){
        Method method = null;
        try {
            method =ConsentHandler.class.getDeclaredMethod("completeConsentActions", CoppaStatus.class);;
            method.setAccessible(true);
            method.invoke(mConfirmationHandler,CoppaStatus.kDICOPPAConsentGiven);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}