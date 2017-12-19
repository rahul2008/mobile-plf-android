package com.philips.platform.mya.usr;

import com.philips.platform.consenthandlerinterface.ConsentListCallback;
import com.philips.platform.consenthandlerinterface.CreateConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Locale;

/**
 * Created by Entreco on 19/12/2017.
 */
public class MarketingConsentHandlerTest {

    @Mock
    private ConsentListCallback givenConsentListCallback;
    @Mock
    private CreateConsentCallback givenCreateConsentCallback;

    private MarketingConsentHandler subject = new MarketingConsentHandler();
    private ConsentDefinition givenConsentDefinition;
    private boolean givenStatus;

    @Test
    public void checkConsents() throws Exception {
        subject.checkConsents(givenConsentListCallback);
    }

    @Test
    public void itShouldGiveMarketingConsent() throws Exception {
        givenConsentDefinition();
        givenStatus(true);
        whenPostingConsentDefinition();
        // then marketing consent is given
    }

    @Test
    public void itShouldRejectMarketingConsent() throws Exception {
        givenConsentDefinition();
        givenStatus(false);
        whenPostingConsentDefinition();
        // then marketing consent is rejected
    }

    private void givenConsentDefinition() {
        givenConsentDefinition = new ConsentDefinition("txt", "help me", Collections.singletonList("type"), 42, Locale.US);
    }

    private void givenStatus(boolean status) {
        givenStatus = status;
    }

    private void whenPostingConsentDefinition() {
        subject.post(givenConsentDefinition, givenStatus, givenCreateConsentCallback);
    }

}