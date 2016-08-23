package com.philips.cdp.registration.apptagging;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/11/2016.
 */
public class AppTaggingErrorsTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    AppTaggingErrors appTaggingErrors = new AppTaggingErrors();



    public AppTaggingErrorsTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

    }

    public void testAppTaggingErrors() {

       // AppInfraSingleton.setInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));

    /*    appTaggingErrors.trackActionRegisterError(111);
        appTaggingErrors.trackActionRegisterError(390);
        appTaggingErrors.trackActionRegisterError(210);
        appTaggingErrors.trackActionRegisterError(112);

        appTaggingErrors.trackActionLoginError(111);
        appTaggingErrors.trackActionLoginError(112);
        appTaggingErrors.trackActionLoginError(210);
        appTaggingErrors.trackActionLoginError(211);

        appTaggingErrors.trackActionForgotPasswordFailure(111);
        appTaggingErrors.trackActionForgotPasswordFailure(212);

        appTaggingErrors.trackActionResendNetworkFailure(111);
        appTaggingErrors.trackActionResendNetworkFailure(112);*/
    }
}
