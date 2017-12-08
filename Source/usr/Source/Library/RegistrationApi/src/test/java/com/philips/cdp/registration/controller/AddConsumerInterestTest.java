package com.philips.cdp.registration.controller;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/28/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AddConsumerInterestTest {


    @Mock
    AddConsumerInterestHandler addConsumerInterestHandlerMock;

    AddConsumerInterest addConsumerInterest;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        addConsumerInterest = new AddConsumerInterest(addConsumerInterestHandlerMock);
    }

    @Test
    public void onSuccess() throws Exception {

        addConsumerInterest.onSuccess();
        Mockito.verify(addConsumerInterestHandlerMock).onAddConsumerInterestSuccess();
    }

    @Mock
    CaptureApiError captureApiErrorMock;

    @Test
    public void onFailure() throws Exception {

        addConsumerInterest.onFailure(captureApiErrorMock);
        Mockito.verify(addConsumerInterestHandlerMock).onAddConsumerInterestFailedWithError(0);

    }

}