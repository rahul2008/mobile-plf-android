package com.philips.platform.csw.permission;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.philips.platform.consenthandlerinterface.ConsentConfiguration;
import com.philips.platform.consenthandlerinterface.ConsentError;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import edu.emory.mathcs.backport.java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentError givenError;
    private List<ConsentConfiguration> givenConsentConfigurations = new ArrayList<>();

    @Mock
    private PermissionInterface mockPermissionInterface;
    @Mock
    private ConsentHandlerInterface mockHandlerInterface;
    @Mock
    private PermissionAdapter mockAdapter;
    @Mock
    private Consent mockRequiredConsent;
    @Mock
    private ConsentDefinition mockConsentDefinition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        givenPresenter();
    }

    @Test
    public void testShowProgressDialog_withNoConsentConfigurations() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockPermissionInterface, never()).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor_withNoConsentConfigurations() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockHandlerInterface, never()).checkConsents(mPermissionPresenter);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        givenConsentConfigurations();
        mPermissionPresenter.getConsentStatus();
        verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        givenConsentConfigurations();
        mPermissionPresenter.getConsentStatus();
        verify(mockHandlerInterface).checkConsents(mPermissionPresenter);
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        givenConsentError();
        mPermissionPresenter.onGetConsentFailed(givenError);
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onGetConsentRetrieved(new ArrayList<Consent>());
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
        givenError = new ConsentError("SOME ERROR", 401);
    }

    private void givenConsentConfigurations() {
        ConsentDefinition definition = new ConsentDefinition("", "", Collections.singletonList("moment"), 0, Locale.US);
        ConsentConfiguration configuration = new ConsentConfiguration(Arrays.asList(definition), mockHandlerInterface);
        givenConsentConfigurations = Arrays.asList(configuration);
        givenPresenter();
    }

    private void givenPresenter() {
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, givenConsentConfigurations, mockAdapter);
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
        mPermissionPresenter.onToggledConsent(mockConsentDefinition, mockHandlerInterface, toggled);
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
