package com.philips.platform.catk.error;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maqsood on 11/4/17.
 */
public class ServerErrorTest {

    private List<Error> serverErrorList;
    private Error error;
    private ServerError serverError;

    @Before
    public void setUp() throws Exception {
        serverError = new ServerError();
        serverErrorList = new ArrayList<>();
        error = new Error();
        error.setSubject("sfsfas");
        error.setSubject("Sfs");
        error.setReason("sgs");
        error.getSubjectType("gsgf");
        serverError.setErrors(serverErrorList);
        serverErrorList.add(error);
    }

    @After
    public void tearDown() throws Exception {
        serverErrorList = null;
        error =null;
    }

    @Test
    public void testserverErrorListNotNull() throws Exception {
        Assert.assertNotNull(serverErrorList);
    }


    @Test
    public void testserverErrorListNotEmty() throws Exception {
        Assert.assertEquals(1,serverErrorList.size());
    }
}