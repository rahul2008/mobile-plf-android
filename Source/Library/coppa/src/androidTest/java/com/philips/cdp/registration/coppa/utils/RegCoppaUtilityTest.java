package com.philips.cdp.registration.coppa.utils;

import android.test.InstrumentationTestCase;
import android.text.SpannableString;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegCoppaUtilityTest extends InstrumentationTestCase{

    RegCoppaUtility regCoppaUtility;
    @Before
    public void setUp() throws Exception {regCoppaUtility= new RegCoppaUtility();
    }
    @Test
    public void testReg(){
        assertNotNull(regCoppaUtility);
    }
    @Test
    public void testPrivate(){
        Method method = null;
        SpannableString spannableString;
        CharSequence charSequence;
        charSequence= new CharSequence() {
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

        spannableString= new SpannableString(charSequence);
        try {
            method =RegCoppaUtility.class.getDeclaredMethod("removeUnderlineFromLink",SpannableString.class);;
            method.setAccessible(true);
            method.invoke(regCoppaUtility, spannableString);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}