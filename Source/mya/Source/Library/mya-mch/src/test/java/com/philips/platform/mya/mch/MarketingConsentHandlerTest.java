package com.philips.platform.mya.mch;

import com.philips.cdp.registration.User;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Entreco on 19/12/2017.
 */
public class MarketingConsentHandlerTest {

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkConsent_1() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(true);
        whenCheckingConsents();
        thenMarketingConsentIsRetrieved(ConsentStatus.active);
    }

    @Test
    public void checkConsents_2() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenCheckingConsents();
        thenMarketingConsentIsRetrieved(ConsentStatus.rejected);
    }

    @Test
    public void itShouldReportErrorWhenCheckingConsentsThrowsAnException() throws Exception {
        givenConsentDefinition();
        givenMarketingConsentIsGiven(false);
        whenCheckingConsentsThrowsException();
        thenErrorCallbackIsCalled();
    }

    @Test
    public void itShouldGiveMarketingConsent() throws Exception {
        givenConsentDefinition();
        givenStatusToPost(true);
        whenPostingConsentDefinitionSucceeds();
        theMarketingConsentIsReportedToCallback(ConsentStatus.active);
    }

    @Test
    public void itShouldRejectMarketingConsent() throws Exception {
        givenConsentDefinition();
        givenStatusToPost(false);
        whenPostingConsentDefinitionSucceeds();
        theMarketingConsentIsReportedToCallback(ConsentStatus.rejected);
    }

    @Test
    public void itShouldNotGiveMarketingConsentWhenPostingAcceptedOperationFails() throws Exception {
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

    private void givenConsentDefinition() {
        givenConsentDefinition = new ConsentDefinition("txt", "help me", Collections.singletonList("type"), 42, Locale.US);
        subject = new MarketingConsentHandler(mockUser, Collections.singletonList(givenConsentDefinition), appInfra);
    }

    private void givenStatusToPost(boolean status) {
        givenStatus = status;
    }

    private void givenMarketingConsentIsGiven(boolean given) {
        when(mockUser.getReceiveMarketingEmail()).thenReturn(given);
    }

    private void whenCheckingConsents() {
        subject.fetchConsentStates(null, givenCheckConsentCallback);
        verify(mockUser).getReceiveMarketingEmail();
    }

    private void whenCheckingConsentsThrowsException() {
        when(mockUser.getReceiveMarketingEmail()).thenThrow(new RuntimeException("error offline"));
        subject.fetchConsentStates(null, givenCheckConsentCallback);
        verify(mockUser).getReceiveMarketingEmail();
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

}