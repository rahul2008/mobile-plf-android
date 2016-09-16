package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.widget.TextView;

import com.philips.cdp.registration.configuration.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/17/2016.
 */
public class RegUtilityTest extends InstrumentationTestCase {

    @Mock
    RegUtility regUtility;

    @Mock
    Context context;

    @Mock
    private static long createAccountStartTime;

    @Mock
    TextView termsAndConditionsAcceptance;
    @Mock
    Activity activity;
    @Mock
    ClickableSpan termsAndConditionClickListener;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        regUtility = new RegUtility();
        context = getInstrumentation().getTargetContext();
    }

    @Test
    public void testGetCheckBoxPadding() throws Exception {
        assertNotNull(regUtility.getCheckBoxPadding(context));
    }

   /* @Test
    public void testLinkifyTermsandCondition() throws Exception {
        Mockito.doNothing().when(regUtility).linkifyTermsandCondition(
                termsAndConditionsAcceptance,
                activity, termsAndConditionClickListener);
    }

    @Test
    public void testLinkifyPhilipsNews() throws Exception {

    }

    @Test
    public void testLinkifyAccountSettingPhilips() throws Exception {

    }

    @Test
    public void testHandleTermsCondition() throws Exception {

    }*/

    @Test
    public void testGetConfiguration() throws Exception {
        assertEquals(Configuration.EVALUATION, regUtility.getConfiguration(null));
        assertEquals(Configuration.EVALUATION, regUtility.getConfiguration("sample"));
        assertEquals(Configuration.PRODUCTION, regUtility.getConfiguration(Configuration.PRODUCTION.getValue()));
        assertEquals(Configuration.TESTING, regUtility.getConfiguration(Configuration.TESTING.getValue()));
        assertEquals(Configuration.DEVELOPMENT, regUtility.getConfiguration(Configuration.DEVELOPMENT.getValue()));
        assertEquals(Configuration.STAGING, regUtility.getConfiguration(Configuration.STAGING.getValue()));
        assertEquals(Configuration.EVALUATION, regUtility.getConfiguration(Configuration.EVALUATION.getValue()));
    }

    //
//    @Test
//    public void testCheckIsValidSignInProviders() throws Exception {
//
//    }
//
//    @Test
//    @Before
//    public void testSetCreateAccountStartTime() throws Exception {
//    }

    @Test
    public void testGetCreateAccountStartTime() throws Exception {
        regUtility.setCreateAccountStartTime(1234567890);
        assertEquals(1234567890, regUtility.getCreateAccountStartTime());
    }

    @Test
    public void testCheckIsValidSignInProviders() throws Exception {
        regUtility.checkIsValidSignInProviders(null);
    }

    @Test
    public void testRemoveUnderlineFromLink() {
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
            ;
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