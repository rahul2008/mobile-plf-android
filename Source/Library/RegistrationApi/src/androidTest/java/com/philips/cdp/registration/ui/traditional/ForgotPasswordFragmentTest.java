package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ForgotPasswordFragmentTest extends InstrumentationTestCase{

    ForgotPasswordFragment forgotPasswordFragment;

    @Before
    public void setUp() throws Exception {
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
                    RegistrationHelper.getInstance().
                            setAppInfraInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));
                    RLog.initForTesting(getInstrumentation().getContext());
                }catch(Exception e){System.out.println(e);}
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