package com.philips.platform.mya.csw.permission;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.catk.ConsentAccessToolKitEmulator;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.mya.csw.CswDependencies;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswSettings;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.csw.mock.RestInterfaceMock;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;

import android.content.Context;
import android.test.mock.MockContext;

import edu.emory.mathcs.backport.java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private static final String AMERICAN_LOCALE = "en-US";
    private PermissionPresenter mPermissionPresenter;
    private ConsentError givenError;
    private Consent requiredConsent;
    private List<ConsentConfiguration> givenConsentConfigurations = new ArrayList<>();
    private RestInterfaceMock restInterfaceMock = new RestInterfaceMock();

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
    @Mock
    private ConsentHandlerInterface mockConsentHandler;
    @Mock
    private Context mockContext;

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
        givenConsentConfigurations();
        verify(mockHandlerInterface, never()).fetchConsentStates(givenConsentConfigurations.get(0).getConsentDefinitionList(), mPermissionPresenter);
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
        verify(mockHandlerInterface).fetchConsentStates(givenConsentConfigurations.get(0).getConsentDefinitionList(), mPermissionPresenter);
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        givenConsentError();
        mPermissionPresenter.onGetConsentsFailed(givenError);
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onGetConsentsSuccess(new ArrayList<Consent>());
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testShouldShowErrorWhenGetConsentFails() throws Exception {
        String errorTitle = "test error title";
        String errorMessage = "test error message";
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn(errorMessage);
        whenGetConsentFailed();
        thenErrorIsShown(true, errorTitle, errorMessage);
    }

    @Test
    public void testShouldShowErrorWhenCreateConsentFails() throws Exception {
        String errorTitle = "test error title";
        String errorMessage = "test error message";
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn(errorMessage);
        whenCreateConsentFailed();
        thenErrorIsShown(false, errorTitle, errorMessage);
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
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface, givenConsentConfigurations);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    private void givenActiveClickStreamConsent() {
        BackendConsent consent = new BackendConsent(AMERICAN_LOCALE, ConsentStatus.active, "clickstream", 1);
        requiredConsent = new Consent(consent, clickStreamConsentDefinition());
    }

    private void givenRejectedClickStreamConsent() {
        BackendConsent consent = new BackendConsent(AMERICAN_LOCALE, ConsentStatus.rejected, "clickstream", 1);
        requiredConsent = new Consent(consent, clickStreamConsentDefinition());
    }

    private void givenClickStreamConsentView() {
        ConsentView consentView = new ConsentView(clickStreamConsentDefinition(), mockConsentHandler);
        List<ConsentView> consentViews = new ArrayList<>();
        consentViews.add(consentView);
        when(mockAdapter.getConsentViews()).thenReturn(consentViews);
    }

    private ConsentDefinition clickStreamConsentDefinition() {
        return new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"), 1);
    }

    public void whenCreateConsentSuccess() {
        mPermissionPresenter.onPostConsentSuccess(requiredConsent);
    }

    private void whenOnGetConsentRetrieved() {
        ArrayList<Consent> consentArrayList = new ArrayList<>();
        consentArrayList.add(requiredConsent);
        mPermissionPresenter.onGetConsentsSuccess(consentArrayList);
    }

    private void thenVerifyEnableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
    }

    private void thenVerifyDisableTaggingIsInvoked() {
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, CswInterface.getCswComponent().getAppTaggingInterface().getPrivacyConsent());
    }

    @Test
    public void testShouldShowLoaderWhenTogglingConsent() throws Exception {
        whenAppIsOnline();
        whenTogglingConsentTo(true);
        thenProgressIsShown();
    }

    @Test
    public void testShouldNotShowLoaderWhenTogglingConsent() throws Exception {
        String errorTitle = "test offline error title";
        String errorMessage = "test offline error message";
        whenAppIsOffline();
        given(mockContext.getString(R.string.csw_offline_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_offline_message)).willReturn(errorMessage);
        whenTogglingConsentTo(true);
        thenOfflineErrorIsShown(false, errorTitle, errorMessage);
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentFails() throws Exception {
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn("title");
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn("message");
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
        ConsentDefinition definition = new ConsentDefinition("", "", Collections.singletonList("moment"), 0);
        ConsentConfiguration configuration = new ConsentConfiguration(Arrays.asList(definition), mockHandlerInterface);
        givenConsentConfigurations = Arrays.asList(configuration);
        givenPresenter();
    }

    private void givenPresenter() {
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, givenConsentConfigurations, mockAdapter){
            @Override
            protected RestInterface getRestClient() {
                return restInterfaceMock;
            }
        };
        mPermissionPresenter.mContext = mockContext;
    }

    private void whenGetConsentFailed() {
        mPermissionPresenter.onGetConsentsFailed(givenError);
    }

    private void whenCreateConsentFailed() {
        mPermissionPresenter.onPostConsentFailed(null, givenError);
    }

    private void whenCreateConsentSucceeds() {
        when(mockRequiredConsent.getType()).thenReturn("");
        mPermissionPresenter.onPostConsentSuccess(mockRequiredConsent);
    }

    private void whenTogglingConsentTo(boolean toggled) {
        mPermissionPresenter.onToggledConsent(mockConsentDefinition, mockHandlerInterface, toggled);
    }

    private void whenAppIsOnline() {
        restInterfaceMock.isInternetAvailable = true;
    }

    private void whenAppIsOffline() {
        restInterfaceMock.isInternetAvailable = false;
    }

    private void thenErrorIsShown(boolean goBack, String title, String message) {
        verify(mockPermissionInterface).showErrorDialog(goBack, title, message);
    }

    private void thenOfflineErrorIsShown(boolean goBack, String title, String message) {
        verify(mockPermissionInterface).showErrorDialog(goBack, title, message);
    }

    private void thenProgressIsShown() {
        verify(mockPermissionInterface).showProgressDialog();
    }

    private void thenProgressIsHidden() {
        verify(mockPermissionInterface).hideProgressDialog();
    }
}
