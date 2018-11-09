/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw.permission;

import android.content.Context;

import com.google.common.collect.ImmutableMap;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.csw.BuildConfig;
import com.philips.platform.csw.CswConstants;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswSettings;
import com.philips.platform.csw.R;
import com.philips.platform.csw.dialogs.ConfirmDialogTextResources;
import com.philips.platform.csw.dialogs.ConfirmDialogView;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private static final List<String> CONSENT_TYPES = Collections.singletonList("coaching");
    private PermissionPresenter presenter;
    private ConsentError givenError;
    private List<ConsentDefinition> givenConsentDefinitions = new ArrayList<>();
    private ConsentDefinitionStatus consentDefinitionStatus;
    private ConsentDefinition consentDefinition;
    private ConsentView consentView;

    @Mock
    private PermissionContract.View mockView;

    @Mock
    private PermissionAdapter mockAdapter;

    @Mock
    private ConsentManagerInterface consentManagerInterface;

    @Mock
    private PermissionContract.Presenter.ConsentToggleResponse responseMock;

    @Mock
    private Context context;

    private AppInfraInterfaceMock appInfraInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        consentDefinition = new ConsentDefinition(0, 0, Collections.singletonList("moment"), 1);
        consentView = Mockito.spy(new ConsentView(consentDefinition));
        when(mockAdapter.getConsentViews()).thenReturn(Collections.singletonList(consentView));
        givenCswComponent();
        givenPresenter();
        givenConsentDefinitions();
    }

    @Test
    public void initializesViewPresenter() {
        verify(mockView).setPresenter(presenter);
    }

    @Test
    public void testCreateAppTaggingComponent() {
        assertEquals("CSW", appInfraInterface.appTaggingComponentId);
        assertEquals(BuildConfig.VERSION_NAME, appInfraInterface.appTaggingComponentVersion);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetConsentsIsCalledOnInteractor_withEmptyConsentDefinitions() {
        presenter.fetchConsentStates(Collections.EMPTY_LIST);
        verify(consentManagerInterface, never()).fetchConsentStates(givenConsentDefinitions, presenter);
    }

    @Test
    public void fetchConsentStatesShowsOfflineErrorMessageWhenNotOnline() {
        givenNoConnectionConsentError();
        givenViewIsActive();
        whenGetConsentFailed();
        thenErrorIsShown(true, R.string.csw_offline_title, R.string.csw_offline_message);
    }

    @Test
    public void trackPageNameTagsPage() {
        whenTrackingPageName();
        thenPageIsTagged("consentCenter");
    }

    @Test
    public void testShowProgressDialog() {
        givenCswComponent();
        presenter.fetchConsentStates(givenConsentDefinitions);
        verify(mockView).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() {
        givenCswComponent();
        presenter.fetchConsentStates(givenConsentDefinitions);
        verify(consentManagerInterface).fetchConsentStates(givenConsentDefinitions, presenter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testShowProgressDialog_withEmptyConsentDefinition() {
        givenCswComponent();
        presenter.fetchConsentStates(Collections.EMPTY_LIST);
        verify(mockView, never()).showProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onError() {
        givenConsentError();
        givenViewIsActive();
        presenter.onGetConsentsFailed(givenError);
        verify(mockView).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() {
        givenCswComponent();
        presenter.onGetConsentsSuccess(givenConsentDefinitionStatusList());
        verify(consentView).storeConsentDefnitionStatus(consentDefinitionStatus);
        verify(mockView).hideProgressDialog();
    }

    @Test
    public void testShouldShowErrorWhenGetConsentFails() {
        givenConsentError();
        givenViewIsActive();
        whenGetConsentFailed();
        thenErrorIsShown(true, R.string.csw_problem_occurred_error_title, givenError);
    }

    @Test
    public void testShouldShowErrorWhenCreateConsentFails() {
        givenConsentError();
        whenCreateConsentFailed();
        thenErrorIsShown(false, R.string.csw_problem_occurred_error_title, givenError);
        thenAdapterCreateConsentFailedIsCalled();
    }

    @Test
    public void testShouldShowErrorWhenCreateConsentFailsBecauseOfNoConnection() {
        givenNoConnectionConsentError();
        whenCreateConsentFailed();
        thenErrorIsShown(false, R.string.csw_offline_title, R.string.csw_offline_message);
    }

    @Test
    public void testShouldShowLoaderWhenTogglingConsent() {
        whenTogglingConsentTo(true);
        thenProgressIsShown();
    }

    @Test
    public void testTagsActionWhenAcceptingConsentSucceeds() {
        whenTogglingConsentSucceeds(true);
        thenActionIsTagged("sendData", ImmutableMap.of("specialEvents", "consentAccepted", "consentType", "moment"));
    }

    @Test
    public void testTagsActionWhenRejectingConsentSucceeds() {
        whenTogglingConsentSucceeds(false);
        thenActionIsTagged("sendData", ImmutableMap.of("specialEvents", "consentRejected", "consentType", "moment"));
    }

    @Test
    public void testTagsActionWhenTogglingMultiTypeConsentSucceeds() {
        givenMultiTypeConsent("moment", "coaching");
        whenTogglingConsentSucceeds(true);
        thenActionIsTagged("sendData", ImmutableMap.of("specialEvents", "consentAccepted", "consentType", "moment|coaching"));
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentFails() {
        givenCswComponent();
        givenConsentError();
        whenTogglingConsentFails(true);
        thenProgressIsHidden();
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentSucceeds() {
        whenTogglingConsentSucceeds(false);
        thenProgressIsHidden();
    }

    @Test
    public void testTagsActionWhenRevokeConsentPopupIsShown() {
        boolean revokeAction = true;
        whenRevokePopUpDismisses(revokeAction);
        thenTaggingIsInvoked(revokeAction);
    }

    @Test
    public void test_givenDefinitionHasNoRevokeWarning_andToggleChanged_consentGiven_thenShouldCallStoreConsentState() {
        givenCswComponent();
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1);
        presenter.onToggledConsent(1, oldDefinition, true, responseMock);
        verify(consentManagerInterface).storeConsentState((ConsentDefinition) any(), eq(true), (PostConsentCallback) any());
    }

    @Test
    public void test_givenDefinitionHasNoRevokeWarning_andToggleChanged_consentGiven_thenShouldNotCallShowDialog() {
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1);
        presenter.onToggledConsent(1, oldDefinition, true, responseMock);
        verify(mockView, never()).showConfirmRevokeConsentDialog((ConfirmDialogTextResources) any(), (ConfirmDialogView.ConfirmDialogResultHandler) any());
    }

    @Test
    public void test_givenDefinitionHasNoRevokeWarning_andToggleChanged_consentNotGiven_thenShouldCallStoreConsentState() {
        givenCswComponent();
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1);
        presenter.onToggledConsent(1, oldDefinition, false, responseMock);
        verify(consentManagerInterface).storeConsentState((ConsentDefinition) any(), eq(false), (PostConsentCallback) any());
    }

    @Test
    public void test_givenDefinitionHasNoRevokeWarning_andToggleChanged_consentNotGiven_thenShouldNotCallShowDialog() {
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1);
        presenter.onToggledConsent(1, oldDefinition, false, responseMock);
        verify(mockView, never()).showConfirmRevokeConsentDialog((ConfirmDialogTextResources) any(), (ConfirmDialogView.ConfirmDialogResultHandler) any());
    }

    @Test
    public void test_givenDefinitionHasRevokeWarning_andToggleChanged_consentGiven_thenShouldCallStoreConsentState() {
        givenCswComponent();
        int revokeWarningTextRes = R.string.mya_csw_consent_revoked_confirm_descr;
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1,
                revokeWarningTextRes);
        presenter.onToggledConsent(1, oldDefinition, true, responseMock);
        verify(consentManagerInterface).storeConsentState((ConsentDefinition) any(), eq(true), (PostConsentCallback) any());
    }

    @Test
    public void test_givenDefinitionHasRevokeWarning_andToggleChanged_consentGiven_thenShouldNotCallShowDialog() {
        int revokeWarningTextRes = R.string.mya_csw_consent_revoked_confirm_descr;
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1,
                revokeWarningTextRes);
        presenter.onToggledConsent(1, oldDefinition, true, responseMock);
        verify(mockView, never()).showConfirmRevokeConsentDialog((ConfirmDialogTextResources) any(), (ConfirmDialogView.ConfirmDialogResultHandler) any());
    }

    @Test
    public void test_givenDefinitionHasRevokeWarning_andToggleChanged_consentNotGiven_thenShouldCallStoreConsentState() {
        int revokeWarningTextRes = R.string.mya_csw_consent_revoked_confirm_descr;
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1,
                revokeWarningTextRes);
        presenter.onToggledConsent(1, oldDefinition, false, responseMock);
        verify(consentManagerInterface, never()).storeConsentState((ConsentDefinition) any(), eq(false), (PostConsentCallback) any());
    }

    @Test
    public void test_givenDefinitionHasRevokeWarning_andToggleChanged_consentNotGiven_thenShouldCallShowDialog() {
        int revokeWarningTextRes = R.string.mya_csw_consent_revoked_confirm_descr;
        ConfirmDialogTextResources expectedResources = new ConfirmDialogTextResources(
                R.string.csw_privacy_settings,
                revokeWarningTextRes,
                R.string.mya_csw_consent_revoked_confirm_btn_ok,
                R.string.mya_csw_consent_revoked_confirm_btn_cancel);
        ConsentDefinition oldDefinition = new ConsentDefinition(0, 0, CONSENT_TYPES, 1,
                revokeWarningTextRes);
        presenter.onToggledConsent(1, oldDefinition, false, responseMock);
        verify(mockView).showConfirmRevokeConsentDialog(eq(expectedResources), (ConfirmDialogView.ConfirmDialogResultHandler) any());
    }

    private ArrayList<ConsentDefinitionStatus> givenConsentDefinitionStatusList() {
        ArrayList<ConsentDefinitionStatus> consentArrayList = new ArrayList<>();
        consentDefinitionStatus = new ConsentDefinitionStatus();
        consentDefinitionStatus.setConsentDefinition(consentDefinition);
        consentDefinitionStatus.setConsentState(ConsentStates.active);
        consentDefinitionStatus.setConsentVersionState(ConsentVersionStates.AppVersionIsHigher);
        consentArrayList.add(consentDefinitionStatus);
        return consentArrayList;
    }

    private void givenCswComponent() {
        CswInterface cswInterface = new CswInterface();
        appInfraInterface = new AppInfraInterfaceMock();
        appInfraInterface.consentManagerInterface = consentManagerInterface;
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    private void givenConsentError() {
        givenError = new ConsentError("SOME ERROR", 401);
    }

    private void givenNoConnectionConsentError() {
        givenError = new ConsentError("NO CONNECTION ERROR", ConsentError.CONSENT_ERROR_NO_CONNECTION);
    }

    private void givenViewIsActive() {
        when(mockView.isActive()).thenReturn(true);
    }

    private void givenConsentDefinitions() {
        ConsentDefinition definition = new ConsentDefinition(0, 0, Collections.singletonList("moment"), 0);
        givenConsentDefinitions = Collections.singletonList(definition);
        givenPresenter();
    }

    private void givenMultiTypeConsent(String... types) {
        consentDefinition = new ConsentDefinition(-1, -1, Arrays.asList(types), 1);
    }

    private void givenPresenter() {
        presenter = new PermissionPresenter(mockView, mockAdapter);
    }

    public void whenCreateConsentSuccess() {
        presenter.onPostConsentSuccess();
    }

    private void whenGetConsentFailed() {
        presenter.onGetConsentsFailed(givenError);
    }

    private void whenCreateConsentFailed() {
        presenter.onPostConsentFailed(givenError);
    }

    private void whenCreateConsentSucceeds() {
        presenter.onPostConsentSuccess();
    }

    private void whenTogglingConsentTo(boolean toggled) {
        presenter.onToggledConsent(1, consentDefinition, toggled, null);
    }

    private void whenTogglingConsentFails(final boolean toggled) {
        whenTogglingConsentTo(toggled);
        whenCreateConsentFailed();
    }

    private void whenTogglingConsentSucceeds(final boolean accepted) {
        whenTogglingConsentTo(accepted);
        whenCreateConsentSucceeds();
    }

    private void whenTrackingPageName() {
        presenter.trackPageName();
    }

    private void whenRevokePopUpDismisses(boolean action) {
        presenter.tagRevokeConsentPopUpClicked(action);
    }

    private void thenErrorIsShown(boolean goBack, int titleRes, ConsentError error) {
        verify(mockView).showErrorDialog(goBack, titleRes, error);
    }

    private void thenErrorIsShown(boolean goBack, int titleRes, int messageRes) {
        verify(mockView).showErrorDialog(goBack, titleRes, messageRes);
    }

    private void thenActionIsTagged(String expectedAction, Map<String, String> expectedData) {
        assertEquals(expectedAction, appInfraInterface.taggedActionPageName);
        assertEquals(expectedData, appInfraInterface.taggedValues);
    }

    private void thenPageIsTagged(String expectedPageName) {
        assertEquals(expectedPageName, appInfraInterface.taggedPage);
    }

    private void thenProgressIsShown() {
        verify(mockView).showProgressDialog();
    }

    private void thenProgressIsHidden() {
        verify(mockView).hideProgressDialog();
    }


    private void thenAdapterCreateConsentFailedIsCalled() {
        verify(mockAdapter).onCreateConsentFailed(anyInt(), eq(givenError));
    }

    private void thenTaggingIsInvoked(boolean action) {
        String revokeAction = action ? CswConstants.Tagging.Action.ACTION_YES : CswConstants.Tagging.Action.ACTION_CANCEL;
        thenActionIsTagged("sendData", ImmutableMap.of(CswConstants.Tagging.IN_APP_NOTIFICATION, CswConstants.Tagging.REVOKE_CONSENT_POPUP, CswConstants.Tagging.IN_APP_NOTIFICATION_RESPONSE, revokeAction));
    }
}
