package com.philips.platform.csw.permission;

import android.test.mock.MockContext;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.catk.ConsentAccessToolKitEmulator;
import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.CswConsentAccessToolKitManipulator;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswSettings;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentNetworkError givenError;
    private RequiredConsent requiredConsent;

    @Mock
    private PermissionInterface mockPermissionInterface;
    @Mock
    private GetConsentInteractor mockGetInteractor;
    @Mock
    private CreateConsentInteractor mockCreateInteractor;
    @Mock
    private PermissionAdapter mockAdapter;

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
        thenToastIsShown();
    }

    @Test
    public void testShouldShowToastWhenCreateConsentFails() throws Exception {
        givenConsentError();
        whenCreateConsentFailed();
        thenToastIsShown();
    }

    @Test
    public void testShouldOptInWhenClickStreamCreateConsentSuccess() {
        givenCswComponent();
        givenActiveClickStreamConsent();
        whenCreateConsentSuccess();
        thenVerifyEnableTaggingIsInvoked();
    }

    @Test
    public void testShouldOptOutWhenClickStreamCreateConsentSuccess() {
        givenCswComponent();
        givenRejectedClickStreamConsent();
        whenCreateConsentSuccess();
        thenVerifyDisableTaggingIsInvoked();
    }

    private void givenCswComponent() {
        CswInterface cswInterface = new CswInterface();
        AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        MockContext context = new MockContext();
        ConsentAccessToolKitEmulator consentAccessToolKit = new ConsentAccessToolKitEmulator();
        CswConsentAccessToolKitManipulator.setInstance(consentAccessToolKit);
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    private void givenActiveClickStreamConsent() {
        Consent consent = new Consent(new Locale("en", "US"), ConsentStatus.active, "clickstream", 1);
        ConsentDefinition consentDefinition = new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"),
                1, new Locale("en", "US"));
        requiredConsent = new RequiredConsent(consent, consentDefinition);
    }

    private void givenRejectedClickStreamConsent() {
        Consent consent = new Consent(new Locale("en", "US"), ConsentStatus.rejected, "clickstream", 1);
        ConsentDefinition consentDefinition = new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"),
                1, new Locale("en", "US"));
        requiredConsent = new RequiredConsent(consent, consentDefinition);
    }

    public void whenCreateConsentSuccess() {
        mPermissionPresenter.onCreateConsentSuccess(requiredConsent);
    }

    private void thenVerifyEnableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
    }

    private void thenVerifyDisableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
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

    private void thenToastIsShown() {
        verify(mockPermissionInterface).showErrorDialog(givenError);
    }
}
