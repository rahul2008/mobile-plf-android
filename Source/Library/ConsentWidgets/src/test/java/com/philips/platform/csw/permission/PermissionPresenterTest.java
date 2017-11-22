package com.philips.platform.csw.permission;

import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.model.RequiredConsent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    PermissionPresenter mPermissionPresenter;
    @Mock
    PermissionInterface mockPermissionInterface;
    @Mock
    GetConsentInteractor mockGetInteractor;
    @Mock
    CreateConsentInteractor mockCreateInteractor;
    @Mock private PermissionAdapter mockAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<ConsentView> consentViews = new ArrayList<>();
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, mockGetInteractor, mockCreateInteractor, consentViews,
                mockAdapter);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        mPermissionPresenter.getConsentStatus();
        Mockito.verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        mPermissionPresenter.getConsentStatus();
        Mockito.verify(mockGetInteractor).fetchLatestConsents(mPermissionPresenter);
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        mPermissionPresenter.onConsentFailed(42);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onConsentRetrieved(new ArrayList<RequiredConsent>());
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }
}
