package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class UpdateReceiveMarketingEmailTest extends InstrumentationTestCase {


    @Mock
    UpdateReceiveMarketingEmail addConsumerInterest;
    UpdateUserDetailsHandler mAddConsumerInterestHandler;

    @Mock
    Context context;


    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
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