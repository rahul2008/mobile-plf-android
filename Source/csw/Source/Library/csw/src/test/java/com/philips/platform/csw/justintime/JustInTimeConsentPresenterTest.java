package com.philips.platform.csw.justintime;

import com.google.common.collect.ImmutableMap;
import com.philips.platform.csw.BuildConfig;
import com.philips.platform.csw.R;
import com.philips.platform.csw.justintime.spy.ConsentManagerInterfaceSpy;
import com.philips.platform.csw.justintime.spy.JustInTimeWidgetHandlerSpy;
import com.philips.platform.csw.justintime.spy.ViewSpy;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JustInTimeConsentPresenterTest {

    private JustInTimeConsentPresenter presenter;
    private ViewSpy view;
    private AppInfraInterfaceMock appInfraMock;
    private ConsentManagerInterfaceSpy consentManagerInterface;
    private ConsentDefinition consentDefinition;
    private JustInTimeWidgetHandlerSpy completionListener;
    private ConsentError consentError;

    @Before
    public void setup() {
        appInfraMock = new AppInfraInterfaceMock();
        view = new ViewSpy();
        consentManagerInterface = new ConsentManagerInterfaceSpy();
        appInfraMock.consentManagerInterface = consentManagerInterface;
        consentDefinition = new ConsentDefinition(0, 0, Arrays.asList("firstType", "secondType"), 0);
        consentError = new ConsentError("", 1234);
        completionListener = new JustInTimeWidgetHandlerSpy();
        presenter = new JustInTimeConsentPresenter(view, appInfraMock, consentDefinition, completionListener);
    }

    @Test
    public void setsPresenterOnView() {
        assertEquals(presenter, view.presenter);
    }

    @Test
    public void testCreateAppTaggingComponent() {
        assertEquals("CSW", appInfraMock.appTaggingComponentId);
        assertEquals(BuildConfig.VERSION_NAME, appInfraMock.appTaggingComponentVersion);
    }

    @Test
    public void trackPageNameTagsPageAndAddsConsentTypes() {
        whenTrackingPageAction();
        thenPageIsTagged("jitConsent", ImmutableMap.of("consentType", "firstType|secondType"));
    }

    @Test
    public void onConsentGivenShowsErrorWhenOffline() {
        givenUserIsOffline();
        whenGivingConsent();
        thenErrorDialogIsShown(R.string.csw_offline_title, R.string.csw_offline_message);
    }

    @Test
    public void onConsentGivenShowsProgressDialogWhenOnline() {
        whenGivingConsent();
        thenProgressDialogIsShown();
    }

    @Test
    public void onConsentGivenStoresActiveConsentStateWhenOnline() {
        whenGivingConsent();
        thenConsentStateIsStored(consentDefinition, true);
    }

    @Test
    public void onConsentRejectedStoresRejectedConsentStateWhenOnline() {
        whenRejectingConsent();
        thenConsentStateIsStored(consentDefinition, false);
    }

    @Test
    public void onConsentGivenCallsCompletionListenerOnSuccessWhenPostIsNotSuccessful() {
        givenPostFails();
        whenGivingConsent();
        thenCompletionHandlerIsNotCalledOnConsentGiven();
    }

    @Test
    public void onConsentGivenHidesProgressDialogWhenPostIsNotSuccessful() {
        givenPostFails();
        whenGivingConsent();
        thenProgressDialogIsHidden();
    }

    @Test
    public void onConsentRejectedCallsCompletionListenerOnSuccessWhenPostIsNotSuccessful() {
        givenPostFails();
        whenRejectingConsent();
        thenCompletionHandlerIsNotCalledOnConsentRejected();
    }

    @Test
    public void onConsentRejectedHidesProgressDialogWhenPostIsNotSuccessful() {
        givenPostFails();
        whenRejectingConsent();
        thenProgressDialogIsHidden();
    }

    @Test
    public void onConsentRejectedShowsErrorDialogWhenPostIsNotSuccessful() {
        givenPostFails();
        whenRejectingConsent();
        thenShowsErrorDialog();
    }

    private void givenUserIsOffline() {
        consentManagerInterface.callsCallback_onPostConsentFailed(consentDefinition, new ConsentError("", ConsentError.CONSENT_ERROR_NO_CONNECTION));
    }

    private void givenPostFails() {
        consentManagerInterface.callsCallback_onPostConsentFailed(consentDefinition, consentError);
    }

    private void whenGivingConsent() {
        presenter.onConsentGivenButtonClicked();
    }

    private void whenRejectingConsent() {
        presenter.onConsentRejectedButtonClicked();
    }

    private void whenTrackingPageAction() {
        presenter.trackPageName();
    }

    private void thenPageIsTagged(final String expectedPageName, Map<String, String> keyValues) {
        assertEquals(expectedPageName, appInfraMock.taggedPage);
        assertEquals(keyValues, appInfraMock.taggedPageValues);
    }

    private void thenErrorDialogIsShown(int expectedTitle, int expectedMessage) {
        assertEquals(expectedTitle, view.errorTitleId_showErrorDialog);
        assertEquals(expectedMessage, view.errorMessageId_showErrorDialog);
    }

    private void thenProgressDialogIsShown() {
        assertTrue(view.progressDialogShown);
    }

    private void thenProgressDialogIsHidden() {
        assertTrue(view.progressDialogHidden);
    }

    private void thenConsentStateIsStored(ConsentDefinition definition, boolean active) {
        assertEquals(definition, consentManagerInterface.definition_storeConsentState);
        assertEquals(active, consentManagerInterface.status_storeConsentState);
        assertNotNull(consentManagerInterface.callback_storeConsentState);
    }

    private void thenCompletionHandlerIsCalledOnConsentGiven() {
        assertTrue(completionListener.consentGiven);
    }

    private void thenCompletionHandlerIsNotCalledOnConsentGiven() {
        assertFalse(completionListener.consentGiven);
    }


    private void thenCompletionHandlerIsNotCalledOnConsentRejected() {
        assertFalse(completionListener.consentRejected);
    }

    private void thenShowsErrorDialog() {
        assertEquals(R.string.csw_problem_occurred_error_title, view.errorTileId_showErrorDialogForCode);
        assertEquals(consentError.getErrorCode(), view.errorCode_showErrorDialogForCode);
    }

}