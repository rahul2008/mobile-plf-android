package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class ResendVerificationEmailTest extends InstrumentationTestCase{

    Context context;
    @Mock
    ResendVerificationEmail mResendVerificationEmail;
    ResendVerificationEmailHandler mResendVerificationEmailHandler;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mResendVerificationEmailHandler = new ResendVerificationEmailHandler() {
            @Override
            public void onResendVerificationEmailSuccess() {

            }

            @Override
            public void onResendVerificationEmailFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        assertNotNull(mResendVerificationEmailHandler);
        mResendVerificationEmail = new ResendVerificationEmail(context,mResendVerificationEmailHandler);
        assertNotNull(mResendVerificationEmail);

    }
    @Test
    public void testmResendVerificationEmail(){
        CaptureApiError error = new CaptureApiError();
        mResendVerificationEmail.onFailure(error);
        assertSame(mResendVerificationEmail.mResendVerificationEmail,mResendVerificationEmailHandler );
    }
}