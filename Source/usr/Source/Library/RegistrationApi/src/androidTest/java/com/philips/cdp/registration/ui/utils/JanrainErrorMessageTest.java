package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class JanrainErrorMessageTest extends RegistrationApiInstrumentationBase {

    @Mock
    JanrainErrorMessage janrainErrorMessage;

    @Mock
    Context context;

    @Mock
    private static final int INTERNET_NOT_AVAILABLE = 15;

    @Mock
    private static final int EMAIL_ALREADY_IN_USE = 14;

    @Mock
    private static final int INVALID_USERNAME_PASSWORD = 10;

    @Mock
    private static final int INVALID_PASSWORD = 9;

    @Mock
    private static final int JANRAIN_SIGN_IN_INVALID_INPUT = 2;

    @Mock
    private static final int JANRAIN_FORGOT_PASSWORD_INVALID_INPUT = 11;

    @Test
    public void testGetError() throws Exception {
         assertEquals(context.getResources().getString(R.string.reg_JanRain_Invalid_Input), janrainErrorMessage.getError(JANRAIN_SIGN_IN_INVALID_INPUT));
        assertEquals(context.getResources().getString(R.string.reg_JanRain_Invalid_Input),janrainErrorMessage.getError(JANRAIN_FORGOT_PASSWORD_INVALID_INPUT)); // "Invalid parameters";
        assertEquals(context.getResources().getString(R.string.reg_JanRain_LogIn_Failed), janrainErrorMessage.getError(INVALID_PASSWORD));

        assertEquals(context.getResources().getString(R.string.reg_JanRain_Invalid_Credentials), janrainErrorMessage.getError(INVALID_USERNAME_PASSWORD));
        assertEquals(context.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg), janrainErrorMessage.getError(EMAIL_ALREADY_IN_USE));

        assertEquals(context.getResources().getString(R.string.reg_JanRain_Error_Check_Internet), janrainErrorMessage.getError(INTERNET_NOT_AVAILABLE));
    }

    @Test
    public void testGetContext() throws Exception {
        janrainErrorMessage.setContext(context);
        assertEquals(context, janrainErrorMessage.getContext());

    }


    @Before
    public void setUp() throws Exception {
           super.setUp();
        context = getInstrumentation().getTargetContext();

        janrainErrorMessage = new JanrainErrorMessage(context);
        assertNotNull(janrainErrorMessage);
    }
}