/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.securestorage;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecureStorageTest {

    private SecureStorageInterface mSecureStorage = null;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        mSecureStorage = new SecureStorage(mAppInfra);
    }

    @Test
    public void testIsLaunchedByEmulator() {
        assertFalse(mSecureStorage.isEmulator());
    }

    @Test
    public void testIsCodeTampered() {
        assertTrue(mSecureStorage.isCodeTampered());
    }

}