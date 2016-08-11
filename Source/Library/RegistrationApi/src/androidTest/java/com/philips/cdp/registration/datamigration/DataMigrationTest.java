package com.philips.cdp.registration.datamigration;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.controller.RegisterSocial;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/11/2016.
 */
public class DataMigrationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    DataMigration continueSocialProviderLogin;
    Context context;


    public DataMigrationTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getContext();
        continueSocialProviderLogin = new DataMigration(context);
    }

    public void testCheckFileEncryptionStatus(){
        continueSocialProviderLogin.checkFileEncryptionStatus();
    }
}
