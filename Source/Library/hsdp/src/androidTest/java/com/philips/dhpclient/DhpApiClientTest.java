
package com.philips.dhpclient;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.response.DhpResponseVerifier;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiClientTest extends InstrumentationTestCase{
    DhpApiClient mDhpApiClient;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
                mDhpApiClient = new DhpApiClient(dhpApiClientConfiguration);
    }

    public void testDhpApiClient(){
        assertNotNull(mDhpApiClient);
//        mDhpApiClient.UTCDatetimeAsString();
        DhpResponseVerifier dhpResponseVerifier = new DhpResponseVerifier() {
            @Override
            public void verify(DhpResponse dhpResponse) {

            }
        };
        mDhpApiClient.setResponseVerifier(dhpResponseVerifier);
        assertNotNull(mDhpApiClient);


    }

    public void testSendSignedRequest(){
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        try {
            method = DhpApiClient.class.getDeclaredMethod("sendSignedRequest", new Class[]{String.class, String.class, String.class, HashMap.class, Object.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{s,s,s,headers,d});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testSendSignedRequestForSocialLogin(){
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        try {
            method = DhpApiClient.class.getDeclaredMethod("sendSignedRequestForSocialLogin", new Class[]{String.class, String.class, String.class, HashMap.class, Object.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{s,s,s,headers,d});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testAddSignedDateHeader(){
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        try {
            method = DhpApiClient.class.getDeclaredMethod("addSignedDateHeader", Map.class);
            method.setAccessible(true);
            method.invoke(mDhpApiClient,headers);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testSendRestRequest1(){
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        try {
            method = DhpApiClient.class.getDeclaredMethod("sendRestRequest", new Class[]{String.class, String.class, String.class, Map.class, Object.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{s,s,s,headers,d});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testSendRestRequest() throws URISyntaxException {
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?"+s);


        try {
            method = DhpApiClient.class.getDeclaredMethod("sendRestRequest", new Class[]{String.class, URI.class, Map.class, Object.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{s,uri,headers,d});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testEstablishConnection() throws URISyntaxException {
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?"+s);

        try {
            method = DhpApiClient.class.getDeclaredMethod("establishConnection", new Class[]{ URI.class,String.class, Map.class, Object.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{uri,s,headers,d});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testAddRequestBody(){
        Method method = null;
        String s= "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?"+s);
        HttpURLConnection urlConnection=null;
        try {
            urlConnection =  (HttpURLConnection) uri.toURL().openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            method = DhpApiClient.class.getDeclaredMethod("addRequestBody", new Class[]{ String.class,HttpURLConnection.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{s,urlConnection});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){}
    }

    public void testOpenHttpURLConnection(){
        Method method = null;
        String s= "sample";
        URI uri = URI.create(s + s + "?"+s);
        try {
            method = DhpApiClient.class.getDeclaredMethod("openHttpURLConnection", new Class[]{URI.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{uri});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){

        }
    }


    public void testAddRequestHeaders() {
        Method method = null;
        String s= "sample";
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?"+s);
        HttpURLConnection urlConnection=null;
        try {
            urlConnection =  (HttpURLConnection) uri.toURL().openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            method = DhpApiClient.class.getDeclaredMethod("addRequestHeaders", new Class[]{ Map.class,HttpURLConnection.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{headers,urlConnection});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){

        }
    }

    public void testSign()  {
        Method method = null;
        String s= "sample";
        Map<String, String> headers = new HashMap<>();

        URI uri = URI.create(s + s + "?"+s);
        try {
            method = DhpApiClient.class.getDeclaredMethod("sign", new Class[]{Map.class, String.class, String.class, String.class, String.class});
            method.setAccessible(true);
            method.invoke(mDhpApiClient, new  Object[]{headers,s,s,s,s});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e){

        }
    }

}
