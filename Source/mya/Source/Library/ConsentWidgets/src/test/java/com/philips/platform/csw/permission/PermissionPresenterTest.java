package com.philips.platform.csw.permission;

import com.android.volley.VolleyError;
import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentNetworkError givenError;

    @Mock
    private PermissionInterface mockPermissionInterface;
    @Mock
    private GetConsentInteractor mockGetInteractor;
    @Mock
    private CreateConsentInteractor mockCreateInteractor;
    @Mock
    private PermissionAdapter mockAdapter;
    @Mock
    private RequiredConsent mockRequiredConsent;
    @Mock
    private ConsentDefinition mockConsentDefinition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, mockGetInteractor, mockCreateInteractor, mockAdapter);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockGetInteractor).fetchLatestConsents(mPermissionPresenter);
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        givenConsentError();
        mPermissionPresenter.onGetConsentFailed(givenError);
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onGetConsentRetrieved(new ArrayList<RequiredConsent>());
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testShouldShowToastWhenGetConsentFails() throws Exception {
        givenConsentError();
        whenGetConsentFailed();
        thenErrorIsShown();
    }

    @Test
    public void testShouldShowErrorWhenCreateConsentFails() throws Exception {
        givenConsentError();
        whenCreateConsentFailed();
        thenErrorIsShown();
    }

    @Test
    public void testShouldShowLoaderWhenTogglingConsent() throws Exception {
        whenTogglingConsentTo(true);
        thenProgressIsShown();
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentFails() throws Exception {
        whenTogglingConsentTo(true);
        whenCreateConsentFailed();
        thenProgressIsHidden();
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentSucceeds() throws Exception {
        whenTogglingConsentTo(false);
        whenCreateConsentSucceeds();
        thenProgressIsHidden();
    }

    private void givenConsentError() {
        givenError = new ConsentNetworkError(new VolleyError());
    }

    private void whenGetConsentFailed() {
        mPermissionPresenter.onGetConsentFailed(givenError);
    }

    private void whenCreateConsentFailed() {
        mPermissionPresenter.onCreateConsentFailed(null, givenError);
    }

    private void whenCreateConsentSucceeds() {
        mPermissionPresenter.onCreateConsentSuccess(mockRequiredConsent);
    }

    private void whenTogglingConsentTo(boolean toggled) {
        mPermissionPresenter.onToggledConsent(mockConsentDefinition, toggled);
    }

    private void thenErrorIsShown() {
        verify(mockPermissionInterface).showErrorDialog(givenError);
    }

    private void thenProgressIsShown() {
        verify(mockPermissionInterface).showProgressDialog();
    }

    private void thenProgressIsHidden() {
        verify(mockPermissionInterface).hideProgressDialog();
    }
}
