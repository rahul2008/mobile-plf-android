package com.philips.platform.appinfra.securestoragev2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by abhishek on 2/8/18.
 */
public class SSKeyProviderExceptionTest {
    @Test
    public void ssKeyProviderExceptionMessageTest() throws Exception {
        SSKeyProviderException ssEncodeDecodeException=new SSKeyProviderException("This is key provider exception");
        assertEquals("This is key provider exception",ssEncodeDecodeException.getMessage());
    }
}