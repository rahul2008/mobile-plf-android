package com.philips.cdp2.ews.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ColorsUtilTest {

    @Mock
    Context mockContext;

    @Mock
    Resources.Theme mockTheme;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mockContext.getTheme()).thenReturn(mockTheme);
    }

    @Test
    public void itShouldVerifyGetAttributeColor() throws Exception {
        ColorsUtil.getAttributeColor(mockContext, 1);
        verify(mockTheme).resolveAttribute(anyInt(), (TypedValue) anyObject(), anyBoolean());
    }

}