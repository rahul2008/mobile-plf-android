package com.philips.cdp.registration.coppa.ui.fragment;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationCoppaFragmentTest extends InstrumentationTestCase{

    RegistrationCoppaFragment registrationCoppaFragment;

    @Before
    public void setUp() throws Exception {
        registrationCoppaFragment= new RegistrationCoppaFragment();
    }
    @Test
    public void testAcess(){
        assertNotNull(registrationCoppaFragment);
    }
    @Test
    public void testPerformReplaceWithPerentalAccess(){
            Method method = null;
            try {
                method = RegistrationCoppaFragment.class.getDeclaredMethod("performReplaceWithPerentalAccess");
                method.setAccessible(true);
                method.invoke(registrationCoppaFragment);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

    }
    @Test
    public void testAddParentalApprovalFragment(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("addParentalApprovalFragment");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testAddParentalApprovalFragmentonLaunch(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("addParentalApprovalFragmentonLaunch");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testHandleConsentState(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("handleConsentState");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
        public void testTrackPage(){
            Method method = null;
            String currPage="currentpage";
            try {
                method = RegistrationCoppaFragment.class.getDeclaredMethod("trackPage",String.class);
                method.setAccessible(true);
                method.invoke(registrationCoppaFragment,currPage);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
    }
    @Test
    public void testHandleBackStack(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("handleBackStack");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testTrackHandler(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("trackHandler");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLoadFirstFragment(){
        Method method = null;
        try {
            method = RegistrationCoppaFragment.class.getDeclaredMethod("loadFirstFragment");
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /*@Test
    public void testLaunchRegistrationFragmentOnLoggedIn(){
        Method method = null;
        boolean isAccountSettings=true;
        try {
            //not ex
            method = RegistrationCoppaFragment.class.getDeclaredMethod("launchRegistrationFragmentOnLoggedIn",Boolean.class);
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment,isAccountSettings);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testaddFragment(){

        Method method = null;
        Fragment fragment= new Fragment();
        try {
            //not ex
            method = RegistrationCoppaFragment.class.getDeclaredMethod("addFragment",Fragment.class);
            method.setAccessible(true);
            method.invoke(registrationCoppaFragment,fragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/
}