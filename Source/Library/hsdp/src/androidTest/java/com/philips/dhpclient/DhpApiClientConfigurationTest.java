//package com.philips.dhpclient;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNotNull;
//
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//
//
//public class DhpApiClientConfigurationTest extends HSDPInstrumentationBase{
//
//    DhpApiClientConfiguration mDhpApiClientConfiguration;
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        mDhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey","signingSecret");
//        assertNotNull(mDhpApiClientConfiguration);
//    }
//
//    @Test
//    public void testGetApiBaseUrl() throws Exception {
//        assertEquals("apiBaseUrl",mDhpApiClientConfiguration.getApiBaseUrl());
//
//    }
//
//    @Test
//    public void testGetDhpApplicationName() throws Exception {
//        assertEquals("dhpApplicationName",mDhpApiClientConfiguration.getDhpApplicationName());
//
//    }
//
//    @Test
//    public void testGetSigningKey() throws Exception {
//        assertEquals("signingKey",mDhpApiClientConfiguration.getSigningKey());
//
//    }
//
//    @Test
//    public void testGetSigningSecret() throws Exception {
//        assertEquals("signingSecret",mDhpApiClientConfiguration.getSigningSecret());
//
//    }
//}