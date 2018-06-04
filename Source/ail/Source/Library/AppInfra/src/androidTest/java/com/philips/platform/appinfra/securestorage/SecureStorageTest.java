package com.philips.platform.appinfra.securestorage;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

public class SecureStorageTest extends AppInfraInstrumentation {
    private SecureStorageInterface mSecureStorage = null;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        mAppInfra = new AppInfra.Builder().build(context);
        mSecureStorage = new SecureStorage(mAppInfra);
    }

    public void testIsLaunchedByEmulator() {
        assertFalse(mSecureStorage.isEmulator());
    }

    public void testIsCodeTampered() {
        assertTrue(mSecureStorage.isCodeTampered());
    }

}