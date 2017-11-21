package com.philips.platform.csw.permission;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.csw.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Locale;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

public class CreateConsentInteractorTest {

    private CreateConsentInteractor subject;
    @Mock private ConsentAccessToolKit mockCatk;
    private ConsentView givenConsentView;
    @Captor
    private ArgumentCaptor<Consent> captorConsent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itShouldCallCreateConsentOnTheCatk() throws Exception {
        givenCreateConsentInteractor();
        givenConsentViewWithLocale(Locale.getDefault());
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowIllegalStateWhenUsingDefinitionWithLocaleThatIsMissingCountry() throws Exception {
        givenCreateConsentInteractor();
        givenConsentViewWithLocale(new Locale("nl", ""));
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowIllegalStateWhenUsingDefinitionWithLocaleThatIsMissingLanguage() throws Exception {
        givenCreateConsentInteractor();
        givenConsentViewWithLocale(new Locale("", "NL"));
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    private void givenCreateConsentInteractor() {
        subject = new CreateConsentInteractor(mockCatk);
    }

    private void givenConsentViewWithLocale(Locale locale) {
        givenConsentView = new ConsentView(new ConsentDefinition("text", "help", "moment", 0, locale));
    }

    private void whenCallingCreateConsentInGivenState(boolean checked) {
        subject.createConsentStatus(givenConsentView, checked);
    }

    private void thenCreateConsentIsCalledOnTheCatk() {
        verify(mockCatk).createConsent(captorConsent.capture(), isA(CreateConsentListener.class));
    }

}