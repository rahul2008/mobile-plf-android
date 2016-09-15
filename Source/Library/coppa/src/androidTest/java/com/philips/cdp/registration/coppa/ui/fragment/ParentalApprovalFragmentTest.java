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
public class ParentalApprovalFragmentTest extends InstrumentationTestCase{

    ParentalApprovalFragment parentalApprovalFragment;

    @Before
    public void setUp() throws Exception {
        parentalApprovalFragment= new ParentalApprovalFragment();

    }
    @Test
    public void testAssert(){
        assertNotNull(parentalApprovalFragment);
    }
    @Test
    public void testCheckApprovalStatus(){
        Method method = null;
        try {
            method = ParentalApprovalFragment.class.getDeclaredMethod("checkApprovalStatus");
            method.setAccessible(true);
            method.invoke(parentalApprovalFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testInitUi(){
        Method method = null;
        View view = new View(getInstrumentation().getContext());
        try {
            method = ParentalApprovalFragment.class.getDeclaredMethod("initUi", View.class);
            method.setAccessible(true);
            method.invoke(parentalApprovalFragment,view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetUsText(){
        Method method = null;
        try {
            method = ParentalApprovalFragment.class.getDeclaredMethod("getUsText");
            method.setAccessible(true);
            method.invoke(parentalApprovalFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetNonUsText(){
        Method method = null;
        try {
            method = ParentalApprovalFragment.class.getDeclaredMethod("getNonUsText");
            method.setAccessible(true);
            method.invoke(parentalApprovalFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /*@Test
    public void testHandleUiState(){
        Method method = null;
        try {
            method = ParentalApprovalFragment.class.getDeclaredMethod("handleUiState");
            method.setAccessible(true);
            method.invoke(parentalApprovalFragment);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/
}