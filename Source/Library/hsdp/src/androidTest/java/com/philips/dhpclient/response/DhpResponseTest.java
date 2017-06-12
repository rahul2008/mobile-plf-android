//package com.philips.dhpclient.response;
//
//
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertTrue;
//
//
//
//
//public class DhpResponseTest extends HSDPInstrumentationBase {
//
//    DhpResponse mDhpResponse;
//    DhpResponse mDhpResponse1;
//
//    Map<String, Object> mRawResponse;
//    @Before
//    public void setUp() throws Exception {
//           super.setUp();
//        mRawResponse = new HashMap<String,Object>();
//        mDhpResponse = new DhpResponse(mRawResponse);
//        mDhpResponse1 = new DhpResponse(mRawResponse);
//
//    }
//
//    public void testDhpResponse(){
//        assertNotNull(mDhpResponse);
//        mDhpResponse = new DhpResponse("sample",mRawResponse);
//        assertNotNull(mDhpResponse);
//        assertTrue(mDhpResponse.equals(mDhpResponse));
//        assertFalse(mDhpResponse.equals(mDhpResponse1));
//        assertFalse(mDhpResponse.equals(mRawResponse));
//        assertFalse(mDhpResponse.equals(null));
//        assertNotNull(mDhpResponse.hashCode());
//        assertNotNull(mDhpResponse.toString());
//    }
//}