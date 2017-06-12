//package com.philips.dhpclient.response;
//
//
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//
//
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
//public class DhpAuthenticationResponseTest extends HSDPInstrumentationBase {
//    DhpAuthenticationResponse mDhpAuthenticationResponse;
//    DhpAuthenticationResponse mDhpAuthenticationResponse1;
//
//    Map<String, Object> mRawResponse;
//
//
//    @Before
//    public void setUp() throws Exception {
//         super.setUp();
//        mDhpAuthenticationResponse = new DhpAuthenticationResponse(mRawResponse);
//        mDhpAuthenticationResponse1 = new DhpAuthenticationResponse(mRawResponse);
//
//
//
//    }
//
//    public void testDhpAuthenticationResponse()
//    {
//        assertNotNull(mDhpAuthenticationResponse);
//        mDhpAuthenticationResponse = new DhpAuthenticationResponse("accessToken","refreshToken",1221,"userId",mRawResponse) ;
//        assertNotNull(mDhpAuthenticationResponse);
//        assertTrue(mDhpAuthenticationResponse.equals(mDhpAuthenticationResponse));
//        assertFalse(mDhpAuthenticationResponse.equals(mDhpAuthenticationResponse1));
//        assertFalse(mDhpAuthenticationResponse.equals(mRawResponse));
//        assertFalse(mDhpAuthenticationResponse.equals(null));
//
//        assertNotNull(mDhpAuthenticationResponse.hashCode());
//        assertNotNull(mDhpAuthenticationResponse.toString());
//    }
//
//
//
//}