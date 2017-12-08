package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;


public class AddConsumerInterestTest extends RegistrationApiInstrumentationBase {
    @Mock
    AddConsumerInterest addConsumerInterest;
    AddConsumerInterestHandler mAddConsumerInterestHandler;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mAddConsumerInterestHandler = new AddConsumerInterestHandler() {
            @Override
            public void onAddConsumerInterestSuccess() {

            }

            @Override
            public void onAddConsumerInterestFailedWithError(int error) {
                assertNotNull(error);
            }
        };

        assertNotNull(mAddConsumerInterestHandler);
        addConsumerInterest = new AddConsumerInterest(mAddConsumerInterestHandler);
        assertNotNull(addConsumerInterest);




    }

    @Test
    public void testOnSuccess() throws Exception {
        addConsumerInterest.onSuccess();
        CaptureApiError error = new CaptureApiError();
        addConsumerInterest.onFailure(error);
        assertSame(addConsumerInterest.mAddConsumerInterest,mAddConsumerInterestHandler );

    }

//    @Test
//    public void testOnFailure() throws Exception {
//
//    }
}