package com.philips.cdp.registration.coppa.ui.fragment;

import android.test.InstrumentationTestCase;
import android.view.View;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ParentalAccessFragmentTest extends InstrumentationTestCase {

    ParentalConsentFragment parentalConsentFragment;
    @Before
    public void setUp() throws Exception {
        parentalConsentFragment = new ParentalConsentFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(parentalConsentFragment);
    }
    @Test
    public void testInitUi(){
        Method method = null;
        View view = new View(getInstrumentation().getContext());
        try {
            method = ParentalConsentFragment.class.getDeclaredMethod("initUi",View.class);
            method.setAccessible(true);
            method.invoke(parentalConsentFragment, new Object[]{view});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testShowParentalAccessDailog(){
        Method method = null;
        try {
            method = ParentalConsentFragment.class.getDeclaredMethod("showParentalAccessDailog");
            method.setAccessible(true);
            method.invoke(parentalConsentFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}