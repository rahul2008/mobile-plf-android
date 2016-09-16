package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiSignerTest  extends InstrumentationTestCase {
    DhpApiSigner mDhpApiSigner;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        try {
            mDhpApiSigner = new DhpApiSigner(null, null);
        } catch ( IllegalArgumentException e){

        }
        mDhpApiSigner = new DhpApiSigner("sample","sample");
    }

    public void testDhpApiClient(){
        assertNotNull(mDhpApiSigner);
        Map<String, String> headers = new HashMap<String,String>();
        assertNotNull(mDhpApiSigner.buildAuthorizationHeaderValue("requestMethod","queryString",headers,"dhpUrl","requestbody"));
    }


    public void testJoinHeaders(){
            Method method = null;
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("key","value");
        try {
                method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", Map.class);
                method.setAccessible(true);
                method.invoke(mDhpApiSigner,headers);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

    }


    public void testJoinHeaders2(){
        Method method = null;
        List<String>  headers = new ArrayList<String>();
        headers.add("value");
        try {
            method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", List.class);
            method.setAccessible(true);
            method.invoke(mDhpApiSigner,headers);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testBuildAuthorizationHeaderValue(){
        StringBuilder requestHeader = new StringBuilder();
        requestHeader.append(";");
        requestHeader.append("Credential:");
        requestHeader.append("sharedKey");
        requestHeader.append(";");
        requestHeader.append("SignedHeaders:");
        String header = requestHeader.toString();
        String signature = "signature";
        Method method = null;
        try {
            method = DhpApiSigner.class.getDeclaredMethod("buildAuthorizationHeaderValue", new Class[]{String.class, String.class});
            method.setAccessible(true);
            method.invoke(mDhpApiSigner,new Object[]{ header,signature});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testHash(){
        Method method = null;
        String data= "data";
        byte[] key= new byte[1];
        key[0]=-1;

        try {
            method = DhpApiSigner.class.getDeclaredMethod("hash", new Class[]{String.class, byte[].class});
            method.setAccessible(true);
            method.invoke(mDhpApiSigner,new Object[]{data,key});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}