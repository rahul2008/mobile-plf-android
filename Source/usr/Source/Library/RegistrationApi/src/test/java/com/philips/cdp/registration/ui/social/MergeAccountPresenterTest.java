/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.social;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by philips on 11/22/17.
 */
@RunWith(RobolectricTestRunner.class)
public class MergeAccountPresenterTest {

    @Mock
    private User userMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    private MergeAccountContract mergeAccountContractMock;

    private MergeAccountPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

        presenter = new MergeAccountPresenter(mergeAccountContractMock,userMock);
    }

    @Test
    public void shouldSetStatus_OnNetWorkStateReceived() throws Exception {
        presenter.onNetWorkStateReceived(true);
        Mockito.verify(mergeAccountContractMock).connectionStatus(true);
        Mockito.verify(mergeAccountContractMock).mergeStatus(true);
    }

    @Test
    public void shouldReturnEmailIfValid_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips@gmail.com");
        Assert.assertSame("philips@gmail.com",presenter.getLoginWithDetails());
    }

    @Test
    public void shouldReturnMobileIfInvalidEmail_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips");
        Mockito.when(userMock.getMobile()).thenReturn("8904291902");
        Assert.assertSame("8904291902",presenter.getLoginWithDetails());
    }
}