package com.philips.platform.csw.permission;

import android.test.mock.MockContext;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.catk.ConsentAccessToolKitEmulator;
import com.philips.platform.consenthandlerinterface.ConsentConfiguration;
import com.philips.platform.consenthandlerinterface.ConsentError;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.datamodel.BackendConsent;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentStatus;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentError givenError;
    private Consent requiredConsent;
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
        mPermissionPresenter.onGetConsentsFailed(givenError);
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        mPermissionPresenter.onGetConsentsSuccess(new ArrayList<Consent>());
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
        BackendConsent consent = new BackendConsent(new Locale("en", "US"), ConsentStatus.active, "clickstream", 1);
        ConsentDefinition consentDefinition = new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"),
                1, new Locale("en", "US"));
        requiredConsent = new Consent(consent, consentDefinition);
    }

    private void givenRejectedClickStreamConsent() {
        BackendConsent consent = new BackendConsent(new Locale("en", "US"), ConsentStatus.rejected, "clickstream", 1);
        ConsentDefinition consentDefinition = new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("clickstream"),
                1, new Locale("en", "US"));
        requiredConsent = new Consent(consent, consentDefinition);
    }

    public void whenCreateConsentSuccess() {
        mPermissionPresenter.onPostConsentSuccess(requiredConsent);
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
