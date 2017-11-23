/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.util;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;


public class TextUtilTest {

    @Mock private Context mockContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldVerifyIsEmpty() throws Exception {
        assertEquals(true,TextUtil.isEmpty(null));
    }

//    @Test
//    public void itShouldVerifyHTMLText() throws Exception{
//        when(mockContext.getString(R.string.label_ews_home_network_body)).thenReturn("Please read all 4 steps before leaving the application.\\n1. Tap WiFi in your phone Settings\\n2. Make sure your WiFi is turned on \\n3. Select your home network\\n4. Return to the %1$s");
//        String explanation = String.format(Locale.getDefault(), mockContext.getString(R.string.label_ews_home_network_body),
//                "appName");
//
//        assertNotNull(TextUtil.getHTMLText(explanation));
//    }
}