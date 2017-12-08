package com.philips.cdp.registration.coppa.ui.fragment;

import android.view.View;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ParentalCaringSharingFragmentTest extends RegistrationApiInstrumentationBase {

    ParentalCaringSharingFragment parentalCaringSharingFragment;

    @Before
    public void setUp() throws Exception {
       super.setUp();
        parentalCaringSharingFragment= new ParentalCaringSharingFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(parentalCaringSharingFragment);
    }
    @Test
    public void testInitUi() {
        Method method = null;
        View view = new View(getInstrumentation().getContext());
        try {
            method = ParentalCaringSharingFragment.class.getDeclaredMethod("initUi", View.class);
            method.setAccessible(true);
            method.invoke(parentalCaringSharingFragment, view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetText(){
        Method method = null;
        try {
            method = ParentalCaringSharingFragment.class.getDeclaredMethod("getUsText");
            method.setAccessible(true);
            method.invoke(parentalCaringSharingFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetAlreadyUsText(){
        Method method = null;
        try {
            method = ParentalCaringSharingFragment.class.getDeclaredMethod("getAlreadyUsText");
            method.setAccessible(true);
            method.invoke(parentalCaringSharingFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}