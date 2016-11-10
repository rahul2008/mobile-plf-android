package com.philips.cdp.security;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Test;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class SecureStorageTest extends InstrumentationTestCase{
    SecureStorage mSecureStorage ;

    Context context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    mSecureStorage = new SecureStorage();
        context = getInstrumentation().getTargetContext();
    }

    @Test
    public void testGetErrorCode() throws Exception {
        mSecureStorage.init(context);

        mSecureStorage.generateSecretKey();
     //   assertNotNull(mSecureStorage.encrypt("hello"));
//        mSecureStorage.migrateUserData("hello");
        assertNull(mSecureStorage.stringToObject("hello"));
        byte[] a = new byte[4];
        a[0]=10;
        a[1]=10;
        a[2]=10;
        a[3]=10;
        assertNull(mSecureStorage.decrypt(a));
        assertNotNull(mSecureStorage.objectToString(null));


    }
}