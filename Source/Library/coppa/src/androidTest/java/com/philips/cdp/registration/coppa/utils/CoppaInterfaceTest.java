package com.philips.cdp.registration.coppa.utils;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.test.InstrumentationTestCase;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CoppaInterfaceTest extends InstrumentationTestCase {

    CoppaInterface coppaInterface;
    FragmentActivity fragmentActivity;
    ActionBarListener actionBarListener;

    @Before
    public void setUp() throws Exception {
        coppaInterface = new CoppaInterface();
        fragmentActivity = new FragmentActivity();
        actionBarListener = new ActionBarListener() {
            @Override
            public void updateActionBar(@StringRes final int i, final boolean b) {

            }

            @Override
            public void updateActionBar(final String s, final boolean b) {

            }
        };
    }

    @Test
    public void testAssert() {
        assertNotNull(coppaInterface);
    }

    @Test
    public void testLaunchAsFragment() {
        Method method = null;
        FragmentLauncher fragmentLauncher = new FragmentLauncher(fragmentActivity, 1, actionBarListener);
        UappLaunchInput uappLaunchInput = new UappLaunchInput();
        try {
            method = CoppaInterface.class.getDeclaredMethod("launchAsFragment", new Class[]{FragmentLauncher.class, UappLaunchInput.class});
            method.setAccessible(true);
            method.invoke(coppaInterface, new Object[]{fragmentLauncher, uappLaunchInput});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /*@Test
    public void testLaunchAsActivity() {
        Method method = null;
        FragmentLauncher fragmentLauncher = new FragmentLauncher(fragmentActivity, 1, actionBarListener);
        UappLaunchInput uappLaunchInput = new UappLaunchInput();
        try {
            method = CoppaInterface.class.getDeclaredMethod("launchAsActivity", new Class[]{FragmentLauncher.class, UappLaunchInput.class});
            method.setAccessible(true);
            method.invoke(coppaInterface, new Object[]{fragmentLauncher, uappLaunchInput});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/
}