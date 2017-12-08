package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.settings.RegistrationHelper;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationActivityTest extends RegistrationApiInstrumentationBase {

    RegistrationActivity registrationActivity;
    @Before
    public void setUp() throws Exception {
       super.setUp();
        registrationActivity= new RegistrationActivity();
    }

    public void testAssert(){
        assertNotNull(registrationActivity);
    }


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