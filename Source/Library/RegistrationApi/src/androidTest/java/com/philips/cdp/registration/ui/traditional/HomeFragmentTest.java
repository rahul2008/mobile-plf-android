package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;
import android.text.SpannableString;
import android.widget.LinearLayout;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HomeFragmentTest extends InstrumentationTestCase {

    HomeFragment homeFragment;
    @Mock
    LinearLayout mLlSocialProviderBtnContainer;

    @Before
    public void setUp() throws Exception {
        homeFragment = new HomeFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(homeFragment);
    }
    @Test
    public void testHandleSocialProviders(){
        Method method = null;
        String countryCode="en_US";
        try {
            synchronized(this){//synchronized block

                try{
                    RegistrationHelper.getInstance().
                            setAppInfraInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));
                    RLog.initForTesting(getInstrumentation().getContext());
                }catch(Exception e){System.out.println(e);}
            }
            method =HomeFragment.class.getDeclaredMethod("handleSocialProviders",String.class);;
            method.setAccessible(true);
            method.invoke(homeFragment,countryCode);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testInflateEachProviderBtn(){
        Method method = null;
        String provider="en_US";
        try {
            method =HomeFragment.class.getDeclaredMethod("inflateEachProviderBtn",String.class);;
            method.setAccessible(true);
            method.invoke(homeFragment,provider);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testRemoveUnderlineFromLink(){
        Method method = null;
        CharSequence source=new CharSequence() {
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
        SpannableString spanableString=new SpannableString(source);
        try {
            method =HomeFragment.class.getDeclaredMethod("removeUnderlineFromLink",SpannableString.class);;
            method.setAccessible(true);
            method.invoke(homeFragment,spanableString);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testTrackSocialProviderPage(){
        /*Method method = null;
        String mProvider="test";
        try {
            method =HomeFragment.class.getDeclaredMethod("trackSocialProviderPage");;
            method.setAccessible(true);
            method.invoke(homeFragment,mProvider);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

    }
}