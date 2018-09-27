package com.philips.cdp.registration.controller;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by philips on 11/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddConsumerInterestTest extends TestCase {

    @Mock
    private CaptureApiError captureApiErrorMock;
    @Mock
    private AddConsumerInterestHandler addConsumerInterestHandlerMock;

    private AddConsumerInterest addConsumerInterest;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        addConsumerInterest = new AddConsumerInterest(addConsumerInterestHandlerMock);
    }

    @Test
    public void onSuccess() {
        addConsumerInterest.onSuccess();
        Mockito.verify(addConsumerInterestHandlerMock).onAddConsumerInterestSuccess();
    }

    @Test
    public void onFailure() {
        addConsumerInterest.onFailure(captureApiErrorMock);
        Mockito.verify(addConsumerInterestHandlerMock).onAddConsumerInterestFailedWithError(0);

    }

}