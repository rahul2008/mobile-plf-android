package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XCheckBoxTest extends InstrumentationTestCase{

    XCheckBox xCheckBox;
    @Before
    public void setUp() throws Exception {
        xCheckBox= new XCheckBox(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xCheckBox);
    }

    @Test
    public void testChangeBackGround(){
        Method method = null;
        try {
            method =XCheckBox.class.getDeclaredMethod("changeBackGround");;
            method.setAccessible(true);
            method.invoke(xCheckBox);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}