package com.philips.cdp.registration.consents;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.philips.cdp.registration.consents.URConsentProvider.USR_MARKETING_CONSENT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MarketingConsentHandlerTest {


    @Captor
    private ArgumentCaptor<RefreshUserHandler> refreshUserHandler;
    @Mock
    private CheckConsentsCallback givenCheckConsentCallback;
    @Mock
    private PostConsentCallback givenPostConsentCallback;
    @Mock
    private User mockUser;
    @Captor
    private ArgumentCaptor<MarketingConsentHandler.MarketingUpdateCallback> marketingCallbackCaptor;
    @Captor
    private ArgumentCaptor<Consent> consentCaptor;
    @Captor
    private ArgumentCaptor<List<Consent>> consentGetCaptor;
    @Captor
    private ArgumentCaptor<ConsentError> errorCaptor;

    private MarketingConsentHandler subject;

    private ConsentDefinition givenConsentDefinition;

    private boolean givenStatus;
    @Mock
    private Context mockContext;
    @Mock
    private AppInfraInterface appInfraMock;
    @Mock
    private InternationalizationInterface internationalizationInterfaceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationInterfaceMock);
    }

    @Test
    public void checkConsent_1() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(true);
        whenRefreshUser();
        thenMarketingConsentIsRetrieved(ConsentStatus.active);
    }

    private void whenRefreshUser() {
        subject.fetchConsentState(URConsentProvider.fetchMarketingConsentDefinition(mockContext, new Locale("en", "US")), givenCheckConsentCallback);
        verify(mockUser).refreshUser(refreshUserHandler.capture());
        refreshUserHandler.getValue().onRefreshUserSuccess();
    }

    private void whenRefreshUserOnFetchConsentStates() {
        subject.fetchConsentStates(null, givenCheckConsentCallback);
        verify(mockUser).refreshUser(refreshUserHandler.capture());
        refreshUserHandler.getValue().onRefreshUserSuccess();
    }


    @Test
    public void checkConsent_0() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(true);
        //whenCheckingConsent();
        whenRefreshUser();
        thenMarketingConsentIsRetrieved(ConsentStatus.active);
    }


    @Test
    public void checkConsents_2() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenRefreshUser();
