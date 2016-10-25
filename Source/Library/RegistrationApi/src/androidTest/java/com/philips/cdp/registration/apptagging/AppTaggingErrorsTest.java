package com.philips.cdp.registration.apptagging;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.platform.appinfra.AppInfra;

/**
 * Created by 310243576 on 8/11/2016.
 */
public class AppTaggingErrorsTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    AppTaggingErrors appTaggingErrors;
    Context mContext;




    public AppTaggingErrorsTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        appTaggingErrors = new AppTaggingErrors();


    }

    public void testAppTaggingErrors() {

        try {

            synchronized(this){//synchronized block

                RegistrationHelper.getInstance().setAppInfraInstance( new AppInfra.Builder().build(mContext));
                appTaggingErrors.trackActionRegisterError(111);
                appTaggingErrors.trackActionRegisterError(390);
                appTaggingErrors.trackActionRegisterError(210);
                appTaggingErrors.trackActionRegisterError(112);

                appTaggingErrors.trackActionLoginError(111);
                appTaggingErrors.trackActionLoginError(112);
                appTaggingErrors.trackActionLoginError(210);
                appTaggingErrors.trackActionLoginError(211);

                appTaggingErrors.trackActionForgotPasswordFailure(111);
                appTaggingErrors.trackActionForgotPasswordFailure(212);
                appTaggingErrors.trackActionForgotPasswordFailure(211);


                appTaggingErrors.trackActionResendNetworkFailure(111);
                appTaggingErrors.trackActionResendNetworkFailure(112);

                assertNotNull(appTaggingErrors);

            }
        }catch(Exception e){System.out.println(e);}

    }
}
