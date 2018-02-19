/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.ews.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static android.support.v4.content.ContextCompat.getDrawable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ContextCompat.class)
public class StringProviderTest {

    private StringProvider subject;

    private static final int PARAM_1 = 1231;
    private static final int PARAM_2 = 2343;
    private static final int PARAM_3 = 3544;
    private final String PARAM_ARG = "abc";

    @Mock
    private Context mockContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockStatic(ContextCompat.class);

        subject = new StringProvider(mockContext);
    }

    @Test
    public void itShouldGiveStringParam() throws Exception {
        subject.getString(PARAM_1);

        verify(mockContext).getString(anyInt());
    }

    @Test
    public void itShouldGiveStringWithResArgument() throws Exception {
        subject.getString(PARAM_1, PARAM_2);

        verify(mockContext).getString(anyInt(), (String) any());
    }

    @Test
    public void itShouldGiveStringByPassingStringArguments() throws Exception {
        subject.getString(PARAM_1, PARAM_ARG);

        verify(mockContext).getString(anyInt(), any());
    }

    @Test
    public void itShouldGiveStringByPassingDifferentResArguments() throws Exception {
        subject.getString(PARAM_1, PARAM_2, PARAM_3);

        verify(mockContext).getString(anyInt(), any(), any());
    }

    @Test
    public void itShouldGiveStringByPassingResAndStringArguments() throws Exception {
        subject.getString(PARAM_1, PARAM_2, PARAM_ARG);

        verify(mockContext).getString(anyInt(), any(), any());
    }

    @Test
    public void getImageResource() throws Exception {
        subject.getImageResource(PARAM_1);

        getDrawable(mockContext, PARAM_1);
    }
}
