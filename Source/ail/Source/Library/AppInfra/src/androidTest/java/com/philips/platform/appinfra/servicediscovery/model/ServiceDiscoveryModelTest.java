/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


/**
 * ServiceDiscovery Model Test class.
 */
public class ServiceDiscoveryModelTest {

    private ServiceDiscovery mServiceDiscoveryModel = null;

    @Before
    protected void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        ServiceDiscoveryInterface mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        ServiceDiscoveryManager mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mServiceDiscoveryModel = new ServiceDiscovery(mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mServiceDiscoveryModel);
    }

    @Test
    public void testsetSuccess() {
        mServiceDiscoveryModel.setSuccess(false);
        assertFalse(mServiceDiscoveryModel.isSuccess());
    }

    @Test
    public void testsetHttpStatus() {
        mServiceDiscoveryModel.setHttpStatus(null);
        assertNull(mServiceDiscoveryModel.httpStatus);
        mServiceDiscoveryModel.setHttpStatus("TestHttpStatus");
        assertSame("TestHttpStatus", mServiceDiscoveryModel.getHttpStatus());
    }

    @Test
    public void testsetCountry() {
        mServiceDiscoveryModel.setCountry(null);
        assertNull(mServiceDiscoveryModel.country);
        mServiceDiscoveryModel.setCountry("TestCountry");
        assertSame("TestCountry", mServiceDiscoveryModel.getCountry());
    }

    @Test
    public void testsetError() {
        ServiceDiscovery.Error error = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
        error.setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT);
        assertNotNull(error.getErrorvalue());
        error.setMessage("Test");
        assertNotNull(error.getMessage());
        mServiceDiscoveryModel.setError(error);
        assertNotNull(mServiceDiscoveryModel.getError());
        assertNotNull(mServiceDiscoveryModel.error);
    }

    @Test
    public void testisSuccess() {
        mServiceDiscoveryModel.setSuccess(true);
        assertTrue(mServiceDiscoveryModel.isSuccess());
    }

    @Test
    public void testsetMatchByCountry() {
        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mServiceDiscoveryModel.setMatchByCountry(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryModel.getMatchByCountry());
    }

    @Test
    public void testsetMatchByLanguage() {
        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mServiceDiscoveryModel.setMatchByLanguage(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryModel.getMatchByLanguage());
    }

    @Test
    public void testgetHttpStatus() {
        mServiceDiscoveryModel.setHttpStatus("testSet");
        assertNotNull(mServiceDiscoveryModel.getHttpStatus());
    }

    @Test
    public void testgetMatchByCountry() {
        mServiceDiscoveryModel.setMatchByCountry(commonMatchByCountryOrLanguage());
        assertNotNull(mServiceDiscoveryModel.getMatchByCountry());
    }

    @Test
    public void testgetMatchByLanguage() {
        mServiceDiscoveryModel.setMatchByLanguage(commonMatchByCountryOrLanguage());
        assertNotNull(mServiceDiscoveryModel.getMatchByLanguage());
    }

    @Test
    public void testgetCountry() {
        mServiceDiscoveryModel.setCountry("testCountry");
        assertNotNull(mServiceDiscoveryModel.getCountry());
    }

    private MatchByCountryOrLanguage commonMatchByCountryOrLanguage() {
        MatchByCountryOrLanguage.Config.Tag mTag = new MatchByCountryOrLanguage.Config.Tag();
        mTag.setId("TestTagId");
        mTag.setName("TestTagName");
        mTag.setKey("TestTagKey");

        assertNotNull(mTag.getId());
        assertNotNull(mTag.getKey());
        assertNotNull(mTag.getName());

        ArrayList<MatchByCountryOrLanguage.Config.Tag> mTagArray = new ArrayList<MatchByCountryOrLanguage.Config.Tag>();
        mTagArray.add(mTag);

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("TestMapKey", "TestMapValue");

        MatchByCountryOrLanguage.Config mconfig = new MatchByCountryOrLanguage.Config();
        mconfig.setMicrositeId("TestMicrositeId");
        mconfig.setTags(mTagArray);
        mconfig.setUrls(mMap);

        assertNotNull(mconfig.getMicrositeId());
        assertNotNull(mconfig.getTags());
        assertNotNull(mconfig.getUrls());

        ArrayList<MatchByCountryOrLanguage.Config> mConfigArray = new ArrayList<MatchByCountryOrLanguage.Config>();
        mConfigArray.add(mconfig);

        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mMatchByCountryOrLanguage.setAvailable(false);
        mMatchByCountryOrLanguage.setConfigs(mConfigArray);

        assertNotNull(mMatchByCountryOrLanguage.getLocale());
        assertNotNull(mMatchByCountryOrLanguage.getConfigs());
        assertFalse(mMatchByCountryOrLanguage.isAvailable());
        return mMatchByCountryOrLanguage;
    }
}
