package com.philips.cdp.registration.coppa.base;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class CoppaConsentUpdaterTest extends InstrumentationTestCase {
    CoppaConsentUpdater mCoppaConsentUpdater;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        mCoppaConsentUpdater = new CoppaConsentUpdater(mContext);
        assertNotNull(mCoppaConsentUpdater);
        mContext = getInstrumentation().getTargetContext();

    }

    @Test
    public void testUpdateCoppaConsentStatus() {
        assertNotNull(mContext);
    }
    @Test
    public void testBuildConsentStatus(){
        Method method = null;
        boolean coppaConsentStatus=true;
        JSONObject consentsObject= new JSONObject();
        try {
            method =CoppaConsentUpdater.class.getDeclaredMethod("buildConsentStatus",Boolean.class,JSONObject.class);;
            method.setAccessible(true);
            method.invoke(mCoppaConsentUpdater,coppaConsentStatus,consentsObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testBuildConsentConfirmation(){
        Method method = null;
        boolean coppaConsentConfirmationStatus=true;
        JSONObject consentsObject= new JSONObject();
        try {
            method =CoppaConsentUpdater.class.getDeclaredMethod("buildConsentConfirmation",Boolean.class,JSONObject.class);;
            method.setAccessible(true);
            method.invoke(mCoppaConsentUpdater,coppaConsentConfirmationStatus,consentsObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}