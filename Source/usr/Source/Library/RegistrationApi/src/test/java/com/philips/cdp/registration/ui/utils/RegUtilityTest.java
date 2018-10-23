package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.text.SpannableString;

import com.philips.cdp.registration.configuration.Configuration;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.class)
public class RegUtilityTest extends TestCase {

    private RegUtility regUtility;

    @Mock
    private Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        regUtility = new RegUtility();

    }

    @Test
    public void testGetConfiguration() {
        assertEquals(Configuration.EVALUATION, RegUtility.getConfiguration(null));
        assertEquals(Configuration.EVALUATION, RegUtility.getConfiguration("sample"));
        assertEquals(Configuration.PRODUCTION, RegUtility.getConfiguration(Configuration.PRODUCTION.getValue()));
        assertEquals(Configuration.TESTING, RegUtility.getConfiguration(Configuration.TESTING.getValue()));
        assertEquals(Configuration.DEVELOPMENT, RegUtility.getConfiguration(Configuration.DEVELOPMENT.getValue()));
        assertEquals(Configuration.STAGING, RegUtility.getConfiguration(Configuration.STAGING.getValue()));
        assertEquals(Configuration.EVALUATION, RegUtility.getConfiguration(Configuration.EVALUATION.getValue()));
    }

    @Test
    public void testGetCreateAccountStartTime() {
        RegUtility.setCreateAccountStartTime(1234567890);
        assertEquals(1234567890, RegUtility.getCreateAccountStartTime());
    }

    @Test
    public void testCheckIsValidSignInProviders() {
        RegUtility.checkIsValidSignInProviders(null);
    }

    @Test
    public void RemoveUnderlineFromLink() {
        Method method = null;
        CharSequence source = new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(final int index) {
                return 0;
            }

            @Override
            public CharSequence subSequence(final int start, final int end) {
                return null;
            }
        };
        SpannableString spanableString = new SpannableString(source);
        try {
            method = RegUtility.class.getDeclaredMethod("removeUnderlineFromLink", SpannableString.class);
            method.setAccessible(true);
            method.invoke(regUtility, spanableString);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}