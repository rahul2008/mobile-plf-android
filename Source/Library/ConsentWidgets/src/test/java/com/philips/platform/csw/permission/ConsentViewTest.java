package com.philips.platform.csw.permission;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class ConsentViewTest {

    private String TYPE_MOMENT = "moment";

    @Before
    public void setUp() throws Exception {
        consentDefinition = new ConsentDefinition("SomeText", "SomeHelp", TYPE_MOMENT, 1, Locale.CANADA);
        consentView = new ConsentView(consentDefinition);
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
        consentView.storeConsent(new Consent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, version));
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

    private Consent currentConsentRejected = new Consent(Locale.ENGLISH, ConsentStatus.rejected, TYPE_MOMENT, 1);
    private Consent currentConsentAccepted = new Consent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, 1);
    private Consent oldConsentAccepted = new Consent(Locale.ENGLISH, ConsentStatus.active, TYPE_MOMENT, 0);
    private ConsentDefinition consentDefinition;
    private ConsentView consentView;

}