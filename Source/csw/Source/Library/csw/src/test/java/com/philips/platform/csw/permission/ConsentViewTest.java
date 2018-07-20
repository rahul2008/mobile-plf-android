package com.philips.platform.csw.permission;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConsentViewTest {

    private String TYPE_MOMENT = "moment";
    private Date NOW = new Date();

    @Before
    public void setUp() throws Exception {
        consentDefinition = new ConsentDefinition(0, 0, Collections.singletonList(TYPE_MOMENT), 1);
        rejectedConsentDefinitionStatus = new ConsentDefinitionStatus(ConsentStates.rejected, ConsentVersionStates.InSync, consentDefinition, NOW);
        activeConsentDefinitionStatus = new ConsentDefinitionStatus(ConsentStates.active, ConsentVersionStates.InSync, consentDefinition, NOW);
        oldVersionConsentDefinitionStatus = new ConsentDefinitionStatus(ConsentStates.active, ConsentVersionStates.AppVersionIsLower, consentDefinition, NOW);
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
        thenSwitchIsOn();
    }

    @Test
    public void isChecked_trueWhenConsentGivenAndSameVersion() {
        whenConsentIsAccepted();
        thenSwitchIsOn();
    }

    @Test
    public void isEnabled_falseWhenCurrentVersionHigherThanDefinition() {
        whenConsentIsVersion(ConsentVersionStates.AppVersionIsLower);
        thenSwitchIsDisabled();
    }

    @Test
    public void isEnabled_trueWhenThereIsNoConsent() {
        whenThereIsNoConsent();
        thenSwitchIsEnabled();
    }

    @Test
    public void isEnabled_falseWhenPostConsentFails() {
        whenConsentIsVersion(ConsentVersionStates.InSync);
        andIsError(true);
        thenSwitchIsEnabled();
    }

    @Test
    public void isEnabled_trueWhenCurrentVersionLowerThanDefinition() {
        whenConsentIsVersion(ConsentVersionStates.InSync);
        thenSwitchIsEnabled();
    }

    private void whenConsentIsVersion(ConsentVersionStates version) {
        consentView.storeConsentDefnitionStatus(new ConsentDefinitionStatus(ConsentStates.active, version, consentDefinition, NOW));
    }

    private void whenConsentIsAccepted() {
        consentView.storeConsentDefnitionStatus(activeConsentDefinitionStatus);
    }

    private void whenConsentIsAcceptedButVersionIsOld() {
        consentView.storeConsentDefnitionStatus(oldVersionConsentDefinitionStatus);
    }

    private void whenThereIsNoConsent() {
        consentView.storeConsentDefnitionStatus(null);
    }

    private void whenConsentIsRejected() {
        consentView.storeConsentDefnitionStatus(rejectedConsentDefinitionStatus);
    }
    private void andIsError(boolean isError) {
        consentView.setError(isError);
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
    private ConsentDefinitionStatus rejectedConsentDefinitionStatus;
    private ConsentDefinitionStatus activeConsentDefinitionStatus;
    private ConsentDefinitionStatus oldVersionConsentDefinitionStatus;
    private ConsentView consentView;

}