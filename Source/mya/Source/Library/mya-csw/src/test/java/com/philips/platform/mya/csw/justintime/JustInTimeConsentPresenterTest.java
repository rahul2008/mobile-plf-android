package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.justintime.spy.ConsentHandlerInterfaceSpy;
import com.philips.platform.mya.csw.justintime.spy.ViewSpy;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JustInTimeConsentPresenterTest {

    private JustInTimeConsentPresenter presenter;
    private ViewSpy view;
    private AppInfraInterfaceMock appInfraMock;
    private ConsentHandlerInterfaceSpy consentHandlerInterface;

    @Before
    public void setup() {
        appInfraMock = new AppInfraInterfaceMock();
        view = new ViewSpy();
        consentHandlerInterface = new ConsentHandlerInterfaceSpy();
        presenter = new JustInTimeConsentPresenter(view, appInfraMock, consentHandlerInterface);
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
    public void OnConsentGivenShowsProgressDialogWhenOnline() {
        givenUserIsOnline();
        whenGivingConsent();
        thenProgressDialogIsShown();
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

    private void thenErrorDialogIsShown(int expectedTitle, int expectedMessage) {
        assertEquals(expectedTitle, view.errorTitleId_showErrorDialog);
        assertEquals(expectedMessage, view.errorMessageId_showErrorDialog);
    }

    private void thenProgressDialogIsShown() {
        assertTrue(view.progressDialogShown);
    }

}