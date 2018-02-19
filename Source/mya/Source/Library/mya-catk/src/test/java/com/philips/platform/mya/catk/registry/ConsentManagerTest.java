package com.philips.platform.mya.catk.registry;

import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConsentManagerTest {

    private static ConsentManager mConsentManager;
    private ConsentHandlerInterface mReceivedHandler;

    private ConsentDefinition mConsentDefinition;
    private List<ConsentDefinition> mConsentDefinitionList;

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

    @Test
    public void getHandler_ShouldReturnNullOnNoConsentType() {
        whenGetHandlerIsInvokedForNonExistingType();
        verifyNullHandlerIsReceived();
    }

    @Test
    public void getHandler_ShouldReturnNullOnConsentRemoved() {
        givenConsentTypeIsRemoved();
        whenGetHandlerIsInvokedForRemovedType();
        verifyNullHandlerIsReceived();
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
        SamplerHandler samplerHandler = new SamplerHandler();
        mConsentManager.register(Arrays.asList("testConsent3"), samplerHandler);
    }

    private void whenGetHandlerIsInvokedForType() {
        mReceivedHandler = mConsentManager.getHandler("testConsent3");
    }

    private void verifyRegisteredHandlerIsReceived() {
        assertEquals(mReceivedHandler.getClass(), SamplerHandler.class);
    }

    private void whenGetHandlerIsInvokedForNonExistingType() {
        mReceivedHandler = mConsentManager.getHandler("testConsent4");
    }

    private void verifyNullHandlerIsReceived() {
        assertNull(mReceivedHandler);
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
        SamplerHandler handler = new SamplerHandler();
        mConsentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("testConsent5"), 0,
                Locale.getDefault());
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentManager.register(Arrays.asList("testConsent5"), handler);
        mConsentManager.fetchConsentState(mConsentDefinition, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsSuccess(null);
    }

    @Test
    public void fetchConsentStates_ShouldReturnConsentStatus() {
        SamplerHandler handler = new SamplerHandler();
        mConsentDefinitionList = new ArrayList<>();
        mCheckConsentsCallback = mock(CheckConsentsCallback.class);
        mConsentDefinitionList.add(new ConsentDefinition("text", "help", Collections.singletonList("testConsent6"), 0,
                Locale.getDefault()));

        mConsentManager.register(Arrays.asList("testConsent6"), handler);
        mConsentManager.fetchConsentStates(mConsentDefinitionList, mCheckConsentsCallback);
        verify(mCheckConsentsCallback).onGetConsentsSuccess(null);
    }

    @Test
    public void storeConsentState_ShouldSaveTheConsentState() {
        mPostConsentCallback = mock(PostConsentCallback.class);
        mConsentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("testConsent5"), 0,
                Locale.getDefault());
        mConsentManager.storeConsentState(mConsentDefinition, true, mPostConsentCallback);
        verify(mPostConsentCallback).onPostConsentSuccess(null);
    }

    private class SamplerHandler implements ConsentHandlerInterface {
        @Override
        public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
            callback.onGetConsentsSuccess(null);
        }

        @Override
        public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
            callback.onGetConsentsSuccess(null);
        }

        @Override
        public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
            callback.onPostConsentSuccess(null);
        }
    }
}