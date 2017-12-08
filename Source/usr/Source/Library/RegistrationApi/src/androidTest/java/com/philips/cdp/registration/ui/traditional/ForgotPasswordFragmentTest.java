package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.ui.utils.RLog;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ForgotPasswordFragmentTest extends RegistrationApiInstrumentationBase {

    ForgotPasswordFragment forgotPasswordFragment;

    @Before
    public void setUp() throws Exception {
      super.setUp();
        forgotPasswordFragment= new ForgotPasswordFragment();
    }
    @Test
    public void testAsste(){
        assertNotNull(forgotPasswordFragment);
    }
    @Test
    public void testHandleSendForgotPasswordSuccess(){
        Method method = null;
       try {
            synchronized(this){//synchronized block

                try{
                    RLog.init();
                }catch(Exception ignored){}
            }
            method =ForgotPasswordFragment.class.getDeclaredMethod("handleSendForgotPasswordSuccess");;
            method.setAccessible(true);
            method.invoke(forgotPasswordFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}