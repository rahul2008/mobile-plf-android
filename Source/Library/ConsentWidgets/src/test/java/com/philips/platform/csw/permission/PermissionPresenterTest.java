package com.philips.platform.csw.permission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    PermissionPresenter mPermissionPresenter;
    @Mock
    PermissionInterface mockPermissionInterface;
    @Mock
    GetConsentInteractor mockInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, mockInteractor);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        mPermissionPresenter.getConsentStatus();
        Mockito.verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        mPermissionPresenter.getConsentStatus();
        Mockito.verify(mockInteractor).getConsents(mPermissionPresenter);
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        mPermissionPresenter.onConsentFailed(42);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onConsentRetrieved(null);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }
}