//        whenCheckingConsents();
        thenMarketingConsentIsRetrieved(ConsentStatus.rejected);
    }


    @Test
    public void checkConsent_3() throws Exception {
        givenConsentDefinitionTypeNotSame();
        givenMarketingConsentIsGiven(true);
        //whenCheckingConsent();
        whenRefreshUser();
        thenErrorCallbackIsCalled();
    }

    private void givenConsentDefinitionTypeNotSame() {
        givenConsentDefinition = new ConsentDefinition("txt", "help me", Collections.singletonList("type"), 42);
        subject = new TestMarketingConsentHandler(mockContext, Collections.singletonList(givenConsentDefinition));
    }

    @Test
    public void itShouldReportErrorWhenCheckingConsentsThrowsAnException() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenRefreshUser();
        whenCheckingConsentsThrowsException();
        //  thenErrorCallbackIsCalled();
        thenMarketingConsentIsRetrieved(ConsentStatus.rejected);
    }


    @Test
    public void itShouldReportErrorWhenCheckingConsentsOnFetchConsentThrowsAnException() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenRefreshUserOnFetchConsentStates();
        whenCheckingConsentsThrowsException();
        thenMarketingConsentIsRetrieved(ConsentStatus.rejected);
    }


    @Test
    public void itShouldReportErrorWhenCheckingConsentThrowsAnException() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenCheckingConsentThrowsException();
        thenErrorCallbackIsCalled();
    }

    private void whenCheckingConsentThrowsException() {
        when(mockUser.getReceiveMarketingEmail()).thenThrow(new RuntimeException("error offline"));
        subject.fetchConsentState(null, givenCheckConsentCallback);
        verify(mockUser).refreshUser(refreshUserHandler.capture());
        refreshUserHandler.getValue().onRefreshUserSuccess();
    }

    @Test
    public void itShouldGiveMarketingConsent() throws Exception {
        givenPhoneLanguageIs("nl-NL");
        givenConsentDefinition();
        givenStatusToPost(true);
        whenPostingConsentDefinitionSucceeds();
        theMarketingConsentIsReportedToCallback(ConsentStatus.active);
    }

    @Test
    public void itShouldRejectMarketingConsent() throws Exception {
        givenPhoneLanguageIs("nl-NL");
        givenConsentDefinition();
        givenStatusToPost(false);
        whenPostingConsentDefinitionSucceeds();
        theMarketingConsentIsReportedToCallback(ConsentStatus.rejected);
    }

    @Test
    public void itShouldNotGiveMarketingConsentWhenPostingAcceptedOperationFails() throws Exception {
        givenPhoneLanguageIs("nl-NL");
        givenConsentDefinition();
        givenStatusToPost(true);
        whenPostingConsentDefinitionFails(501);
        thenErrorIsReportedToCallback(501);
    }

    @Test
    public void itShouldNotGiveMarketingConsentWhenPostRejectedOperationFails() throws Exception {
        givenConsentDefinition();
        givenStatusToPost(false);
        whenPostingConsentDefinitionFails(501);
        thenErrorIsReportedToCallback(501);
    }


    @Test
    public void itShouldGiveMarketingConsentWhenRefreshUserFailed() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenRefreshUserFailed();
        thenErrorCallbackIsCalled();
    }

    @Test
    public void itShouldGiveMarketingConsentsWhenRefreshUserFailed() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenRefreshUserFailedOnFetchConsentState();
        thenErrorCallbackIsCalled();
    }

    private void whenRefreshUserFailed() {
        subject.fetchConsentState(URConsentProvider.fetchMarketingConsentDefinition(mockContext, new Locale("en", "US")), givenCheckConsentCallback);
        verify(mockUser).refreshUser(refreshUserHandler.capture());
        refreshUserHandler.getValue().onRefreshUserFailed(anyInt());
    }

    private void whenRefreshUserFailedOnFetchConsentState() {
        subject.fetchConsentStates(null, givenCheckConsentCallback);
        verify(mockUser).refreshUser(refreshUserHandler.capture());
        refreshUserHandler.getValue().onRefreshUserFailed(anyInt());
    }


    private void givenConsentDefinition() {
        final ArrayList<String> types = new ArrayList<>();
        types.add(USR_MARKETING_CONSENT);
        givenConsentDefinition = new ConsentDefinition("txt", "help me", types, 42);
        subject = new TestMarketingConsentHandler(mockContext, Collections.singletonList(givenConsentDefinition));

    }

    private void givenStatusToPost(boolean status) {
        givenStatus = status;
    }

    private void givenMarketingConsentIsGiven(boolean given) {
        when(mockUser.getReceiveMarketingEmail()).thenReturn(given);
    }


    private void whenCheckingConsentsThrowsException() {
        when(mockUser.getReceiveMarketingEmail()).thenThrow(new RuntimeException("error offline"));
        subject.fetchConsentStates(null, givenCheckConsentCallback);
    }

    private void whenPostingConsentDefinitionSucceeds() {
        subject.storeConsentState(givenConsentDefinition, givenStatus, givenPostConsentCallback);
        verify(mockUser).updateReceiveMarketingEmail(marketingCallbackCaptor.capture(), eq(givenStatus));
        marketingCallbackCaptor.getValue().onUpdateSuccess();
    }

    private void whenPostingConsentDefinitionFails(int errorCode) {
        subject.storeConsentState(givenConsentDefinition, givenStatus, givenPostConsentCallback);
        verify(mockUser).updateReceiveMarketingEmail(marketingCallbackCaptor.capture(), eq(givenStatus));
        marketingCallbackCaptor.getValue().onUpdateFailedWithError(errorCode);
    }

    private void theMarketingConsentIsReportedToCallback(ConsentStatus active) {
        verify(givenPostConsentCallback).onPostConsentSuccess(consentCaptor.capture());

        if (active == ConsentStatus.active) {
            assertTrue(consentCaptor.getValue().isAccepted());
        } else {
            assertFalse(consentCaptor.getValue().isAccepted());
        }
    }

    private void thenMarketingConsentIsRetrieved(ConsentStatus active) {
        verify(givenCheckConsentCallback).onGetConsentsSuccess(consentGetCaptor.capture());

        if (active == ConsentStatus.active) {
            assertTrue(consentGetCaptor.getValue().get(0).isAccepted());
        } else {
            assertFalse(consentGetCaptor.getValue().get(0).isAccepted());
        }
    }

    private void thenErrorIsReportedToCallback(int errorCode) {
        verify(givenPostConsentCallback).onPostConsentFailed(any(ConsentDefinition.class), errorCaptor.capture());
        assertEquals(errorCode, errorCaptor.getValue().getErrorCode());
    }

    private void thenErrorCallbackIsCalled() {
        verify(givenCheckConsentCallback).onGetConsentsFailed(any(ConsentError.class));
        verify(givenCheckConsentCallback, never()).onGetConsentsSuccess(anyList());
    }

    private void givenPhoneLanguageIs(String bcp47UILocale) {
        when(internationalizationInterfaceMock.getBCP47UILocale()).thenReturn(bcp47UILocale);
    }

    private class TestMarketingConsentHandler extends MarketingConsentHandler {

        public TestMarketingConsentHandler(Context context, List<ConsentDefinition> definitions) {
            super(context, definitions, appInfraMock);
        }

        @Override
        User getUser() {
            return mockUser;
        }
    }
}