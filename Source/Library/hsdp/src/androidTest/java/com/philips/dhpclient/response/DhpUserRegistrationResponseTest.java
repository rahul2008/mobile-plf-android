//package com.philips.dhpclient.response;
//
//
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//public class DhpUserRegistrationResponseTest extends HSDPInstrumentationBase {
//
//    DhpUserRegistrationResponse mDhpUserRegistrationResponse;
//    DhpUserRegistrationResponse mDhpUserRegistrationResponse1;
//
//    Map<String, Object> mRawResponse;
//
//    @Before
//    public void setUp() throws Exception {
//          super.setUp();
//        mRawResponse = new HashMap<String,Object>();
//        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample",mRawResponse);
//        mDhpUserRegistrationResponse1 = new DhpUserRegistrationResponse("sample",mRawResponse);
//
//
//    }
//
//    public void testDhpUserRegistrationResponse(){
//        assertNotNull(mDhpUserRegistrationResponse);
//        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample","sample",mRawResponse);
//        assertNotNull(mDhpUserRegistrationResponse);
//        assertTrue(mDhpUserRegistrationResponse.equals(mDhpUserRegistrationResponse));
//        assertFalse(mDhpUserRegistrationResponse.equals(mDhpUserRegistrationResponse1));
//        assertFalse(mDhpUserRegistrationResponse.equals(mRawResponse));
//        assertFalse(mDhpUserRegistrationResponse.equals(null));
//
//
//        assertNotNull(mDhpUserRegistrationResponse.hashCode());
//    }
//}