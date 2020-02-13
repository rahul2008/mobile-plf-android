package com.philips.platform.appinfra.consentmanager;

import androidx.annotation.NonNull;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ConsentStatusChangeMapperTest {
    @Mock
    private ConsentStatusChangedListener statusChangedListener;
    @Mock
    private ConsentStatusChangedListener statusChangedListenerTest;

    private ConsentStatusChangeMapper consentStatusChangeMapper = new ConsentStatusChangeMapper();;

    @Test
    public void consentStatusChangedCalled_IfDefinitionRegistered() {
        ConsentDefinition consentDefinition = registerConsentDefinition();
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, null, true);
        verify(statusChangedListener).consentStatusChanged(consentDefinition, null, true);
    }

    @Test
    public void consentStatusChangedCalledOnce_IfDefinitionRegisteredTwiceForSameListener() {
        ConsentDefinition consentDefinition = registerConsentDefinition();
        consentStatusChangeMapper.registerConsentStatusUpdate(consentDefinition, statusChangedListener);
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, null, true);
        verify(statusChangedListener).consentStatusChanged(consentDefinition, null, true);
    }

    @Test
    public void consentStatusChangedCalledTwice_IfDefinitionRegisteredForTwoListeners() {
        ConsentDefinition consentDefinition = registerConsentDefinition();
        consentStatusChangeMapper.registerConsentStatusUpdate(consentDefinition, statusChangedListenerTest);
        final ConsentError consentError = getConsentError();
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, consentError, true);
        verify(statusChangedListener).consentStatusChanged(consentDefinition, consentError, true);
        verify(statusChangedListenerTest).consentStatusChanged(consentDefinition, consentError, true);
    }

    @Test
    public void consentStatusChangedCalled_IfDefinitionRegisteredWithNonRegisteredListener() {
        ConsentDefinition consentDefinition = registerConsentDefinition();
        consentStatusChangeMapper.unRegisterConsentStatusUpdate(consentDefinition, statusChangedListenerTest);
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, null, true);
        verify(statusChangedListener).consentStatusChanged(consentDefinition, null, true);
    }

    @Test
    public void consentStatusChangedNeverCalled_IfDefinitionDeRegistered() {
        ConsentDefinition consentDefinition = registerConsentDefinition();
        consentStatusChangeMapper.unRegisterConsentStatusUpdate(consentDefinition, statusChangedListener);
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, null, true);
        verifyZeroInteractions(statusChangedListener);
    }

    @Test
    public void consentStatusChangedNeverCalled_IfDefinitionNotRegistered() {
        registerConsentDefinition();
        ConsentDefinition consentDefinition = getConsentDefinitionForTesting();
        consentStatusChangeMapper.consentStatusChanged(consentDefinition, null, true);
        verifyZeroInteractions(statusChangedListener);
    }

    @NonNull
    private ConsentDefinition registerConsentDefinition() {
        ConsentDefinition consentDefinition = getConsentDefinitionForRegister();
        consentStatusChangeMapper.registerConsentStatusUpdate(consentDefinition, statusChangedListener);
        return consentDefinition;
    }

    private ConsentDefinition getConsentDefinitionForRegister() {
        return new ConsentDefinition(1, 2, Collections.singletonList("Type1"), 3);
    }

    private ConsentDefinition getConsentDefinitionForTesting() {
        return new ConsentDefinition(4, 5, Collections.singletonList("Type2"), 6);
    }

    private ConsentError getConsentError() {
        return new ConsentError("Error", 1);
    }
}