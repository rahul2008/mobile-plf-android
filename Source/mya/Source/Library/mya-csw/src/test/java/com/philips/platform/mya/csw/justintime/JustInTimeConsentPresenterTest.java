package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.justintime.spy.ConsentHandlerInterfaceSpy;
import com.philips.platform.mya.csw.justintime.spy.ViewSpy;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JustInTimeConsentPresenterTest {

    private JustInTimeConsentPresenter presenter;
    private ViewSpy view;
    private AppInfraInterfaceMock appInfraMock;
    private ConsentHandlerInterfaceSpy consentHandlerInterface;
    private ConsentDefinition consentDefinition;

    @Before
    public void setup() {
        appInfraMock = new AppInfraInterfaceMock();
        view = new ViewSpy();
        consentHandlerInterface = new ConsentHandlerInterfaceSpy();
        consentDefinition = new ConsentDefinition("", "", Collections.EMPTY_LIST, 0);
        presenter = new JustInTimeConsentPresenter(view, appInfraMock, consentHandlerInterface, consentDefinition);
    }

    @Test
    public void setsPresenterOnView() {
        assertEquals(presenter, view.presenter);
    }

    @Test
    public void onConsentGivenShowsErrorWhenOffline() {
        givenUserIsOffline();
        whenGivingConsent();
        thenErrorDialogIsShown(R.string.csw_offline_title, R.string.csw_offline_message);
    }

    @Test
    public void onConsentGivenShowsProgressDialogWhenOnline() {
        givenUserIsOnline();
        whenGivingConsent();
        thenProgressDialogIsShown();
    }

    @Test
    public void onConsentGivenStoresActiveConsentStateWhenOnline() {
        givenUserIsOnline();
        whenGivingConsent();
        thenConsentStateIsStored(consentDefinition, true);
    }

    @Test
    public void onConsentRejectedStoresRejectedConsentStateWhenOnline() {
        givenUserIsOnline();
        whenRejectingConsent();
        thenConsentStateIsStored(consentDefinition, false);
    }

    private void givenUserIsOffline() {
        appInfraMock.restInterfaceMock.isInternetAvailable = false;
    }

    private void givenUserIsOnline() {
        appInfraMock.restInterfaceMock.isInternetAvailable = true;
    }

    private void whenGivingConsent() {
        presenter.onConsentGivenButtonClicked();
    }

    private void whenRejectingConsent() {
        presenter.onConsentRejectedButtonClicked();
    }

    private void thenErrorDialogIsShown(int expectedTitle, int expectedMessage) {
        assertEquals(expectedTitle, view.errorTitleId_showErrorDialog);
        assertEquals(expectedMessage, view.errorMessageId_showErrorDialog);
    }

    private void thenProgressDialogIsShown() {
        assertTrue(view.progressDialogShown);
    }

    private void thenConsentStateIsStored(ConsentDefinition definition, boolean active) {
        assertEquals(definition, consentHandlerInterface.definition_storeConsentState);
        assertEquals(active, consentHandlerInterface.status_storeConsentState);
        assertNotNull(consentHandlerInterface.callback_storeConsentState);
    }
}