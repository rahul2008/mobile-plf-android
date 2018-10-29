/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw.injection;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class CswModuleTest {

    @Mock
    private Context contextMock;

    private CswModule subject;

    @Before
    public void setUp() {
        initMocks(this);
        subject = new CswModule(contextMock);
    }

    @Test
    public void givenModuleCreated_whenProvideAppContext_thenShouldAlwaysReturnObject() {
        Context result = subject.provideAppContext();
        assertNotNull(result);
    }
}