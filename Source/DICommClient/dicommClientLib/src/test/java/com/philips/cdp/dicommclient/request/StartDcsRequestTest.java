package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.cpp.CppController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class StartDcsRequestTest {

    @Mock
    CppController cppControllerMock;

    @Mock
    ResponseHandler responseHandlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void canInit() throws Exception {
        new StartDcsRequest(cppControllerMock, responseHandlerMock);
    }
}