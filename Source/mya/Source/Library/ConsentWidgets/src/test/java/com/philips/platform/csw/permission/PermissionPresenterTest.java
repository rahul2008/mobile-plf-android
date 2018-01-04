package com.philips.platform.csw.permission;

import android.test.mock.MockContext;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.catk.ConsentAccessToolKitEmulator;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.catk.CswConsentAccessToolKitManipulator;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
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
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentNetworkError givenError;
    private Consent requiredConsent;

    @Mock
    private PermissionInterface mockPermissionInterface;
    @Mock
    private ConsentInteractor mockInteractor;
    @Mock
    private PermissionAdapter mockAdapter;
    @Mock
    private Consent mockRequiredConsent;
    @Mock
    private ConsentDefinition mockConsentDefinition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, mockInteractor, mockAdapter);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        mPermissionPresenter.getConsentStatus();
        verify(mockInteractor).fetchLatestConsents(mPermissionPresenter);
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

    @Test
    public void testShouldOptInWhenOnGetClickStreamConsentRetrieved() {
        givenCswComponent();
        givenActiveClickStreamConsent();
        givenClickStreamConsentView();
        whenOnGetConsentRetrieved();
        thenVerifyEnableTaggingIsInvoked();
    }

    @Test
    public void testShouldOptOutWhenOnGetClickStreamConsentRetrieved() {
        givenCswComponent();
        givenRejectedClickStreamConsent();
        givenClickStreamConsentView();
        whenOnGetConsentRetrieved();
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
        BackendConsent consent = new BackendConsent(new Locale("en", "US"), ConsentStatus.active, "clickstream", 1);
        requiredConsent = new Consent(consent, clickStreamConsentDefinition());
    }

    private void givenRejectedClickStreamConsent() {
        BackendConsent consent = new BackendConsent(new Locale("en", "US"), ConsentStatus.rejected, "clickstream", 1);
        requiredConsent = new Consent(consent, clickStreamConsentDefinition());
    }

    private void givenClickStreamConsentView() {
        ConsentView consentView = new ConsentView(clickStreamConsentDefinition());
        List<ConsentView> consentViews = new ArrayList<>();
        consentViews.add(consentView);
        when(mockAdapter.getConsentViews()).thenReturn(consentViews);
    }

    private ConsentDefinition clickStreamConsentDefinition() {
        return new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"),
                1, new Locale("en", "US"));
    }

    public void whenCreateConsentSuccess() {
        mPermissionPresenter.onCreateConsentSuccess(requiredConsent);
    }

    private void whenOnGetConsentRetrieved() {
        ArrayList<Consent> consentArrayList = new ArrayList<>();
        consentArrayList.add(requiredConsent);
        mPermissionPresenter.onGetConsentRetrieved(consentArrayList);
    }

    private void thenVerifyEnableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
    }

    private void thenVerifyDisableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
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
        when(mockRequiredConsent.getType()).thenReturn("");
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
