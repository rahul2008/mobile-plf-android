package com.philips.platform.mya.catk.registry;

import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConsentManagerTest {
    private static ConsentManager mConsentManager;
    private ConsentHandlerInterface mReceivedHandler;

    private Consent mConsent;
    private ConsentDefinition mConsentDefinition;
    private List<Consent> mConsentList = new ArrayList<>();
    private List<ConsentDefinition> mConsentDefinitionList;
    private ConsentError mConsentError;

    @Mock
    private CheckConsentsCallback mCheckConsentsCallback;
    @Mock
    private PostConsentCallback mPostConsentCallback;

    @BeforeClass
    public static void setUp() throws Exception {
        mConsentManager = new ConsentManager();
        givenRegisteredConsent();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        mConsentManager = null;
    }

    @Test(expected = RuntimeException.class)
    public void register_shouldThrowExceptionOnSameType() {
        whenRegisteringDuplicateConsentType();
    }

    @Test
    public void getHandler_shouldReturnRegisteredConsents() {
        givenRegisteredNewConsentType();
        whenGetHandlerIsInvokedForType();
        verifyRegisteredHandlerIsReceived();
    }

    @Test(expected = RuntimeException.class)
    public void getHandler_ShouldReturnNullOnNoConsentType() {
        whenGetHandlerIsInvokedForNonExistingType();
    }

    @Test(expected = RuntimeException.class)
    public void getHandler_ShouldReturnNullOnConsentRemoved() {
        givenConsentTypeIsRemoved();
        whenGetHandlerIsInvokedForRemovedType();
    }

    @Test
    public void removeHandler_DoNothingOnTypeNotExist() {
        whenRemoveHandlerIsInvokedForNonExistingType();
    }

    private static void givenRegisteredConsent() {
        mConsentManager.register(Arrays.asList("testConsent1", "testConsent2"), null);
    }

    private void whenRegisteringDuplicateConsentType() {
        mConsentManager.register(Arrays.asList("testConsent2"), null);
    }

    private void givenRegisteredNewConsentType() {
        SamplerHandler1 handler1 = new SamplerHandler1();
        mConsentManager.register(Arrays.asList("testConsent3"), handler1);
    }

    private void whenGetHandlerIsInvokedForType() {
        mReceivedHandler = mConsentManager.getHandler("testConsent3");
    }

    private void verifyRegisteredHandlerIsReceived() {
        assertEquals(mReceivedHandler.getClass(), SamplerHandler1.class);
    }

    private void whenGetHandlerIsInvokedForNonExistingType() {
        mReceivedHandler = mConsentManager.getHandler("testConsent4");
    }

    private void givenConsentTypeIsRemoved() {
        mConsentManager.removeHandler(Arrays.asList("testConsent1"));
    }

    private void whenGetHandlerIsInvokedForRemovedType() {
        mReceivedHandler = mConsentManager.getHandler("testConsent1");
    }

    private void whenRemoveHandlerIsInvokedForNonExistingType() {
        mConsentManager.removeHandler(Arrays.asList("testConsent0"));
    }

    @Test
    public void fetchConsentState_ShouldReturnConsentStatus() {
        SamplerHandler1 handler = new SamplerHandler1();
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent5", "testConsent6"), 0);
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentManager.register(Arrays.asList("testConsent5"), handler);
        mConsentManager.register(Arrays.asList("testConsent6"), handler);
        mConsentManager.fetchConsentState(consentDefinition, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsSuccess(mConsentList);
    }

    @Test
    public void fetchConsentState_WithDifferentTypeHandlers() {
        SamplerHandler1 handler = new SamplerHandler1();
        SampleHandler2 handler2 = new SampleHandler2();
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent7", "testConsent8", "testConsent9"), 0);
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentManager.register(Arrays.asList("testConsent7"), handler);
        mConsentManager.register(Arrays.asList("testConsent8"), handler);
        mConsentManager.register(Arrays.asList("testConsent9"), handler2);
        mConsentManager.fetchConsentState(consentDefinition, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsFailed(mConsentError);
    }

    @Test
    public void fetchConsentStates_ShouldReturnConsentStatus() {
        SamplerHandler1 handler = new SamplerHandler1();
        mConsentDefinitionList = new ArrayList<>();
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentDefinitionList.add(new ConsentDefinition("text", "help", Collections.singletonList("testConsent10"), 0));

        mConsentManager.register(Arrays.asList("testConsent10"), handler);
        mConsentManager.fetchConsentStates(mConsentDefinitionList, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsSuccess(mConsentList);
    }

    @Test
    public void fetchConsentStates_ShouldReturnConsentStatusOfDiffTypes() {
        SamplerHandler1 handler1 = new SamplerHandler1();
        SampleHandler2 handler2 = new SampleHandler2();
        mConsentDefinitionList = new ArrayList<>();
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent11", "testConsent12"), 0));
        mConsentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent17", "testConsent18"), 0));

        mConsentManager.register(Arrays.asList("testConsent11", "testConsent17", "testConsent18"), handler1);
        mConsentManager.register(Arrays.asList("testConsent12"), handler2);
        mConsentManager.fetchConsentStates(mConsentDefinitionList, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsFailed(mConsentError);
    }


    @Test
    public void storeConsentState_ShouldSaveTheConsentState() {
        SamplerHandler1 handler = new SamplerHandler1();
        mConsentManager.register(Arrays.asList("testConsent13"), handler);
        mPostConsentCallback = mock(PostConsentCallback.class);
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("testConsent13"), 0);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
        verify(mPostConsentCallback).onPostConsentSuccess(mConsent);
    }

    @Test
    public void storeConsentState_ShouldSaveTheConsentStateOfDiffTypes() {
        SamplerHandler1 handler1 = new SamplerHandler1();
        SampleHandler2 handler2 = new SampleHandler2();
        mConsentManager.register(Arrays.asList("testConsent14"), handler1);
        mConsentManager.register(Arrays.asList("testConsent15"), handler2);
        mConsentManager.register(Arrays.asList("testConsent16"), handler1);

        mPostConsentCallback = mock(PostConsentCallback.class);
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent14", "testConsent15", "testConsent16"), 0);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
        verify(mPostConsentCallback).onPostConsentFailed(mConsentDefinition, mConsentError);
    }

    private void givenConsentDefinition() {
        mConsentDefinition = new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("SomeConsent"),
                1);
    }

    private void givenReceivedConsent() {
        BackendConsent backendConsent = new BackendConsent("ec_US", ConsentStatus.active, "SomeConsent", 1);
        mConsent = new Consent(backendConsent, new ConsentDefinition("SomeText", "SomeHelpText", Collections.singletonList("SomeConsent"),
                1));
        mConsentList.add(mConsent);
    }

    private void givenConsentError() {
        mConsentError = new ConsentError("sample Error", CONSENT_ERROR);
    }

    private class SamplerHandler1 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
            givenReceivedConsent();
            callback.onGetConsentsSuccess(mConsentList);
        }

        @Override
        public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
            givenReceivedConsent();
            callback.onGetConsentsSuccess(mConsentList);
        }

        @Override
        public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
            givenReceivedConsent();
            callback.onPostConsentSuccess(mConsent);
        }
    }

    private class SampleHandler2 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
            givenConsentError();
            callback.onGetConsentsFailed(mConsentError);
        }

        @Override
        public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
            givenConsentError();
            callback.onGetConsentsFailed(mConsentError);
        }

        @Override
        public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
            givenConsentDefinition();
            givenConsentError();
            callback.onPostConsentFailed(mConsentDefinition, mConsentError);
        }
    }
}