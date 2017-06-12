package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;


public class UpdateReceiveMarketingEmailTest extends RegistrationApiInstrumentationBase {


    @Mock
    UpdateReceiveMarketingEmail addConsumerInterest;
    UpdateUserDetailsHandler mAddConsumerInterestHandler;

    @Mock
    Context context;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mAddConsumerInterestHandler = new UpdateUserDetailsHandler() {
            @Override
            public void onUpdateSuccess() {

            }

            @Override
            public void onUpdateFailedWithError(int error) {

            }
        };

        assertNotNull(mAddConsumerInterestHandler);
       // addConsumerInterest = new UpdateReceiveMarketingEmail(mAddConsumerInterestHandler,context,true);
        assertNull(addConsumerInterest);
    }


    @Test
    public void testOnFailure() throws Exception {
//        addConsumerInterest.onSuccess();
        CaptureApiError error = new CaptureApiError();
    //    addConsumerInterest.onFailure(error);
      //  assertSame(addConsumerInterest.mUpdateUserDetails,mAddConsumerInterestHandler );
    }

   }