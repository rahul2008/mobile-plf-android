package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.registration.User;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ValidatorTest extends MockitoTestCase {

    Validator validator;
    @Mock
    Context context;

    User userMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        validator = new Validator();
        userMock = mock(User.class);
        context = getInstrumentation().getContext();
    }

    @Test
    public void testReturnTrueWhenUserSignedIn() throws Exception {
        when(userMock.isUserSignIn(context)).thenReturn(true);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        assertTrue(validator.isUserSignedIn(userMock, context));
    }

    @Test
    public void testReturnFalseWhenUserNotSignedIn() throws Exception {
        when(userMock.isUserSignIn(context)).thenReturn(false);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(false);
        assertFalse(validator.isUserSignedIn(userMock, context));
    }

    @Test
    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(validator.isValidaDate("2016-03-22"));
    }

    @Test
    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(validator.isValidaDate("1998-03-22"));
    }
}