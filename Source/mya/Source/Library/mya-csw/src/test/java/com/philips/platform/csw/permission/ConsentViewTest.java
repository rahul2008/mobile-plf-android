package com.philips.platform.csw.permission;

import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;
import com.philips.platform.mya.chi.datamodel.Consent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.*;

public class ConsentViewTest {

    private String TYPE_MOMENT = "moment";
    @Mock
    private ConsentHandlerInterface mockConsentHandler;

    @Before
    public void setUp() throws Exception {
        consentDefinition = new ConsentDefinition("SomeText", "SomeHelp", Collections.singletonList(TYPE_MOMENT), 1, Locale.CANADA);
        currentConsentRejected = new Consent(new BackendConsent(Locale.ENGLISH, ConsentStatus.rejected, TYPE_MOMENT, 1), consentDefinition);
        currentConsentAccepted = new Consent(new BackendConsent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, 1), consentDefinition);
        oldConsentAccepted = new Consent(new BackendConsent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, 0), consentDefinition);
        consentView = new ConsentView(consentDefinition, mockConsentHandler);
    }

    @Test
    public void isChecked_falseWhenConsentIsRejected() {
        whenConsentIsRejected();
        thenSwitchIsOff();
    }

    @Test
    public void isChecked_falseWhenThereIsNoConsent() {
        whenThereIsNoConsent();
        thenSwitchIsOff();
    }

    @Test
    public void isChecked_falseWhenConsentGivenButOldVersion() {
        whenConsentIsAcceptedButVersionIsOld();
        thenSwitchIsOff();
    }

    @Test
    public void isChecked_trueWhenConsentGivenAndSameVersion() {
        whenConsentIsAccepted();
        thenSwitchIsOn();
    }

    @Test
    public void isEnabled_falseWhenCurrentVersionHigherThanDefinition() {
        whenConsentIsVersion(2);
        thenSwitchIsDisabled();
    }

    @Test
    public void isEnabled_trueWhenThereIsNoConsent() {
        whenThereIsNoConsent();
        thenSwitchIsEnabled();
    }

    private void whenConsentIsVersion(int version) {
        consentView.storeConsent(new Consent(new BackendConsent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, version), consentDefinition));
    }

    @Test
    public void isEnabled_trueWhenCurrentVersionLowerThanDefinition() {
        whenConsentIsVersion(0);
        thenSwitchIsEnabled();
    }

    private void whenConsentIsAccepted() {
        consentView.storeConsent(currentConsentAccepted);
    }

    private void whenConsentIsAcceptedButVersionIsOld() {
        consentView.storeConsent(oldConsentAccepted);
    }

    private void whenThereIsNoConsent() {
        consentView.storeConsent(null);
    }

    private void whenConsentIsRejected() {
        consentView.storeConsent(currentConsentRejected);
    }

    private void thenSwitchIsOff() {
        assertFalse(consentView.isChecked());
    }

    private void thenSwitchIsOn() {
        assertTrue(consentView.isChecked());
    }

    private void thenSwitchIsDisabled() {
        assertFalse(consentView.isEnabled());
    }

    private void thenSwitchIsEnabled() {
        assertTrue(consentView.isEnabled());
    }

    private ConsentDefinition consentDefinition;
    private Consent currentConsentRejected;
    private Consent currentConsentAccepted;
    private Consent oldConsentAccepted;
    private ConsentView consentView;

}