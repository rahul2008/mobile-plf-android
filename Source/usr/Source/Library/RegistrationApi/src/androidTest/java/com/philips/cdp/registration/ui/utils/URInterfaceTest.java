package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class URInterfaceTest extends RegistrationApiInstrumentationBase {
    URInterface urInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        urInterface = new URInterface();
    }

    @Test
    public void testAssert() {
        assertNotNull(urInterface);
    }

    @Test
    public void testLaunchAsActivity() {
        Method method = null;
        ActivityLauncher.ActivityOrientation mScreenOrientation = null;
        ActivityLauncher uiLauncher = new ActivityLauncher(mScreenOrientation, 0);
        UappLaunchInput uappLaunchInput = new UappLaunchInput();
        try {
            method = URInterface.class.getDeclaredMethod("launchAsActivity", ActivityLauncher.class, UappLaunchInput.class);
            ;
            method.setAccessible(true);
            method.invoke(urInterface, uiLauncher, uappLaunchInput);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLaunchAsFragment() {
        Method method = null;
        ActivityLauncher.ActivityOrientation mScreenOrientation = null;
        ActivityLauncher uiLauncher = new ActivityLauncher(mScreenOrientation, 0);
        UappLaunchInput uappLaunchInput = new UappLaunchInput();
        try {
            method = URInterface.class.getDeclaredMethod("launchAsFragment", ActivityLauncher.class, UappLaunchInput.class);
            method.setAccessible(true);
            method.invoke(urInterface, uiLauncher, uappLaunchInput);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLaunch() {
        UiLauncher uiLauncher = new UiLauncher() {
            @Override
            public int getEnterAnimation() {
                return super.getEnterAnimation();
            }
        };
        UappLaunchInput uappLaunchInput = new UappLaunchInput();
        assertNotNull(uiLauncher);
        assertNotNull(uappLaunchInput);
    }
}