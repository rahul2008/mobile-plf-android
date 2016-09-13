package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ConfirmationHandlerTest extends InstrumentationTestCase {

    Context mContext;
    ConfirmationHandler mConfirmationHandler;
    ParentalConsentFragment mParentalConsentFragment;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConfirmationHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
        mParentalConsentFragment= new ParentalConsentFragment();
        assertNotNull(mParentalConsentFragment);
    }

    @Test
    public void testConfirmationHandler(){
        assertNotNull(mConfirmationHandler);
    }

    @Test
    public void testPrivate(){
        Method method = null;
        try {
            method =ConfirmationHandler.class.getDeclaredMethod("handleFailure");;
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
}