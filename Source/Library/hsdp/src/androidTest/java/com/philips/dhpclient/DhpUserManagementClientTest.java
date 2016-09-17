package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import com.philips.dhpclient.request.DhpUserIdentity;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpUserManagementClientTest extends InstrumentationTestCase{
    DhpApiClientConfiguration mDhpApiClientConfiguration;
    DhpUserManagementClient mDhpUserManagementClient;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        mDhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
        mDhpUserManagementClient = new DhpUserManagementClient(mDhpApiClientConfiguration);

    }

    @Test
    public void testDhpUserManagementClient(){
        DhpUserIdentity.Address primaryAddress = new DhpUserIdentity.Address("country");
        List<DhpUserIdentity.Photo> photos = new ArrayList<DhpUserIdentity.Photo>();
        double d = 123;

        DhpUserIdentity.Profile profile = new DhpUserIdentity.Profile("givenName", "middleName", "familyName", "birthday", "currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",d,d, primaryAddress, photos);
        DhpUserIdentity dhpUserIdentity = new DhpUserIdentity("loginId","password",profile);
       try{ mDhpUserManagementClient.registerUser(dhpUserIdentity);}
       catch(Exception e){}

        try {
        mDhpUserManagementClient.retrieveProfile("sample","sample");
        }catch(Exception e){}
        mDhpUserManagementClient.changePassword("loginId", "currentPassword"," newPassword","accessToken");
        try {
        mDhpUserManagementClient.resetPassword("loginId");
        }catch(Exception e){}
        mDhpUserManagementClient.updateProfile("userId", profile,"accessToken");
        try {
       mDhpUserManagementClient.resendConfirmation("email") ;
        }catch(Exception e){}
        assertNotNull(mDhpApiClientConfiguration);
        assertNotNull(mDhpUserManagementClient);

    }

    public void testGetUserInstance(){
        Method method = null;
        Double doubleVal = 234.5;
        try {
            method = DhpUserManagementClient.class.getDeclaredMethod("remapZeroOrNegativeToNull", Double.class);
            method.setAccessible(true);
            method.invoke(mDhpUserManagementClient,doubleVal);
            method.invoke(mDhpUserManagementClient,-0.1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
//
//    private List<DhpUserIdentity.Photo> getPhotos(Map<String, Object> responseMap) {
//        List<Map<String, String>> rawPhotos = MapUtils.extract(responseMap, "exchange.user.profile.photos");
//
//        if (rawPhotos == null)
//            return null;
//
//        List<DhpUserIdentity.Photo> photos = new ArrayList<>();
//
//        for (Map<String, String> photoMap : rawPhotos)
//            photos.add(new DhpUserIdentity.Photo(photoMap.get("type"), photoMap.get("value")));
//
//        return photos;
//    }
public void testGetPhotos(){
    Method method = null;
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put("jpg","base64");
    try {
        method = DhpUserManagementClient.class.getDeclaredMethod("getPhotos", Map.class);
        method.setAccessible(true);
        method.invoke(mDhpUserManagementClient,responseMap);
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }
}
}