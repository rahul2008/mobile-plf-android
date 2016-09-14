package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310238655 on 8/11/2016.
 */
public class ServiceDiscoveryModelTest extends MockitoTestCase {


    private Context context;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    ServiceDiscovery mServiceDiscoveryModel = null;
    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mServiceDiscoveryModel = new ServiceDiscovery();
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mServiceDiscoveryModel);
    }

    public void testsetSuccess() {

        mServiceDiscoveryModel.setSuccess(false);
        assertFalse(mServiceDiscoveryModel.isSuccess());
    }

    public void testsetHttpStatus() {
        mServiceDiscoveryModel.setHttpStatus(null);
        assertNull(mServiceDiscoveryModel.httpStatus);
        mServiceDiscoveryModel.setHttpStatus("TestHttpStatus");
        assertSame("TestHttpStatus", mServiceDiscoveryModel.getHttpStatus());

    }

    public void testsetCountry() {
        mServiceDiscoveryModel.setCountry(null);
        assertNull(mServiceDiscoveryModel.country);
        mServiceDiscoveryModel.setCountry("TestCountry");
        assertSame("TestCountry", mServiceDiscoveryModel.getCountry());
    }

    public void testsetError() {

//        Error error = new Error();
        ServiceDiscovery.Error error= new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
        error.setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT);
        assertNotNull(error.getErrorvalue());
        error.setMessage("Test");
        assertNotNull(error.getMessage());


        mServiceDiscoveryModel.setError(error);
        assertNotNull(mServiceDiscoveryModel.getError());
        assertNotNull(mServiceDiscoveryModel.error);
    }

    public void testisSuccess() {
        mServiceDiscoveryModel.setSuccess(true);
        assertTrue(mServiceDiscoveryModel.isSuccess());
    }

    public void testsetMatchByCountry() {
        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mServiceDiscoveryModel.setMatchByCountry(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryModel.getMatchByCountry());
    }

    public void testsetMatchByLanguage() {
        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mServiceDiscoveryModel.setMatchByLanguage(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryModel.getMatchByLanguage());
    }

    public void testgetHttpStatus() {
        mServiceDiscoveryModel.setHttpStatus("testSet");
        assertNotNull(mServiceDiscoveryModel.getHttpStatus());
    }

    public void testgetMatchByCountry() {

        mServiceDiscoveryModel.setMatchByCountry(commonMatchByCountryOrLanguage());
        assertNotNull(mServiceDiscoveryModel.getMatchByCountry());
    }

    public void testgetMatchByLanguage() {
        mServiceDiscoveryModel.setMatchByLanguage(commonMatchByCountryOrLanguage());
        assertNotNull(mServiceDiscoveryModel.getMatchByLanguage());
    }

    public MatchByCountryOrLanguage commonMatchByCountryOrLanguage() {

        MatchByCountryOrLanguage.Config.Tag mTag = new MatchByCountryOrLanguage.Config.Tag();
        mTag.setId("TestTagId");
        mTag.setName("TestTagName");
        mTag.setKey("TestTagKey");

        assertNotNull(mTag.getId());
        assertNotNull(mTag.getKey());
        assertNotNull(mTag.getName());

        ArrayList mTagArray = new ArrayList();
        mTagArray.add(mTag);

        HashMap mMap = new HashMap<String, String>();
        mMap.put("TestMapKey", "TestMapValue");


        MatchByCountryOrLanguage.Config mconfig= new MatchByCountryOrLanguage.Config();
        mconfig.setMicrositeId("TestMicrositeId");
        mconfig.setTags(mTagArray);
        mconfig.setUrls(mMap);

        assertNotNull(mconfig.getMicrositeId());
        assertNotNull(mconfig.getTags());
        assertNotNull(mconfig.getUrls());

        ArrayList mConfigArray = new ArrayList();
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

    public void testgetCountry() {
        mServiceDiscoveryModel.setCountry("testCountry");
        assertNotNull(mServiceDiscoveryModel.getCountry());
    }



}
