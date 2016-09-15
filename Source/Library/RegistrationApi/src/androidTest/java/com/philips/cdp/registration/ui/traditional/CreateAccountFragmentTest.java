package com.philips.cdp.registration.ui.traditional;

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
public class CreateAccountFragmentTest extends InstrumentationTestCase {

    CreateAccountFragment createAccountFragment;

    @Before
    public void setUp() throws Exception {
        createAccountFragment = new CreateAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(createAccountFragment);
    }
    @Test
    public void testinitUI(){
        Method method = null;
        View view = new View(getInstrumentation().getContext());
        try {
            method = CreateAccountFragment.class.getDeclaredMethod("initUI", View.class);
            method.setAccessible(true);
            method.invoke(createAccountFragment,view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}