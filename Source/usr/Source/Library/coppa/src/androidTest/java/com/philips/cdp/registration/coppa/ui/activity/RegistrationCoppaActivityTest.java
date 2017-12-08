package com.philips.cdp.registration.coppa.ui.activity;

import android.content.pm.ActivityInfo;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationCoppaActivityTest extends RegistrationApiInstrumentationBase {

    RegistrationCoppaActivity mRegistrationCoppaActivity;

    @Before
    public void setUp() throws Exception {
     super.setUp();
        mRegistrationCoppaActivity = new RegistrationCoppaActivity();
    }


    public void testRegistrationcopp() {
        assertNotNull(mRegistrationCoppaActivity);
    }


    public void testSetOrientation() {
        Method method = null;
        try {
            method = RegistrationCoppaActivity.class.getDeclaredMethod("setOrientation", int.class);
            method.setAccessible(true);
            method.invoke(mRegistrationCoppaActivity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            method.invoke(mRegistrationCoppaActivity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            method.invoke(mRegistrationCoppaActivity, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            method.invoke(mRegistrationCoppaActivity, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testInitUi() {
        Method method = null;
        try {
            method = RegistrationCoppaActivity.class.getDeclaredMethod("initUi");
            ;
            method.setAccessible(true);
            method.invoke(mRegistrationCoppaActivity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testWriteDataToFile() {
        Method method = null;
        Boolean is = true;
        try {
            method = RegistrationCoppaActivity.class.getDeclaredMethod("launchRegistrationFragment", new Class[]{Boolean.class, Boolean.class});
            ;
            method.setAccessible(true);
            method.invoke(mRegistrationCoppaActivity, new Object[]{is, is});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}