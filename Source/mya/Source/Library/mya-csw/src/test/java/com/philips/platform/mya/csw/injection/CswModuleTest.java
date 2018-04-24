package com.philips.platform.mya.csw.injection;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CswModuleTest {

    @Mock
    private MockContext contextMock;

    private CswModule subject;

    @Before
    public void setUp(){
        initMocks(this);
        subject = new CswModule(contextMock);
    }

    @Test
    public void givenModuleCreated_whenProvideAppContext_thenShouldAlwaysReturnObject() {
        Context result = subject.provideAppContext();
        assertNotNull(result);
    }
}