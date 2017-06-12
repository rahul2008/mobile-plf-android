//package com.philips.dhpclient;
//
//
//
//import org.junit.Before;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static junit.framework.Assert.assertNotNull;
//import org.junit.Before;
//import org.junit.Test;
//
//public class DhpApiSignerTest  extends HSDPInstrumentationBase {
//    DhpApiSigner mDhpApiSigner;
//
//    @Before
//    public void setUp() throws Exception {
//            super.setUp();
//        try {
//            mDhpApiSigner = new DhpApiSigner(null, null);
//        } catch ( IllegalArgumentException e){
//
//        }
//        mDhpApiSigner = new DhpApiSigner("sample","sample");
//    }
//
//    public void testDhpApiClient(){
//        assertNotNull(mDhpApiSigner);
//        Map<String, String> headers = new HashMap<String,String>();
//        assertNotNull(mDhpApiSigner.buildAuthorizationHeaderValue("requestMethod","queryString",headers,"dhpUrl","requestbody"));
//    }
//
//
//    public void testJoinHeaders(){
//            Method method = null;
//        Map<String, String> headers = new HashMap<String,String>();
//        headers.put("key","value");
//        try {
//                method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", Map.class);
//                method.setAccessible(true);
//                method.invoke(mDhpApiSigner,headers);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//    }
//
//
//    public void testJoinHeaders2(){
//        Method method = null;
//        List<String>  headers = new ArrayList<String>();
//        headers.add("value");
//        try {
//            method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", List.class);
//            method.setAccessible(true);
//            method.invoke(mDhpApiSigner,headers);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//    public void testBuildAuthorizationHeaderValue(){
//        StringBuilder requestHeader = new StringBuilder();
//        requestHeader.append(";");
//        requestHeader.append("Credential:");
//        requestHeader.append("sharedKey");
//        requestHeader.append(";");
//        requestHeader.append("SignedHeaders:");
//        String header = requestHeader.toString();
//        String signature = "signature";
//        Method method = null;
//        try {
//            method = DhpApiSigner.class.getDeclaredMethod("buildAuthorizationHeaderValue", new Class[]{String.class, String.class});
//            method.setAccessible(true);
//            method.invoke(mDhpApiSigner,new Object[]{ header,signature});
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testHash(){
//        Method method = null;
//        String data= "data";
//        byte[] key= new byte[1];
//        key[0]=-1;
//
//        try {
//            method = DhpApiSigner.class.getDeclaredMethod("hash", new Class[]{String.class, byte[].class});
//            method.setAccessible(true);
//            method.invoke(mDhpApiSigner,new Object[]{data,key});
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}