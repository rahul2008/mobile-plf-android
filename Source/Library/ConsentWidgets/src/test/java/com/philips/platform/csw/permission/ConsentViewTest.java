package com.philips.platform.csw.permission;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.ConsentDefinition;

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
    public void isSwitchEnabled_falseWhenConsentIsRejected() {
        whenConsentIsRejected();
        thenSwitchIsOff();
    }

    private void whenConsentIsRejected() {
        consentView.storeConsent(rejectedConsent);
    }

    private void thenSwitchIsOff() {
        assertFalse(consentView.isSwitchEnabled());
    }

    private Consent rejectedConsent = new Consent(Locale.ENGLISH, ConsentStatus.rejected, TYPE_MOMENT, 1);
    private ConsentDefinition consentDefinition;
    private ConsentView consentView;

}