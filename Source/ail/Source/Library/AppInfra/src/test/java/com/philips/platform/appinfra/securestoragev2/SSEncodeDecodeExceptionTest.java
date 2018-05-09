package com.philips.platform.appinfra.securestoragev2;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by abhishek on 2/8/18.
 */
public class SSEncodeDecodeExceptionTest {
    @Test
    public void ssEncodeDecodeExceptionMessageTest() throws Exception {
        SSEncodeDecodeException ssEncodeDecodeException=new SSEncodeDecodeException("This is encode decode exception");
        assertEquals("This is encode decode exception",ssEncodeDecodeException.getMessage());
    }


}