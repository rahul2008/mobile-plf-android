package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class UpdateReceiveMarketingEmailTest extends InstrumentationTestCase {


    @Mock
    UpdateReceiveMarketingEmail addConsumerInterest;
    UpdateReceiveMarketingEmailHandler mAddConsumerInterestHandler;

    @Mock
    Context context;


    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mAddConsumerInterestHandler = new UpdateReceiveMarketingEmailHandler() {
            @Override
            public void onUpdateReceiveMarketingEmailSuccess() {

            }

            @Override
            public void onUpdateReceiveMarketingEmailFailedWithError(int error) {

            }
        };

        assertNotNull(mAddConsumerInterestHandler);
        addConsumerInterest = new UpdateReceiveMarketingEmail(context);
        assertNotNull(addConsumerInterest);
    }


//    @Test
//    public void testOnFailure() throws Exception {
////        addConsumerInterest.onSuccess();
//        CaptureApiError error = new CaptureApiError();
//        addConsumerInterest.onFailure(error);
//        assertSame(addConsumerInterest.mUpdateReceiveMarketingEmailHandler,mAddConsumerInterestHandler );
//    }

   }