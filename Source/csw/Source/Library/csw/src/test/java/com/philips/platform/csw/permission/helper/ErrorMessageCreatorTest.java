package com.philips.platform.csw.permission.helper;

import android.content.Context;

import com.philips.platform.csw.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ErrorMessageCreatorTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void givenCreatorExists_andErrorCode1_whenGetMessageBasedOnErrorCode_thenShouldReturnString() {
        String result = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(context, 1);
        assertEquals("There was a problem (error 1). Please try again.", result);
    }

    @Test
    public void givenCreatorExists_andErrorCode2_whenGetMessageBasedOnErrorCode_thenShouldReturnString() {
        String result = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(context, 2);
        assertEquals("There was a problem (error 2). Please try again.", result);
    }

    @Test
    public void givenCreatorExists_andErrorCode3_whenGetMessageBasedOnErrorCode_thenShouldReturnString() {
        String result = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(context, 3);
        assertEquals("There was a problem (error 3). Please try again.", result);
    }

    @Test
    public void givenCreatorExists_andErrorCode4_whenGetMessageBasedOnErrorCode_thenShouldReturnString() {
        String result = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(context, 4);
        assertEquals("There was a problem connecting to the server. Make sure the date/time on your device is set correctly. If the problem persists, please logout and login.", result);
    }

    @Test
    public void givenCreatorExists_andErrorCode5_whenGetMessageBasedOnErrorCode_thenShouldReturnString() {
        String result = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(context, 5);
        assertEquals("There was a problem (error 5). Please try again.", result);
    }
}