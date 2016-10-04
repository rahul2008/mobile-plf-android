package com.philips.cdp.registration;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/1/2016.
 */
public class AppIdentityInfoTest extends InstrumentationTestCase{
    Context mContext;
    AppIdentityInfo mAppIdentityInfo;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mAppIdentityInfo = new AppIdentityInfo();
    }


    @Test
    public void testSetAppLocalizedNAme() throws Exception {
        mAppIdentityInfo.setAppLocalizedNAme("sample");
        assertEquals("sample",mAppIdentityInfo.getAppLocalizedNAme());

    }

    @Test
    public void testSetSector() throws Exception {
        mAppIdentityInfo.setSector("sample");
        assertEquals("sample",mAppIdentityInfo.getSector());

    }

    @Test
    public void testSetMicrositeId() throws Exception {
        mAppIdentityInfo.setMicrositeId("sample");
        assertEquals("sample",mAppIdentityInfo.getMicrositeId());
    }

    @Test
    public void testSetAppName() throws Exception {
        mAppIdentityInfo.setAppName("sample");
        assertEquals("sample",mAppIdentityInfo.getAppName());
    }

    @Test
    public void testSetAppState() throws Exception {

        mAppIdentityInfo.setAppState("sample");
        assertEquals("sample",mAppIdentityInfo.getAppState());
    }

    @Test
    public void testSetAppVersion() throws Exception {

        mAppIdentityInfo.setAppVersion("sample");
        assertEquals("sample",mAppIdentityInfo.getAppVersion());
    }

    @Test
    public void testSetServiceDiscoveryEnvironment() throws Exception {

        mAppIdentityInfo.setServiceDiscoveryEnvironment("sample");
        assertEquals("sample",mAppIdentityInfo.getServiceDiscoveryEnvironment());

    }
}