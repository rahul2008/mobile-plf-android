package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationHelper;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationActivityTest extends InstrumentationTestCase {

    RegistrationActivity registrationActivity;
    @Before
    public void setUp() throws Exception {
        registrationActivity= new RegistrationActivity();
    }
    @Test
    public void testAssert(){
        assertNotNull(registrationActivity);
    }

    @Test
    public void testSetCustomLocale(){
        Method method = null;
        String provider="en_US";
        try {
            method =RegistrationActivity.class.getDeclaredMethod("setCustomLocale");;
            method.setAccessible(true);
            method.invoke(registrationActivity, RegistrationHelper.getInstance().getLocale(getInstrumentation().getContext()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}