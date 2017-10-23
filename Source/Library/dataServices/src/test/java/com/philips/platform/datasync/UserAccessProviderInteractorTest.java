/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserAccessProviderInteractorTest {
    @Test
    public void isLoggedIn() {
        givenUserIsLoggedIn();
        whenGettingIsLoggedIn();
        thenUserIsLoggedIn();
    }

    @Test
    public void isNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenGettingIsLoggedIn();
        thenUserIsNotLoggedIn();
    }

    private void givenUserIsLoggedIn() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
    }

    private void givenUserIsNotLoggedIn() {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
    }

    @Before
    public void setUp() {
        initMocks(this);

        interactor = new UserAccessProviderInteractor(accessProviderMock);
    }

    private void whenGettingIsLoggedIn() {
        isLoggedInResult = interactor.isLoggedIn();
    }

    private void thenUserIsLoggedIn() {
        assertTrue("User is logged in", isLoggedInResult);
    }

    private void thenUserIsNotLoggedIn() {
        assertFalse("User is not logged in", isLoggedInResult);
    }

    @Mock
    UCoreAccessProvider accessProviderMock;

    private UserAccessProviderInteractor interactor;

    private boolean isLoggedInResult;
}
