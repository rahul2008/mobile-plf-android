/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.productselection;

import android.content.Context;

import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class ApplicationTest {

    @Test
    public void testAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertNotNull(appContext);
    }
}