//package com.philips.dhpclient;
//
//
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static junit.framework.Assert.assertNotNull;
//
//
//public class DhpCommunicationExceptionTest extends HSDPInstrumentationBase {
//
//    DhpCommunicationException mDhpCommunicationException;
//    @Before
//    public void setUp() throws Exception {
//               super.setUp();
//        Throwable e = new Throwable();
//        mDhpCommunicationException =new DhpCommunicationException(e);
//
//    }
//
//    @Test
//    public void testDhpcommunicationException()
//    {
//        assertNotNull(mDhpCommunicationException);
//        mDhpCommunicationException =new DhpCommunicationException("sample","sample");
//        assertNotNull(mDhpCommunicationException);
//    }
//}