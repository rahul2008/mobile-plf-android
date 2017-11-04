package com.philips.platform.catk.error;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Maqsood on 11/4/17.
 */

public class ErrorTest {

    private Error error;

    @Before
    public void setUp() throws Exception {
      error = new Error();
    }

    @After
    public void tearDown() throws Exception {
        error = null;
    }

    @Test
    public void getMessage() throws Exception {
        error.setMessage("time out exception");
        Assert.assertNotNull(error.getMessage());
    }

    @Test
    public void getReason() throws Exception {
        error.setReason("Network failure");
        Assert.assertNotNull(error.getReason());
    }

    @Test
    public void getSubject() throws Exception {
        error.setSubject("time out");
        Assert.assertNotNull(error.getSubject());
    }

    @Test
    public void getSubjectType() throws Exception {
        error.setSubjectType("3443");
        Assert.assertNotNull(error.getSubjectType("3443"));
    }

    @Test
    public void getType() throws Exception {
        error.setType("Type error");
        Assert.assertNotNull(error.getType());
    }

}